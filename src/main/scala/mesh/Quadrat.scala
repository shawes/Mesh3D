package mesh

/**
  *
  * Created by Steven Hawes on 7/06/16.
  */
class Quadrat(size: Double, midpoint: Vertex) extends Quadrilateral(
  new Vertex(midpoint.x - size / 2, midpoint.y - size / 2, midpoint.z),
  new Vertex(midpoint.x - size / 2, midpoint.y + size / 2, midpoint.z),
  new Vertex(midpoint.x + size / 2, midpoint.y - size / 2, midpoint.z),
  new Vertex(midpoint.x + size / 2, midpoint.y + size / 2, midpoint.z)) {


}
