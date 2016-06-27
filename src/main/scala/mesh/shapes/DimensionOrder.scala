package mesh.shapes

class DimensionOrder(val order: String) {
  val X = 0
  val Y = 1
  val Z = 2

  def getFirst: Int = getValue(order.charAt(0))

  private def getValue(c: Char): Int = {
    if (c == 'X') X
    else if (c == 'Y') Y
    else Z
  }

  def getSecond: Int = getValue(order.charAt(1))

  def getVertex(x: Double, y: Double, z: Double): Vertex = {
    getThird match {
      case 0 => new Vertex(z, y, x)
      case 1 => new Vertex(x, z, y)
      case _ => new Vertex(x, y, z)
    }
  }

  def getThird: Int = getValue(order.charAt(2))

}
