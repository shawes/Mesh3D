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
            areas: ParSeq[Seq[List[(Double, Double, Int, Int)]]],
            append: Boolean): Unit = {

    // strip the file extension off the mesh names
    val names = files.map(x => x.getName.split('.')(0))

    val csvFile = new File(file)
    val appendFile = append && csvFile.isFile
    val writer = CSVWriter.open(new File(file), append = appendFile)

    // headers
    if (!appendFile) writer.writeRow(List("mesh_name", "quadrat_size_m", "quadrat_rel_x", "quadrat_rel_y", "quadrat_rel_z_avg", "quadrat_rel_z_stddev", "quadrat_abs_x", "quadrat_abs_y", "quadrat_abs_z", "num_faces", "num_vertices", "3d_surface_area", "2d_surface_area", "surface_rugosity"))

    val areasArray = areas.flatten.toArray.flatten

    val sizes = sizeOfQuadrat.toArray

    var areaIndex = 0
    var sizeIndex = 0
    names.foreach(name => {
      quadrats.foreach(quadrat => {
        quadrat.foreach(q => {
          val area3d = areasArray(areaIndex)._1
          val area2d = areasArray(areaIndex)._2
          val faces = areasArray(areaIndex)._3
          val vertices = areasArray(areaIndex)._4
          val rugosity = area3d / area2d
          if (area3d > 0 && area2d > 0) {
            writer.writeRow(List(name, sizes(sizeIndex), q.id._1, q.id._2, q.relativeZAvg, q.relativeZStd, q.midpoint.x, q.midpoint.y, q.midpoint.z, faces, vertices, area3d, area2d, rugosity))
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
