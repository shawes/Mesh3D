package mesh.io

import java.io.File

import scala.io.Source
import scala.xml.pull.{EvElemEnd, EvElemStart, XMLEventReader}
import scala.xml.{Node, XML}

class MeshReader() {

  def readPull(file: File): (Iterator[Char], Iterator[Char]) = {
    val xml = new XMLEventReader(Source.fromFile(file))
    var insideIndexedFaceSet = false
    var faces: Seq[Seq[Node]] = Nil
    var vertices: Seq[Seq[Node]] = Nil
    while (xml.hasNext) {
      xml.next() match {
        case EvElemStart(_, "IndexedFaceSet", attributes, _) =>
          faces = faces :+ attributes("coordIndex")
          insideIndexedFaceSet = true
        case EvElemEnd(_, "IndexedFaceSet") =>
          insideIndexedFaceSet = false
        case EvElemStart(_, "Coordinate", attributes, _) =>
          if (insideIndexedFaceSet) vertices = vertices :+ attributes("point")
        case _ => ()
      }
    }
    println("Finished getting here, there were (vertcies,faces)" + vertices.size + " " + faces.size)
    new Tuple2(faces.head.text.iterator, vertices.head.text.iterator)
  }

  def readDOM(file: File): (String, String) = {
    val xml = XML.loadFile(file)

    val faces = (xml \\ "X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \ "@coordIndex").text
    val vertices = (xml \\"X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \"Coordinate" \ "@point").text

    new Tuple2(faces,vertices)
  }
}