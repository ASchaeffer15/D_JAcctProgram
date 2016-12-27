package main

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import main.scala.{ExpenseApp, ExpenseEndpoint}
import spray.can.Http

import _root_.scala.concurrent.duration._

/**
  * Created by Schaeffer on 2/14/2016.
  */
object Main extends App {

  override def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("expense")
    val app = new ExpenseApp
    app.initilize()
    val service = system.actorOf(Props(new ExpenseEndpoint(app)), "sj-rest-service")

    // IO requires an implicit ActorSystem, and ? requires an implicit timeout

    // Bind HTTP to the specified service.

    implicit val timeout = Timeout(5 second)

    IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)

  }

}
