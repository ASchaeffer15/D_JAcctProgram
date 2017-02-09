package main.scala

import java.io.{File, PrintWriter}
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.Calendar

import main.scala.helpers.TablePersistence
import main.scala.structures.Row
import org.h2.jdbcx.JdbcConnectionPool
import spray.routing.Route

import scala.collection.mutable.ListBuffer
import scala.collection.parallel.mutable
import scala.sys.process.Process
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, XML}

/**
  * Created by Schaeffer on 2/12/2016.
  */
class ExpenseApp extends App {

  var appDir: String = _

  private def getFiles: List[File] = {
    val files = new File(s"$appDir\\Expense_App_Logs\\Tables\\Current")
    val filesArray = files.listFiles
    if (filesArray == null) {
      List()
    } else {
      filesArray.filter(_.isFile).toList
    }
  }

  def terminal() = AppData.terminal()

  def initilize() = {
    appDir = """E:\Data""".stripMargin
    AppData.initilize(getFiles)
  }

  def getTableData(table: String): List[Row] = AppData.getTableData(table)

  def addTableData(table: String, row: Row): Unit = {
    val tableObject = AppData.addTableData(table, row)
    TablePersistence.move(s"$table.xml", appDir)
    TablePersistence.writeOut(tableObject, appDir)
  }

  def removeTableData(table: String, row: Row): Unit = {
    val tableObject = AppData.removeTableData(table, row)
    TablePersistence.move(s"$table.xml", appDir)
    TablePersistence.writeOut(tableObject, appDir)
  }

  def basicReport(fromDate: String, toDate: String) = AppData.getBasicReport(fromDate, toDate)

  def reportBySupplier(fromDate: String, toDate: String) = AppData.getReportBySupplier(fromDate, toDate)

  def reportByCategory(fromDate: String, toDate: String) = AppData.getReportByCategory(fromDate, toDate)

  def monthlyReport(fromDate: String, toDate: String) = AppData.getReportGetMonthlyTotal(fromDate, toDate)

  def totalReport(fromDate: String, toDate: String) = AppData.getReportGetGrandTotal(fromDate, toDate)


}
