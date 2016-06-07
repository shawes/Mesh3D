package mesh.shapes

class Line(val start: Vertex, val end: Vertex) {

  val x_displacement = start.x - end.x
  val y_displacement = start.y - end.y
  val slope = y_displacement / x_displacement
  val midpoint = new Vertex((start.x + end.x) / 2, (start.y + end.y) / 2, (start.z + end.z) / 2)

}
