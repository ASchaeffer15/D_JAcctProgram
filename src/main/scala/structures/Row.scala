package main.scala.structures

/**
  * Created by Schaeffer on 2/14/2016.
  */
case class Row(row: Seq[Cell]) {
  override def toString: String = {
    s"""
      <row>
        ${row.map(_.toString).mkString("\n")}
      </row>
    """.stripMargin
  }
}
