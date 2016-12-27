package main.scala.structures

/**
  * Created by Schaeffer on 2/14/2016.
  */
case class Table(name: String, headers: Seq[String], var data: List[Row]) {

  override def toString: String =
    s"""
     <table>
      <meta>
        <name>$name</name>
        <headers>
      ${
      headers.map {
        header =>
          s"<header>$header</header>"
      }.mkString("\n")
    }
        </headers>
      </meta>
      <data>
      ${
      data.map(_.toString).mkString("\n")
    }
      </data>
     </table>
    """.stripMargin


}