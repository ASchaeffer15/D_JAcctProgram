package main.scala.structures

/**
  * Created by Schaeffer on 2/14/2016.
  */
case class Cell(header: String, value: String) {

  override def toString: String ={
    s"""<cell key="$header" value="$value"/>"""
  }
}
