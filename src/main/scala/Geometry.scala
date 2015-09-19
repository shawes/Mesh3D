
class Geometry {

  def getAngleBetweenEdges2(edge1 : Edge, edge2:Edge) : Double = {

    val angle1 = Math.atan2(edge1.start.y - edge1.end.y,
      edge1.start.x - edge1.end.x)
    val angle2 = Math.atan2(edge2.start.y - edge2.end.y,
      edge2.start.x - edge2.end.x)

    math.abs(angle1-angle2)
  }

  def getAngleBetweenEdges(edge1 : Edge, edge2:Edge) : Double = {
    math.tan((edge1.slope-edge2.slope)/(1-edge1.slope*edge2.slope))
  }
}
