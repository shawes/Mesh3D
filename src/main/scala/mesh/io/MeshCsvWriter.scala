package mesh.io

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import mesh.shapes.Quadrat

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.ParSeq

class MeshCsvWriter {
  def write(file: String,
            files: List[File],
            quadrats: Seq[List[Quadrat]],
            sizeOfQuadrat: List[Double],
            areas3d: ParSeq[Seq[List[(Double, Int)]]],
            areas2d: ParSeq[Seq[List[(Double, Int)]]]): Unit = {

    // strip the file extension off the mesh names
    val names = files.map(x => x.getName.split('.')(0))

    val csvFile = new File(file)
    val exists: Boolean = csvFile.isFile
    val writer = CSVWriter.open(new File(file), append = exists)

    // headers
    if (!exists) writer.writeRow(List("mesh_name", "quadrat_size_m", "quadrat_coord_x", "quadrat_coord_y", "quadrat_centroid_x", "quadrat_centroid_y", "faces", "3d_area", "2d_area", "rugosity"))

    val areas3dArray = areas3d.flatten.toArray.flatten
    val areas2dArray = areas2d.flatten.toArray.flatten
    val sizes = sizeOfQuadrat.toArray

    var areaIndex = 0
    var sizeIndex = 0
    names.foreach(name => {
      quadrats.foreach(quadrat => {
        quadrat.foreach(q => {
          val area3d = areas3dArray(areaIndex)._1
          val area2d = areas2dArray(areaIndex)._1
          val faces = areas3dArray(areaIndex)._2
          val rugosity = area3d / area2d
          if (area3d > 0 && area2d > 0) {
            writer.writeRow(List(name, sizes(sizeIndex), q.id._1, q.id._2, q.midpoint.x, q.midpoint.y, faces, area3d, area2d, rugosity))
          }
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
