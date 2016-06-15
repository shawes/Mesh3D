package mesh

import mesh.shapes.{Line, Quadrat, Quadrilateral, Vertex}

import scala.collection.mutable.ListBuffer

/** A class that builds quadrats on a mesh, starting from the centre position
  *
  */
class QuadratBuilder {

  /**
    *
    * @param box  the bounding box of the meshes to measure
    * @param size the size of the square quadrilateral (in the units of the provided mesh)
    * @return list of the quadrats that fit the bounding box
    */
  def build(box: Quadrilateral, size: Double): List[Quadrat] = {

    val centroid = box.centroid
    val quadrats = new ListBuffer[Quadrat]()
    quadrats += new Quadrat(size, centroid)

    println("centroid is " + centroid)

    val distanceToRightEdge = centroid.distanceTo(new Line(box.b, box.c).midpoint)
    val distanceToBottomEdge = centroid.distanceTo(new Line(box.c, box.d).midpoint)

    println("dist right=" + distanceToRightEdge + ", dist down =" + distanceToBottomEdge)

    for (i <- 0 until (distanceToRightEdge / size).toInt) {
      for (j <- 0 until (distanceToBottomEdge / size).toInt) {
        println("iteration (" + i + "," + j + ")")
        val points = List(new Vertex(centroid.x + i * size, centroid.y + (j * size), centroid.z),
          new Vertex(centroid.x + (i * size), centroid.y - (j * size), centroid.z),
          new Vertex(centroid.x - (i * size), centroid.y + (j * size), centroid.z),
          new Vertex(centroid.x - (i * size), centroid.y - (j * size), centroid.z))

        val validPoints = points.filter(p => box.contains(p))
        for (p <- validPoints) {
          println("Adding quadrat " + p)
          quadrats += new Quadrat(size, p)
        }
      }
    }
    quadrats.toList.distinct
  }
}
