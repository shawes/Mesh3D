package mesh

import java.io.File

import mesh.io.{MeshCsvWriter, MeshReader}
import mesh.shapes._
import org.clapper.argot._

object Driver {

  import ArgotConverters._

  // Setup the command line arguments
  val parser = new ArgotParser("mesh_quadrats", preUsage = Some("Mesh Quadrats, Author: Steven Hawes, Version 1.0"))
  val quadratSize = parser.option[Double](List("quadrat_size"), "n", "The size of a quadrat (metres).")
  val dimensions = parser.option[String](List("dimensions"), "WLH", "The dimensions of the input files (width-length-height)")
  val output = parser.parameter[String]("outputfile", "Output file to which to write (a .csv)", optional = false)
  val input = parser.multiParameter[File]("input", "Input .x3d files to read. If not specified, use stdin.", optional = true) {
    (s, opt) =>
      val file = new File(s)
      if (!file.exists)
        parser.usage("Input file \"" + s + "\" does not exist.")
      file
  }


  def main(args: Array[String]) {
    try {
      parser.parse(args)
      runMesh3D()
    }
    catch {
      case e: ArgotUsageException => println(e.message)
    }
  }

  def runMesh3D(): Unit = {
    val reader = new MeshReader()
    val geometry = new Geometry()
    val files = input.value.toList
    val passes = files.map(x => reader.read(x))
    val meshes = passes.map(x => new Mesh(x, new DimensionOrder(dimensions.value.getOrElse("XYZ"))))
    val boundingBox = geometry.findMaximumBoundingBox(meshes)
    println("Bounding box: " + boundingBox)
    val quadratBuilder = new QuadratBuilder
    val quadrats = quadratBuilder.build(boundingBox, quadratSize.value.get)

    println("There are this many quadrats: " + quadrats.size)
    val areas2d = meshes.map(x => x.getTwoDimensionAreas(quadrats))
    println("Calculated 2D areas")
    val areas3d = meshes.map(x => x.getThreeDimensionAreas(quadrats))
    println("Calculated 3D areas")
    val writer = new MeshCsvWriter()
    writer.write(output.value.get, files, quadrats, quadratSize.value.get, areas3d, areas2d)
  }

  def printArea(mesh: Mesh, polygons: List[Quadrilateral]): Unit = {
    println("--Mesh started--")
    println("Area = " + mesh.getTotalArea(polygons))
    println("--Mesh finished--")
  }

  def getUniqueQuadrats(quadrats: List[Quadrat]): List[Quadrat] = {
    quadrats
  }


}
