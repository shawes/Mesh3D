
class Edge (val start:Vertex, val end:Vertex) {

  val x_disp = start.x - end.x
  val y_disp = start.y - end.y
  val slope = y_disp / x_disp

}
