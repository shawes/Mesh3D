
import scala.Double._
import scala.math._

class Polygon(val a: Vertex, val b: Vertex, val c: Vertex, val d: Vertex) {

  val points = List(a, b, c, d)
  val edges = List((a, b), (b, c), (c, d), (d, a))
  
  def contains(vertex: Vertex): Boolean = {
    rayCasting(vertex)
  }

  override def toString = points.toString()

  private def rayCasting(p: Vertex) = edges.count(raySegI(p, _)) % 2 != 0

  private def raySegI(p: Vertex, e: (Vertex, Vertex)): Boolean = {
    val epsilon = 0.00001
    if (e._1.y > e._2.y)
      return raySegI(p, (e._2, e._1))
    if (p.y == e._1.y || p.y == e._2.y)
      return raySegI(new Vertex(p.x, p.y + epsilon,0), e)
    if (p.y > e._2.y || p.y < e._1.y || p.x > max(e._1.x, e._2.x))
      return false
    if (p.x < min(e._1.x, e._2.x))
      return true
    val left = if (abs(e._1.x - p.x) > MinValue) (p.y - e._1.y) / (p.x - e._1.x) else MaxValue
    val right = if (abs(e._1.x - e._2.x) > MinValue) (e._2.y - e._1.y) / (e._2.x - e._1.x) else MaxValue
    left >= right
  }

  private def inBoundingBox(ver: Vertex): Boolean = {
    (ver.x > a.x && ver.x < d.x) && (ver.y > c.y && ver.y < b.y)
  }

}
