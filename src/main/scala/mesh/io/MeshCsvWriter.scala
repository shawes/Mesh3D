package mesh.io

import java.io.File

import com.github.tototoshi.csv.CSVWriter

import scala.collection.AbstractSeq
import scala.collection.mutable.ArrayBuffer

/**
  *
  * Created by Steven Hawes on 7/06/16.
  */
class MeshCsvWriter {
  def write(file: String,
            files: List[File],
            sizeOfQuadrat: Double,
            areas3d: List[AbstractSeq[Any] with java.io.Serializable],
            areas2d: List[AbstractSeq[Any] with java.io.Serializable]): Unit = {

    val quadratInfo: ArrayBuffer[String] = getQuadratCoordinates(sizeOfQuadrat.toInt)
    val csv3dOutput = quadratInfo :: areas3d
    val csv2dOutput = quadratInfo :: areas2d


    val names = files.map(x => x.getName)
    val f = new File(file)
    val writer = CSVWriter.open(f)
    writer.writeRow(List("", "width"))
    writer.writeRow(List("bounding box size", sizeOfQuadrat))
    writer.writeRow(List("quadrat size", sizeOfQuadrat))
    writer.writeRow("")
    writer.writeRow(List("3D areas"))
    writer.writeRow("quadrat" :: names)
    writer.writeAll(csv3dOutput.transpose)
    writer.writeRow("")
    writer.writeRow(List("2D areas"))
    writer.writeRow("quadrat" :: names)
    writer.writeAll(csv2dOutput.transpose)
    writer.close()

  }

  private def getQuadratCoordinates(size: Int): ArrayBuffer[String] = {
    val quadrats = new ArrayBuffer[String]
    for (i <- 0 until size) {
      for (j <- 0 until size) {
        quadrats += ("(" + i + "," + j + ")")
      }
    }
    quadrats
  }
}
