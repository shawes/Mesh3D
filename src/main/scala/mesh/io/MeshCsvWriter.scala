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

    val quadratIds: List[String] = getQuadratIds(quadrats)
    val quadratCentroids: List[String] = getQuadratCoordinates(quadrats)
    val three_list = List.fill(quadratCentroids.size)("3")
    val two_list = List.fill(quadratCentroids.size)("2")
    val areas3dTransposed = (quadratIds :: quadratCentroids :: three_list :: areas3d).transpose
    val areas2dTransposed = (quadratIds :: quadratCentroids :: two_list :: areas2d).transpose

    // strip the file extension off the mesh names
    val names = files.map(x => x.getName.split('.')(0))

    val writer = CSVWriter.open(new File(file))
    writer.writeRow(List("quadrat size (m)", sizeOfQuadrat))
    writer.writeRow(List("quadrat id", "quadrat centroid", "dimension", names))
    writer.writeAll(areas3dTransposed)
    writer.writeAll(areas2dTransposed)
    writer.close()

  }

  private def getQuadratIds(quadrats: List[Quadrat]): List[String] = {
    val quadratIds: ArrayBuffer[String] = new ArrayBuffer[String]
    for (quadrat <- quadrats) quadratIds += quadrat.id.toString
    quadratIds.toList
  }

  private def getQuadratCoordinates(quadrats: List[Quadrat]): List[String] = {
    val quadratInfo: ArrayBuffer[String] = new ArrayBuffer[String]
    for (quadrat <- quadrats) quadratInfo += getQuadratCentroidAsString(quadrat)
    quadratInfo.toList
  }

  private def getQuadratCentroidAsString(q: Quadrat): String = {
    "(" + q.midpoint.x + "," + q.midpoint.y + ")"
  }
}
