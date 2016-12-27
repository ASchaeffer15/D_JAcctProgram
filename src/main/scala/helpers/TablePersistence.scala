package main.scala.helpers

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Calendar

import main.scala.structures.Table

/**
  * Created by Schaeffer on 4/16/2016.
  */
object TablePersistence {

  def move(fileName: String, appDir: String): Unit = {
    val dateTime = new SimpleDateFormat("MM_dd_yyyy").format(Calendar.getInstance().getTime)
    val destFile = new File(s"$appDir\\Expense_App_Logs\\Tables\\Archived\\${dateTime}_$fileName")
    if(destFile.exists()) destFile.delete()
    val file = new File(s"$appDir\\Expense_App_Logs\\Tables\\Current\\$fileName")
    file.renameTo(destFile)
  }

  def writeOut(table: Table, appDir: String): Unit = {
    val file = new File(s"$appDir\\Expense_App_Logs\\Tables\\Current\\${table.name}.xml")
    if (!file.exists()) file.createNewFile()
    val writer = new PrintWriter(file)
    writer.write(table.toString)
    writer.close()
  }

}
