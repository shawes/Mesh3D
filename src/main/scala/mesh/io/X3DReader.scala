package mesh.io

import java.io.File

import mesh.shapes.{DimensionOrder, Face, Mesh, Vertex}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.xml.pull.{EvElemEnd, EvElemStart, XMLEventReader}
import scala.xml.{Node, XML}

/**
  *
  * @param verbose
  * @param order
  */
class X3DReader(verbose: Boolean, order: DimensionOrder) extends MeshFileTypeReader {

  def read(file: File): Mesh = {
    readPull(file)
  }

  private def readPull(file: File): Mesh = {
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
    val verticesList = constructVerticesList(vertices.head.text.iterator)
    val facesList = constructFacesList(faces.head.text.iterator, verticesList.toArray)
    new Mesh(verticesList, facesList)
  }

  private def constructVerticesList(iterator: Iterator[Char]): List[Vertex] = {

    val verticesBuffer = new ArrayBuffer[Vertex]

    while (iterator.hasNext) {
      val x = getNextNumber(iterator)
      val y = getNextNumber(iterator)
      val z = getNextNumber(iterator)

      verticesBuffer += order.getVertex(x, y, z)

    }
    if (verbose) println("Constructed vertices, there were " + verticesBuffer.size)
    verticesBuffer.toList
  }

  /*
 Uses the constants X,Y,Z to determine the width and the length of the mesh as orientated
 in the mesh. It assumes the width is X, the length is Y and Z is the height.
  */

  private def getNextNumber(iterator: Iterator[Char]): Double = {
    val numberString: StringBuilder = new StringBuilder()

    while ((numberString.isEmpty || numberString.last != ' ') && iterator.hasNext) {
      numberString + iterator.next()
    }
    var number = 0.0
    if (numberString.nonEmpty) {
      try {
        number = numberString.toString().trim.toDouble
      } catch {
        case e: java.lang.NumberFormatException => println("Can't parse this " + numberString.toString())
      }
    }
    number
  }

  private def constructFacesList(iterator: Iterator[Char], vertices: Array[Vertex]): List[Face] = {
    val facesBuffer = new ArrayBuffer[Face]()
    while (iterator.hasNext) {
      val face = new Face(vertices(getNextNumber(iterator).toInt), vertices(getNextNumber(iterator).toInt), vertices(getNextNumber(iterator).toInt))
      facesBuffer += face
      getNextNumber(iterator).toInt // This is the -1 separator
    }
    if (verbose) println("Constructed faces, there were " + facesBuffer.size)
    facesBuffer.toList
  }

  private def readDOM(file: File): (String, String) = {
    val xml = XML.loadFile(file)

    val faces = (xml \\ "X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \ "@coordIndex").text
    val vertices = (xml \\ "X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \ "Coordinate" \ "@point").text

    new Tuple2(faces, vertices)
  }

}
