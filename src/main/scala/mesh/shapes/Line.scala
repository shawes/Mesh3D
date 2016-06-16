package mesh.shapes

class Line(val start: Vertex, val end: Vertex) {

  val x_displacement: Double = end.x - start.x
  val y_displacement: Double = end.y - start.y
  val slope: Double = y_displacement / x_displacement
  val midpoint = new Vertex((start.x + end.x) / 2, (start.y + end.y) / 2, (start.z + end.z) / 2)
  val length = math.sqrt(math.pow(x_displacement, 2) + math.pow(y_displacement, 2))

}
