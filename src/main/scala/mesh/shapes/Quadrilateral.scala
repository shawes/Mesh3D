package mesh.shapes

import scala.Double._
import scala.math._

class Quadrilateral(val a: Vertex, val b: Vertex, val c: Vertex, val d: Vertex) {

  val vertices = List(a, b, c, d)
  val edges = List(new Line(a, b), new Line(b, c), new Line(c, d), new Line(d, a))

  // Gets the centroid of a quadrilateral by calculating the midpoints of the diagonals and
  // then the centroid is the midpoint of the line between these two midpoints
  def centroid = new Line(new Line(a, c).midpoint, new Line(b, d).midpoint).midpoint

  
  def contains(vertex: Vertex): Boolean = {
    rayCasting(vertex)
    // inBoundingBox(vertex)
  }

  override def toString = vertices.toString

  private def rayCasting(point: Vertex) = edges.count(raySegI(point, _)) % 2 != 0

  private def raySegI(point: Vertex, edge: Line): Boolean = {
    val epsilon = 0.00001
    if (edge.start.y > edge.end.y)
      return raySegI(point, new Line(edge.end, edge.start))
    if (point.y == edge.start.y || point.y == edge.end.y)
      return raySegI(new Vertex(point.x, point.y + epsilon, 0), edge)
    if (point.y > edge.end.y || point.y < edge.start.y || point.x > max(edge.start.x, edge.end.x))
      return false
    if (point.x < min(edge.start.x, edge.end.x))
      return true
    val left = if (abs(edge.start.x - point.x) > MinValue) (point.y - edge.start.y) / (point.x - edge.start.x) else MaxValue
    val right = if (abs(edge.start.x - edge.end.x) > MinValue) (edge.end.y - edge.start.y) / (edge.end.x - edge.start.x) else MaxValue
    left >= right
  }

  private def inBoundingBox(ver: Vertex): Boolean = {
    (ver.x > a.x && ver.x < d.x) && (ver.y > c.y && ver.y < b.y)
  }

}
