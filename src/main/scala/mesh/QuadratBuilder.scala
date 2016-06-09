package mesh

import mesh.shapes.{Line, Quadrat, Quadrilateral, Vertex}

import scala.collection.mutable.ArrayBuffer

/**
  *
  * Created by Steven Hawes on 7/06/16.
  */
class QuadratBuilder {

  def build(box: Quadrilateral, size: Double): List[Quadrilateral] = {

    val centroid = box.centroid
    val quadrats = new ArrayBuffer[Quadrilateral]()
    quadrats += new Quadrat(size, centroid)

    val maxRight = centroid.distanceTo(new Line(box.b, box.c).midpoint)
    val maxDown = centroid.distanceTo(new Line(box.c, box.d).midpoint)

    for (i <- 0 until (maxRight / size).toInt) {
      for (j <- 0 until (maxDown / size).toInt) {
        val points = List(new Vertex(centroid.x + i * size, centroid.y + j * size, centroid.z),
          new Vertex(centroid.x + i * size, centroid.y - j * size, centroid.z),
          new Vertex(centroid.x - i * size, centroid.y + j * size, centroid.z),
          new Vertex(centroid.x - i * size, centroid.y - j * size, centroid.z))
        for (p <- points) if (box.contains(p)) quadrats += new Quadrat(size, p)
      }
    }
    quadrats.toList.distinct
  }
}
