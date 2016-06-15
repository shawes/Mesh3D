package mesh.io

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import mesh.shapes.Quadrat

import scala.collection.AbstractSeq
import scala.collection.mutable.ArrayBuffer

/**
  *
  * Created by Steven Hawes on 7/06/16.
  */
class MeshCsvWriter {
  def write(file: String,
            files: List[File],
            quadrats: List[Quadrat],
            sizeOfQuadrat: Double,
            areas3d: List[AbstractSeq[Any] with java.io.Serializable],
            areas2d: List[AbstractSeq[Any] with java.io.Serializable]): Unit = {

    val quadratInfo: List[String] = getQuadratCoordinates(quadrats)
    val three_list = List.fill(quadratInfo.size)("3")
    val two_list = List.fill(quadratInfo.size)("2")
    val csv3dOutput = quadratInfo :: three_list :: areas3d
    val csv2dOutput = quadratInfo :: two_list :: areas2d


    val names = files.map(x => x.getName)
    val f = new File(file)
    val writer = CSVWriter.open(f)
    writer.writeRow(List("", "width"))
    writer.writeRow(List("bounding box size", sizeOfQuadrat))
    writer.writeRow(List("quadrat size (m)", sizeOfQuadrat))
    writer.writeRow(List("quadrat centroid", "dimension") :: names)
    writer.writeAll(csv3dOutput.transpose)
    writer.writeRow("quadrat" :: names)
    writer.writeAll(csv2dOutput.transpose)
    writer.close()

  }

  private def getQuadratCoordinates(quadrats: List[Quadrat]): List[String] = {

    val quadratInfo: ArrayBuffer[String] = new ArrayBuffer[String]
    for (quadrat <- quadrats) quadratInfo += getQuadratCentroidAsString(quadrat)
    quadratInfo.toList
  }

  private def getQuadratCentroidAsString(q: Quadrat): String = {
    "(" + q.centroid.x + "," + q.centroid.y + ")"
  }
}
