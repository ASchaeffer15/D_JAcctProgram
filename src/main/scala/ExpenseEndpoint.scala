package main.scala

import java.io.File
import java.nio.charset.Charset
import java.nio.file.{Files, Paths}

import akka.actor.{Actor, ActorRefFactory}
import main.scala.structures.{Table, Row, Cell}
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.routing.HttpService
import spray.routing.RejectionHandler.Default

import scala.collection.JavaConversions._

/**
  * Created by Schaeffer on 2/14/2016.
  */


class ExpenseEndpoint(tool: ExpenseApp) extends Actor with HttpService with SprayJsonSupport with DefaultJsonProtocol {


  implicit val CellFormat = jsonFormat(Cell, "header", "value")
  implicit val RowFormat = jsonFormat(Row, "row")
  implicit val ListRowFormat = listFormat(RowFormat)
  implicit val TableFormat = jsonFormat(Table, "name", "headers", "data")
  implicit val ReportFormat = jsonFormat(Report, "name", "fromDate", "toDate")

  case class Report(name: String, fromDate: String, toDate: String)


  override implicit def actorRefFactory: ActorRefFactory = context

  implicit val system = context.system

  override def receive = runRoute(
    indexRoute ~ putData ~ getDataRoute ~ openSqlApp ~ deleteData ~ generateReport
  )

  val indexRoute = {
    path("") {
      respondWithMediaType(`text/html`) {
        get {
          complete {
            val path = Paths.get("src/main/resources/ExpenseApp/index.html")
            Files.readAllLines(path, Charset.defaultCharset).mkString("\n")
          }
        }
      }
    } ~
      path("controllers" / Segment) { page =>
        respondWithMediaType(`application/javascript`) {
          get {
            complete {
              val path = Paths.get(s"src/main/resources/ExpenseApp/controllers/$page")
              Files.readAllLines(path, Charset.defaultCharset).mkString("\n")
            }
          }
        }
      } ~
      path("partials" / Segment) { page =>
        respondWithMediaType(`text/html`) {
          get {
            complete {
              val path = Paths.get(s"src/main/resources/ExpenseApp/partials/$page")
              Files.readAllLines(path, Charset.defaultCharset).mkString("\n")
            }
          }
        }
      } ~
      path("AngularJs" / Segment) { page =>
        respondWithMediaType(`application/javascript`) {
          get {
            complete {
              val path = Paths.get(s"src/main/resources/ExpenseApp/AngularJs/$page")
              Files.readAllLines(path, Charset.defaultCharset).mkString("\n")
            }
          }
        }
      } ~
      path("css" / Segment) { page =>
        respondWithMediaType(`text/css`) {
          get {
            complete {
              val path = Paths.get(s"src/main/resources/ExpenseApp/css/$page")
              Files.readAllLines(path, Charset.defaultCharset).mkString("\n")
            }
          }
        }
      } ~
      path("fonts" / Segment) { page =>
        get {
          getFromFile {
            val path = Paths.get(s"src/main/resources/ExpenseApp/fonts/$page")
            new File(path.toString)
          }
        }
      }  ~
      path("images" / Segment) { page =>
        get {
          getFromFile {
            val path = Paths.get(s"src/main/resources/ExpenseApp/images/$page")
            new File(path.toString)
          }
        }
      }
  }

  val getDataRoute = {
    path("getData" / Segment) { table =>
      respondWithMediaType(`application/json`) {
        get {
          complete(
            tool.getTableData(table)
          )
        }
      }
    }
  }

  val helloRoute = {
    path("helloWorld2") {
      get {
        complete {
          // respond with a set of HTML elements
          """<html>
            <body>
              <h1>Path 2</h1>
            </body>
          </html>
          """
        }
      }
    }
  }

  val putData = {
    path("addData") {
      respondWithMediaType(`application/json`) {
        post {
          entity(as[Table]) { request =>
            detach() {
              complete {
                tool.addTableData(request.name, request.data.head)
                ""
              }
            }
          }
        }
      }
    }
  }

  val deleteData = {
    path("removeData") {
      respondWithMediaType(`application/json`) {
        post {
          entity(as[Table]) { request =>
            detach() {
              complete {
                tool.removeTableData(request.name, request.data.head)
                ""
              }
            }
          }
        }
      }
    }
  }

  val generateReport = {
    path("report") {
      respondWithMediaType(`application/json`) {
        post {
          entity(as[Report]) { request =>
            detach() {
              complete {
                request.name match {
                  case "Full" => tool.basicReport(request.fromDate,request.toDate)
                  case "Supplier" => tool.reportBySupplier(request.fromDate,request.toDate)
                  case "Category" => tool.reportByCategory(request.fromDate,request.toDate)
                  case "Monthly" => tool.monthlyReport(request.fromDate,request.toDate)
                  case "Total" => tool.totalReport(request.fromDate,request.toDate)
                }
              }
            }
          }
        }
      }
    }
  }

  val openSqlApp = {
    path("sql") {
      get {
        complete {
          tool.terminal()
          "true"
        }
      }
    }
  }


}