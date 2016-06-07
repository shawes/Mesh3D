package mesh

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import mesh.io.MeshReader
import mesh.shapes._
import org.clapper.argot._

import scala.collection.AbstractSeq
import scala.collection.mutable.ArrayBuffer

object Driver {

  import ArgotConverters._

  // Setup the command line arguments
  val parser = new ArgotParser("mesh_quadrats", preUsage = Some("Mesh Quadrats, Author: Steven Hawes, Version 1.0"))
  val quadratSize = parser.option[Int](List("quadrat_size"), "n", "The size of a quadrat (metres).")
  val dimensions = parser.option[String](List("dimensions"), "WLH", "The dimensions of the input files")
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
    val quadratBuilder = new QuadratBuilder
    val quadrats = quadratBuilder.build(boundingBox, quadratSize.value.get)
    val areas2d = meshes.map(x => x.get2DAreas(quadrats))
    val areas3d = meshes.map(x => x.getAreas(quadrats))

    val quadratInfo: ArrayBuffer[String] = getQuadratCoordinates
    val csv3dOutput = quadratInfo :: areas3d
    val csv2dOutput = quadratInfo :: areas2d

    writeCsvFile(files, quadratSize.value.get, csv3dOutput, csv2dOutput)
  }

  def writeCsvFile(files: List[File], sizeOfQuadrat: Double, csv3dOutput: List[AbstractSeq[Any] with java.io.Serializable], csv2dOutput: List[AbstractSeq[Any] with java.io.Serializable]): Unit = {
    val names = files.map(x => x.getName)
    val f = new File(output.value.getOrElse(" "))
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



  def getQuadratCoordinates: ArrayBuffer[String] = {
    val quadrats = new ArrayBuffer[String]
    for (i <- 0 until quadratSize.value.getOrElse(1)) {
      for (j <- 0 until quadratSize.value.getOrElse(1)) {
        quadrats += ("(" + i + "," + j + ")")
      }
    }
    quadrats
  }

  def printArea(mesh: Mesh, polygons: List[Quadrilateral]): Unit = {
    println("--Mesh started--")
    println("Area = " + mesh.getTotalArea(polygons))
    println("--Mesh finished--")
  }


}