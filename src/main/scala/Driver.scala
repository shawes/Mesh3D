import java.io.File
import com.github.tototoshi.csv.CSVWriter
import org.clapper.argot._

object Driver {

  import ArgotConverters._

  // Setup the command line arguments
  val parser = new ArgotParser("mesh3d", preUsage = Some("Version 1.0"))
  val width = parser.option[Int](List("width"), "width ratio", "the ratio of the rectangles width")
  val length = parser.option[Int](List("l", "length"), "length ratio", "the ratio of the rectangles length")
  val dim = parser.option[Int](List("d", "dimension"), "dimension", "the ratio of the rectangles length")
  val output = parser.parameter[String]("outputfile", "Output file to which to write.", optional = false)
  val input = parser.multiParameter[File]("input", "Input files to read. If not specified, use stdin.", optional = true) {
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

    val is3DArea = dim.value.getOrElse(0) == 3
    val meshs = passes.map(x => new Mesh(x, is3DArea))



    val rectangle = geometry.findMaximumBoundingBox(meshs)

    println("Width distance: " + rectangle.a.distanceXY(rectangle.b) + " and " + rectangle.d.distanceXY(rectangle.c))
    println("Length distance: " + rectangle.a.distanceXY(rectangle.d) + " and " + rectangle.b.distanceXY(rectangle.c))

    val rectangleSubDivider = new RectangleSubDivider(widthRatio = width.value.getOrElse(0),
      lengthRatio = length.value.getOrElse(0))

    val polygons = rectangleSubDivider.divideRectangle(rectangle)

    val areas = meshs.map(x => x.getAreas(polygons))

    //val quadrats = range(List(0, 0), List(width.value.getOrElse(0), length.value.getOrElse(0)))
    //println(quadrats)

    //val csvOutput = quadrats ++ areas

    val f = new File(output.value.getOrElse(" "))
    val writer = CSVWriter.open(f)
    writer.writeRow(files.map(x => x.getName))
    writer.writeAll(areas.transpose)
    writer.close()

    //println("There are this many polygons " + polygons.size)

  }

  def printArea(mesh: Mesh, polygons: List[Polygon]): Unit = {
    println("--Mesh started--")
    println("Area = " + mesh.getTotalArea(polygons))
    println("--Mesh finished--")
  }


}
