import java.io.File
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path, Paths}

import scala.collection.JavaConversions._
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
object S$R extends App {

  private def getFiles: List[File] = {
    val appDir = """D:\data""".stripMargin
    val files = new File(s"$appDir\\Expense_App_Logs\\Tables\\Current")
    val filesArray = files.listFiles
    if (filesArray == null) {
      List()
    } else {
      filesArray.filter(_.isFile).toList
    }
  }



val files = getFiles
  val charset = StandardCharsets.UTF_8

  files.foreach {
    x =>
      var content = new String(Files.readAllBytes(Paths.get(x.getAbsolutePath)), charset)
      content = content.replaceAll("&", "&amp;")
      Files.write(Paths.get(x.getAbsolutePath), content.getBytes(charset))

  }


}
