package main.scala

import java.io.File
import java.sql.{ResultSet, Connection}

import main.scala.helpers.AccessSqls
import main.scala.structures.{Table, Row, Cell}
import org.h2.jdbcx.JdbcConnectionPool

import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, XML}

/**
  * Created by Schaeffer on 2/14/2016.
  */
object AppData {


  var conn: Connection = _

  def terminal(): Unit = {
    org.h2.tools.Server.startWebServer(conn)
  }

  def initilize(filesArray: List[File]) {
    val ds = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "user", "password")
    conn = ds.getConnection()
  filesArray.foreach {
      file =>
        val xmlFile = XML.loadFile(file)
        val name = (xmlFile \\ "name").map(_.text).head
        val headers = getHeaders(xmlFile)
        val data = getData(xmlFile)
        conn.createStatement().execute(AccessSqls.createTable(name, headers.toList))
        AppData.data = AppData.data :+ name
        val inserts: List[String] = AccessSqls.insertData(name, headers.toList, data)
        inserts.foreach(conn.createStatement().execute)
    }
    filesArray
  }

  var data: mutable.Buffer[String] = new mutable.ArrayBuffer[String]


  private def getHeaders(xml: Elem): List[String] = {
    (xml \\ "header").map(_.text).toList
  }


  private def getData(xml: Elem): List[Row] = {
    (xml \\ "row").map {
      row =>
        Row(
          (row \ "cell").map {
            cell =>
              Cell(cell \ "@key" text, cell \ "@value" text)
          }
        )
    }.toList
  }

  def getTableData(table: String): List[Row] = {
    Try(conn.createStatement().executeQuery(AccessSqls.getDatabaseData(table))) match {
      case Success(result) =>
        extractDataFromDb(result)
      case Failure(e) =>
        List[Row]()
    }
  }

  def getBasicReport(fromDate: String, toDate: String): List[Row] = {
    Try(conn.createStatement().executeQuery(AccessSqls.generateBasicReport(fromDate,toDate))) match {
      case Success(result) =>
        extractDataFromDb(result)
      case Failure(e) =>
        List[Row]()
    }
  }

  def getReportBySupplier(fromDate: String, toDate: String): List[Row] = {
    Try(conn.createStatement().executeQuery(AccessSqls.generateReportBySupplier(fromDate,toDate))) match {
      case Success(result) =>
        extractDataFromDb(result)
      case Failure(e) =>
        List[Row]()
    }
  }

  def getReportGetGrandTotal(fromDate: String, toDate: String): List[Row] = {
    Try(conn.createStatement().executeQuery(AccessSqls.generateTotalReport(fromDate,toDate))) match {
      case Success(result) =>
        extractDataFromDb(result)
      case Failure(e) =>
        List[Row]()
    }
  }
  def getReportGetMonthlyTotal(fromDate: String, toDate: String): List[Row] = {
    Try(conn.createStatement().executeQuery(AccessSqls.generateMonthlyReport(fromDate,toDate))) match {
      case Success(result) =>
        extractDataFromDb(result)
      case Failure(e) =>
        List[Row]()
    }
  }

  def addTableData(table: String, row: Row): Table = {
    val headers = row.row.map(_.header).toList
    if (!AppData.data.contains(table)) {
      conn.createStatement().execute(AccessSqls.createTable(table, headers))
    }
    val dataInserts = AccessSqls.insertData(table, headers, List(row))
    dataInserts.foreach {
      rowSql =>
        conn.createStatement().execute(rowSql)
    }
    Table(table, headers, getTableData(table))
  }

  def removeTableData(table: String, row: Row): Table = {
    val headers = row.row.map(_.header).toList
    if (!AppData.data.contains(table)) {
      conn.createStatement().execute(AccessSqls.createTable(table, headers))
    }
    val dataDeletion = AccessSqls.deleteData(table, headers, row)
    conn.createStatement().execute(dataDeletion)
    Table(table, headers, getTableData(table))

  }

  private def extractDataFromDb(result: ResultSet): List[Row] = {
    val numColumns = result.getMetaData.getColumnCount
    var headers: List[String] = List()
    for (x <- 1 to numColumns) {
      headers = result.getMetaData.getColumnName(x) :: headers
    }
    var rows: List[Row] = List()
    while (result.next())
      rows = Row(
        headers.map {
          header =>
            Cell(header, result.getString(header))
        }
      ) :: rows
    rows
  }
}
