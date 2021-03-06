package mesh.quadrats

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
    val distanceToEdgeAD = centroid.distanceToXYZ(new Line(box.d, box.a).midpoint)
    val distanceToEdgeCD = centroid.distanceToXYZ(new Line(box.c, box.d).midpoint)

    for (i <- 0 until (distanceToEdgeCD / size).toInt + 1) {
      for (j <- 0 until (distanceToEdgeAD / size).toInt + 1) {
        if (i == 0 && j == 0) quadrats += new Quadrat((0, 0), size, centroid)
        else {
          val fourNewQuadrats =
            List(new Quadrat((i, j), size, new Vertex(centroid.x + i * size, centroid.y + (j * size), centroid.z)),
              new Quadrat((i, j * (-1)), size, new Vertex(centroid.x + (i * size), centroid.y - (j * size), centroid.z)),
              new Quadrat((i * (-1), j), size, new Vertex(centroid.x - (i * size), centroid.y + (j * size), centroid.z)),
              new Quadrat((i * (-1), j * (-1)), size, new Vertex(centroid.x - (i * size), centroid.y - (j * size), centroid.z)))

          val quadratsCentroidInsideBoundingBox = fourNewQuadrats.filter(q => box.contains(q.midpoint))
          quadrats ++= quadratsCentroidInsideBoundingBox
        }
      }
    }
    quadrats.distinct.toList
  }

}
