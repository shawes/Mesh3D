package mesh

import java.io.File
import java.util.Calendar

import mesh.io.{MeshCsvWriter, MeshReader}
import mesh.quadrats.QuadratBuilder
import mesh.shapes._

object Driver {

  val parser = new scopt.OptionParser[Config]("mesh_quadrats") {
    head("mesh_quadrats", "1.0")

    opt[String]('d', "dim").optional().action((x, c) =>
      c.copy(dim = x)).text("the dimensions of the input files WLH (width-length-height)")

    opt[Seq[Double]]('s', "size").required().action((x, c) =>
      c.copy(size = x)).text("the size of a quadrat (standard is metres, but depends on the mesh units)")

    opt[Unit]("verbose").action((_, c) =>
      c.copy(verbose = true)).text("verbose is a flag")

    opt[Unit]("append").action((_, c) =>
      c.copy(verbose = false)).text("append is a flag")

    opt[String]('o', "out").required().action((x, c) =>
      c.copy(out = x)).text("output file to which to write (it has to be a .csv file)")

    arg[File]("<file>...").unbounded().required().action((x, c) =>
      c.copy(files = c.files :+ x)).text("input .x3d files to calcuate quadrats for")
  }


  def main(args: Array[String]) {
    parser.parse(args, Config()) match {
      case Some(config) =>
        val startTime = Calendar.getInstance.getTimeInMillis
        runMesh3D(config)
        val finishTime = Calendar.getInstance.getTimeInMillis
        println("Completed mesh quadrats in " + (finishTime - startTime) / 1000 + " seconds")
      case None =>
    }
  }

  def runMesh3D(config: Config): Unit = {
    val reader = new MeshReader(config.verbose, new DimensionOrder(config.dim))
    val geometry = new Geometry()
    val files = config.files.par

    val meshes = files.map(x => reader.read(x))
    if (config.verbose) println("Finished reading in the mesh files")

    //val meshes = passes.map(x => new Mesh(x))
    //if (config.verbose) println("Finished constructing the vertices and faces")

    val boundingBox = geometry.findMaximumBoundingBox(meshes)
    if (config.verbose) {
      println("Finished constructing the bounding box:")
      println("A: " + boundingBox.a)
      println("B: " + boundingBox.b)
      println("C: " + boundingBox.c)
      println("D: " + boundingBox.d)
    }

    val quadratBuilder = new QuadratBuilder()
    val quadrats = config.size.map(size => quadratBuilder.build(boundingBox, size))
    if (config.verbose) {
      print("Finished generating quadrats of sizes " + config.size.mkString(",") + " of which there were ")
      quadrats.foreach(quadrat => if (quadrats.head == quadrat) print(quadrat.size) else print("," + quadrat.size))
      print(" respectively.\n")
    }

    val areas = meshes.map(x => x.getArea(quadrats))
    if (config.verbose) println("Finished calculating the 2D and 3D areas of the quadrats")

    val writer = new MeshCsvWriter()
    writer.write(config.out, files.toList, quadrats, config.size.toList, areas, config.append)
    if (config.verbose) println("Finished writing to " + config.out)
  }


}
