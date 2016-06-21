package mesh.io

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import mesh.shapes.Quadrat

import scala.collection.mutable.ArrayBuffer

/**
  *
  * Created by Steven Hawes on 7/06/16.
  */
class MeshCsvWriter {
  def write(file: String,
            files: List[File],
            quadrats: Seq[List[Quadrat]],
            sizeOfQuadrat: List[Double],
            areas3d: Seq[Seq[List[Double]]],
            areas2d: Seq[Seq[List[Double]]]): Unit = {

    // strip the file extension off the mesh names
    val names = files.map(x => x.getName.split('.')(0))

    val csvFile = new File(file)
    val exists: Boolean = csvFile.isFile
    val writer = CSVWriter.open(new File(file), append = exists)

    // headers
    if (!exists) writer.writeRow(List("mesh_name", "quadrat_size", "quadrat_id", "quadrat_centroid", "area_dimension", "area_value"))

    val areas3dArray = areas3d.flatten.toArray.flatten
    val areas2dArray = areas2d.flatten.toArray.flatten
    val sizes = sizeOfQuadrat.toArray

    var areaIndex = 0
    var sizeIndex = 0
    names.foreach(name => {
      quadrats.foreach(quadrat => {
        quadrat.foreach(q => {
          writer.writeRow(List(name, sizes(sizeIndex), q.id, getQuadratCentroidAsString(q), "3D", areas3dArray(areaIndex)))
          writer.writeRow(List(name, sizes(sizeIndex), q.id, getQuadratCentroidAsString(q), "2D", areas2dArray(areaIndex)))
          areaIndex = areaIndex + 1
        })
        sizeIndex = sizeIndex + 1
      })
      sizeIndex = 0
    })
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
