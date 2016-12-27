package main.scala.helpers

import main.scala.structures.Row
import org.joda.time.DateTime

/**
  * Created by Schaeffer on 4/16/2016.
  */
object AccessSqls {

  def createTable(tableName: String, headers: List[String]) =
    s"""
      CREATE TABLE $tableName (
      ${
      headers.map {
        x =>
          s"$x ${
            x match {
              case amt if amt.contains("AMT") => "DECIMAL(20, 2)"
              case id if id.contains("ID") => "DECIMAL(20, 2)"
              case date if date.contains("DATE") => "DATE"
              case any => "VARCHAR(255)"
            }
          }"
      }.mkString(",\n")
    }
      )
    """.stripMargin

  def insertData(tableName: String, headers: List[String], data: List[Row]) = {
    data.map {
      row => s"""
      INSERT INTO $tableName (
      ${headers.map(x => x).mkString(",")}
      ) VALUES (
        ${headers.map(x => s"'${setValue(x , row.row.find(_.header == x).get.value)}'").mkString(",")}
      )
    """.stripMargin
    }
  }

  def deleteData(tableName: String, headers: List[String], data: Row) = {
    s"""
      DELETE FROM $tableName
      WHERE ${
      headers.map {
        x =>
          s"$x  = '${setValue(x,data.row.find(_.header == x).get.value)}'"
      }.mkString("\n AND ")
    }
    """.stripMargin
  }

  def getDatabaseData(tableName: String) = {
    s"""
      SELECT * FROM ${tableName.toUpperCase}
    """.stripMargin
  }

  def generateBasicReport(fromDate: String, toDate: String) = {
    s"""
      SELECT
       SUPPLIER_CATEGORY AS "Category",
       SUPPLIER_NAME AS "Supplier Name",
       DATE AS "Expense Date",
       EXPENSE_AMT AS "Total Amount",
       PERCENTAGE_AMT AS "Percentage" ,
       (EXPENSE_AMT * PERCENTAGE_AMT) AS "D AND J AMOUNT",
       USER AS "Entered By"
       FROM EXPENSES
       WHERE 1=1
       AND DATE >= '${setValue("DATE", fromDate)}'
       AND DATE <= '${setValue("DATE", toDate)}'
    """.stripMargin
  }

  def generateReportBySupplier(fromDate: String, toDate: String) = {
    s"""
      SELECT
       SUPPLIER_CATEGORY AS "Category",
       SUPPLIER_NAME AS "Supplier Name",
       SUM(EXPENSE_AMT * PERCENTAGE_AMT) AS "Total"
       FROM EXPENSES
       WHERE 1=1
       AND DATE >= '${setValue("DATE", fromDate)}'
       AND DATE <= '${setValue("DATE", toDate)}'
       GROUP BY
       SUPPLIER_CATEGORY,
       SUPPLIER_NAME
       """.stripMargin
  }

  def generateTotalReport(fromDate: String, toDate: String) = {
    s"""
      SELECT
       'D AND J' AS "Company",
       SUM(EXPENSE_AMT * PERCENTAGE_AMT) AS "Total"
       FROM EXPENSES
       WHERE 1=1
       AND DATE >= '${setValue("DATE", fromDate)}'
       AND DATE <= '${setValue("DATE", toDate)}'
    """.stripMargin
  }

  def generateMonthlyReport(fromDate: String, toDate: String) = {
    s"""
      SELECT
       'D AND J' AS "Company",
       CASE WHEN MONTH(DATE) = 1  THEN 'JAN'
       WHEN MONTH(DATE) = 2  THEN 'FEB'
       WHEN MONTH(DATE) = 3  THEN 'MAR'
       WHEN MONTH(DATE) = 4  THEN 'APR'
       WHEN MONTH(DATE) = 5  THEN 'MAY'
       WHEN MONTH(DATE) = 6  THEN 'JUN'
       WHEN MONTH(DATE) = 7  THEN 'JUL'
       WHEN MONTH(DATE) = 8  THEN 'AUG'
       WHEN MONTH(DATE) = 9  THEN 'SEP'
       WHEN MONTH(DATE) = 10 THEN 'OCT'
       WHEN MONTH(DATE) = 11 THEN 'NOV'
       WHEN MONTH(DATE) = 12 THEN 'DEC'
       END AS "Month",
       SUM(EXPENSE_AMT * PERCENTAGE_AMT) AS "Total",
       |MONTH(DATE)
       FROM EXPENSES
       WHERE 1=1
       AND DATE >= '${setValue("DATE", fromDate)}'
       AND DATE <= '${setValue("DATE", toDate)}'
       GROUP BY
       CASE WHEN MONTH(DATE) = 1  THEN 'JAN'
       WHEN MONTH(DATE) = 2  THEN 'FEB'
       WHEN MONTH(DATE) = 3  THEN 'MAR'
       WHEN MONTH(DATE) = 4  THEN 'APR'
       WHEN MONTH(DATE) = 5  THEN 'MAY'
       WHEN MONTH(DATE) = 6  THEN 'JUN'
       WHEN MONTH(DATE) = 7  THEN 'JUL'
       WHEN MONTH(DATE) = 8  THEN 'AUG'
       WHEN MONTH(DATE) = 9  THEN 'SEP'
       WHEN MONTH(DATE) = 10 THEN 'OCT'
       WHEN MONTH(DATE) = 11 THEN 'NOV'
       WHEN MONTH(DATE) = 12 THEN 'DEC'
       END,
       MONTH(DATE)
       ORDER BY
       MONTH(DATE) DESC
    """.stripMargin
  }

//
//  SELECT
//  'D AND J' AS "Company",
//  CASE WHEN MONTH(DATE) = '10' THEN 'OCT' ELSE 'NA' END AS "Month",
//  SUM(EXPENSE_AMT * PERCENTAGE_AMT) AS "Total"
//  FROM EXPENSES
//    WHERE 1=1
//  --AND DATE >= '${setValue("DATE", fromDate)}'
//  --AND DATE <= '${setValue("DATE", toDate)}'
//  GROUP BY
//    CASE WHEN MONTH(DATE) = '10' THEN 'OCT' ELSE 'NA' END


  private def setValue(header: String, value: String): String = {
    header match {
      case date if date.toUpperCase.contains("DATE") => new DateTime(value).toString("YYYY-MM-dd")
      case _ => value
    }
  }

}
