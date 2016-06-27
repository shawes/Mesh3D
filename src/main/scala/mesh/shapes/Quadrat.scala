package mesh.shapes

/**
  *
  * @param id
  * @param size
  * @param midpoint
  */
class Quadrat(val id: (Int, Int), val size: Double, val midpoint: Vertex) extends Quadrilateral(
  new Vertex(midpoint.x - size / 2, midpoint.y - size / 2, midpoint.z),
  new Vertex(midpoint.x - size / 2, midpoint.y + size / 2, midpoint.z),
  new Vertex(midpoint.x + size / 2, midpoint.y - size / 2, midpoint.z),
  new Vertex(midpoint.x + size / 2, midpoint.y + size / 2, midpoint.z)) {

  var relativeZAvg = 0.0
  var relativeZStd = 0.0

  override def contains(vertex: Vertex): Boolean = (vertex.x > a.x && vertex.x < d.x) && (vertex.y > a.y && vertex.y < b.y)

  override def equals(that: Any): Boolean = that match {
    case that: Quadrat => that.midpoint.equals(this.midpoint) && (that.id._1 == this.id._1 && that.id._2 == this.id._2) && that.size == this.size
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + id.hashCode
    result = prime * result + size.hashCode
    result = prime * result + (if (midpoint == null) 0 else midpoint.hashCode)
    result
  }
}
