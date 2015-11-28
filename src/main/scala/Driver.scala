import java.io.File

import com.github.tototoshi.csv.CSVWriter
import org.clapper.argot._

import scala.collection.mutable.ArrayBuffer

object Driver {

  import ArgotConverters._

  // Setup the command line arguments
  val parser = new ArgotParser("mesh_quadrats", preUsage = Some("Mesh Quadrats, Author: Steven Hawes, Version 1.0"))
  val width = parser.option[Int](List("width"), "n", "The number to subdivide the rectangular mesh's width.")
  val length = parser.option[Int](List("length"), "n", "The number to subdivide the rectangular mesh's length.")
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

    val meshes = passes.map(x => new Mesh(x))

    val rectangle = geometry.findMaximumBoundingBox(meshes)

    val widthValue = rectangle.a.distanceXY(rectangle.b)
    val lengthValue = rectangle.a.distanceXY(rectangle.d)

    val rectangleSubDivider = new RectangleSubDivider(widthRatio = width.value.getOrElse(1),
      lengthRatio = length.value.getOrElse(1))

    val polygons = rectangleSubDivider.divideRectangle(rectangle)


    // Calculate areas
    val areas2d = meshes.map(x => x.get2DAreas(polygons))
    val areas3d = meshes.map(x => x.getAreas(polygons))


    val quadrats: ArrayBuffer[String] = getQuadratCoordinates
    val csv3dOutput = quadrats :: areas3d
    val csv2dOutput = quadrats :: areas2d

    val names = files.map(x => x.getName)
    val f = new File(output.value.getOrElse(" "))
    val writer = CSVWriter.open(f)
    writer.writeRow(List("", "width", "length"))
    writer.writeRow(List("bounding box size", widthValue, lengthValue))
    writer.writeRow(List("quadrat size", widthValue / width.value.getOrElse(1), lengthValue / length.value.getOrElse(1)))
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

  def getQuadratCoordinates: ArrayBuffer[String] = {
    val quadrats = new ArrayBuffer[String]
    for (i <- 0 to width.value.getOrElse(1) - 1) {
      for (j <- 0 to length.value.getOrElse(1) - 1) {
        quadrats += ("(" + i + "," + j + ")")
      }
    }
    quadrats
  }

  def printArea(mesh: Mesh, polygons: List[Polygon]): Unit = {
    println("--Mesh started--")
    println("Area = " + mesh.getTotalArea(polygons))
    println("--Mesh finished--")
  }


}
