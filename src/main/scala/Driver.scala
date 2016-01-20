import java.io
import java.io.File

import com.github.tototoshi.csv.CSVWriter

import scala.collection.AbstractSeq
import scala.collection.mutable.ArrayBuffer

object Driver {

  var width, length: Int = 1

  def main(args: Array[String]) {

    val parser = new scopt.OptionParser[ScoptParser]("mesh3d") {
      head("mesh3d", "1.0")
      opt[Unit]('i', "info") action { (_, c) =>
        c.copy(info = true)
      } text "info provides the size of the bounding box, use to ensure axis are correct"
      opt[Int]('w', "width") action { (x, c) =>
        c.copy(width = x)
      } text "The number to subdivide the rectangular mesh's width"
      opt[Int]('l', "length") action { (x, c) =>
        c.copy(length = x)
      } text "The number to subdivide the rectangular mesh's height"
      opt[String]('d', "dim") action { (x, c) =>
        c.copy(dimensions = x)
      } text "Specify the order of the dimensions in the format XYZ (you want the axis coming out of reef to be Z" +
        "\n, e.g. if the axis coming out is X, then the dimensions might be ZYX "
      opt[File]('o', "out") required() valueName "<file.csv>" action { (x, c) =>
        c.copy(out = x)
      } text "out is a required CSV file to write the results to"
      opt[Seq[File]]('f', "files") required() valueName "<file1.x3d>,<file2.x3d>..." action { (x, c) =>
        c.copy(files = x)
      } text "x3d files to compare"
      note("some notes.\n")
    }
    parser.parse(args, ScoptParser()) match {
      case Some(config) =>
        width = config.width
        length = config.length
        runMesh3D(config)
      case None =>
      // arguments are bad, error message will have been displayed
    }


  }

  def runMesh3D(config: ScoptParser): Unit = {

    val reader = new MeshReader()
    val geometry = new Geometry()
    val files = config.files.toList
    val passes = files.map(x => reader.readx3d(x))
    val meshes = passes.map(x => new Mesh(x, new DimensionOrder(config.dimensions)))
    val rectangle = geometry.findMaximumBoundingBox(meshes)

    // If the info flag is set, just print the bounding box to the command line
    if (config.info) {
      println(rectangle.toString)
      System.exit(0) // There has to be a better way to do this
    }

    val widthValue = rectangle.a.distanceXY(rectangle.b)
    val lengthValue = rectangle.a.distanceXY(rectangle.d)
    val rectangleSubDivider = new RectangleSubDivider(widthRatio = config.width,
      lengthRatio = config.length)
    val polygons = rectangleSubDivider.divideRectangle(rectangle)

    // Calculate areas
    val areas2d = meshes.map(x => x.get2DAreas(polygons))
    val areas3d = meshes.map(x => x.getAreas(polygons))

    val quadrats: ArrayBuffer[String] = getQuadratCoordinates
    val csv3dOutput = quadrats :: areas3d
    val csv2dOutput = quadrats :: areas2d

    writeCsvFile(files, (widthValue, lengthValue), csv3dOutput, csv2dOutput, config.out)
  }

  def writeCsvFile(files: List[File], values: (Double, Double),
                   csv3dOutput: List[AbstractSeq[Any] with io.Serializable],
                   csv2dOutput: List[AbstractSeq[Any] with io.Serializable],
                   file: File): Unit = {
    val names = files.map(x => x.getName)
    val writer = CSVWriter.open(file)
    writer.writeRow(List("", "width", "length"))
    writer.writeRow(List("bounding box size", values._1, values._2))
    writer.writeRow(List("quadrat size", values._1 / width, values._2 / length))
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
    for (i <- 0 until width) {
      for (j <- 0 until length) {
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
