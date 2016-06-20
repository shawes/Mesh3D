package mesh

import java.io.File

import mesh.io.{MeshCsvWriter, MeshReader}
import mesh.shapes._

object Driver {

  // Setup the command line arguments
  /*  val parser = new ArgotParser("mesh_quadrats", preUsage = Some("Mesh Quadrats, Author: Steven Hawes, Version 1.0"))
    val quadratSize = parser.option[Double](List("quadrat_size"), "n", "The size of a quadrat (metres).")
    val dimensions = parser.option[String](List("dimensions"), "WLH", "The dimensions of the input files (width-length-height)")
    val output = parser.parameter[String]("outputfile", "Output file to which to write (a .csv)", optional = false)
    val input = parser.multiParameter[File]("input", "Input .x3d files to read. If not specified, use stdin.", optional = true) {
      (s, opt) =>
        val file = new File(s)
        if (!file.exists)
          parser.usage("Input file \"" + s + "\" does not exist.")
        file
    }*/

  val parser = new scopt.OptionParser[Config]("mesh_quadrats") {
    head("mesh_quadrats", "1.0")

    opt[String]('d', "dim").required().action((x, c) =>
      c.copy(dim = x)).text("the dimensions of the input files WLH (width-length-height)")

    opt[Double]('s', "size").required().action((x, c) =>
      c.copy(size = x)).text("the size of a quadrat (standard is metres, but depends on the mesh units)")

    opt[Unit]("verbose").action((_, c) =>
      c.copy(verbose = true)).text("verbose is a flag")

    opt[String]('o', "out").required().action((x, c) =>
      c.copy(out = x)).text("output file to which to write (it has to be a .csv file)")

    arg[File]("<file>...").unbounded().required().action((x, c) =>
      c.copy(files = c.files :+ x)).text("input .x3d files to calcuate quadrats for")
  }


  def main(args: Array[String]) {
    parser.parse(args, Config()) match {
      case Some(config) =>
        runMesh3D(config)

      case None =>
      // arguments are bad, error message will have been displayed
    }
  }

  def runMesh3D(config: Config): Unit = {
    val reader = new MeshReader()
    val geometry = new Geometry()
    val files = config.files
    val passes = files.map(x => reader.readPull(x))
    if (config.verbose) println("Finished reading in the mesh files")

    val meshes = passes.map(x => new Mesh(x, new DimensionOrder(config.dim)))
    if (config.verbose) println("Finished constructing the vertices and faces")

    val boundingBox = geometry.findMaximumBoundingBox(meshes)
    if (config.verbose) println("Finished constructing the bounding box, with dimensions " + boundingBox)
    val quadratBuilder = new QuadratBuilder
    val quadrats = quadratBuilder.build(boundingBox, config.size)
    if (config.verbose) println("Finished generating " + quadrats.size + " quadrats of size " + config.size _ + "m")
    val areas2d = meshes.map(x => x.getTwoDimensionAreas(quadrats))
    println("Calculated 2D areas")
    val areas3d = meshes.map(x => x.getThreeDimensionAreas(quadrats))
    println("Calculated 3D areas")
    val writer = new MeshCsvWriter()
    writer.write(config.out, files.toList, quadrats, config.size, areas3d.toList, areas2d.toList)
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
