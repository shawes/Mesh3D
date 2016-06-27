package mesh.io

import java.io.File

import mesh.shapes.{DimensionOrder, Face, Mesh, Vertex}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  *
  * Created by Steven Hawes on 24/06/16.
  */
class ObjReader(verbose: Boolean, order: DimensionOrder) extends MeshFileTypeReader {

  def read(file: File): Mesh = {
    val source = Source.fromFile(file)
    val vertices = new ArrayBuffer[Vertex]()
    val faces = new ArrayBuffer[Face]()
    var isZeroVn = false
    for (line <- source.getLines) {
      val instructions = line.split(" ")
      if (instructions.nonEmpty) {
        instructions.head match {
          case "v" => if (!isZeroVn) vertices += createVertex(instructions)
          case "vn" => if (instructions(1).toDouble == 0.0) isZeroVn = true else isZeroVn = false
          case "f" => faces += createFace(instructions, vertices)
          case _ =>
        }
      }
    }
    if (verbose) println("Vertices: " + vertices.size + ", Faces: " + faces.size)
    new Mesh(vertices.toList, faces.toList)
  }

  private def createVertex(instructions: Array[String]) = {
    order.getVertex(instructions(1).toDouble, instructions(2).toDouble, instructions(3).toDouble)
  }

  private def createFace(instructions: Array[String], vertices: ArrayBuffer[Vertex]) = {
    val v1 = instructions(1).split("/")
    val indexV1 = v1.head.toInt
    val v2 = instructions(2).split("/")
    val indexV2 = v2.head.toInt
    val v3 = instructions(3).split("/")
    val indexV3 = v3.head.toInt
    new Face(vertices(indexV1 - 1), vertices(indexV2 - 1), vertices(indexV3 - 1))
  }

}
