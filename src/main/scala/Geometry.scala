
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

  def findMaximumBoundingBox(mesh1: Mesh, mesh2: Mesh, mesh3:Mesh): Polygon = {

    //find max min x
    val leftTop = new Vertex(List(mesh1.corners._1.x, mesh2.corners._1.x, mesh3.corners._1.x).max,
      List(mesh1.corners._1.y, mesh2.corners._1.y, mesh3.corners._1.y).max,
      List(mesh1.corners._1.z, mesh2.corners._1.z, mesh3.corners._1.z).max)

    val rightTop = new Vertex(List(mesh1.corners._2.x, mesh2.corners._2.x, mesh3.corners._2.x).max,
      List(mesh1.corners._2.y, mesh2.corners._2.y, mesh3.corners._2.y).max,
      List(mesh1.corners._2.z, mesh2.corners._2.z, mesh3.corners._2.z).max)

    val rightBottom = new Vertex(List(mesh1.corners._3.x, mesh2.corners._3.x, mesh3.corners._3.x).min,
      List(mesh1.corners._3.y, mesh2.corners._3.y, mesh3.corners._3.y).min,
      List(mesh1.corners._3.z, mesh2.corners._3.z, mesh3.corners._3.z).min)

    val leftBottom = new Vertex(List(mesh1.corners._4.x, mesh2.corners._4.x, mesh3.corners._4.x).min,
      List(mesh1.corners._4.y, mesh2.corners._4.y, mesh3.corners._4.y).min,
      List(mesh1.corners._4.z, mesh2.corners._4.z, mesh3.corners._4.z).min)


    new Polygon(leftTop, rightTop, rightBottom, leftBottom)

  }
//  def findMaximumBoundingBox2(meshList : List[Mesh]): Polygon = {
//
//
//    //find max min x
//    val leftTop = new Vertex(meshList.map(f=> f.corners._1.x.max,
//      List(mesh1.corners._1.y, mesh2.corners._1.y, mesh3.corners._1.y).max,
//      List(mesh1.corners._1.z, mesh2.corners._1.z, mesh3.corners._1.z).max)
//
//    val rightTop = new Vertex(List(mesh1.corners._2.x, mesh2.corners._2.x, mesh3.corners._2.x).max,
//      List(mesh1.corners._2.y, mesh2.corners._2.y, mesh3.corners._2.y).max,
//      List(mesh1.corners._2.z, mesh2.corners._2.z, mesh3.corners._2.z).max)
//
//    val rightBottom = new Vertex(List(mesh1.corners._3.x, mesh2.corners._3.x, mesh3.corners._3.x).min,
//      List(mesh1.corners._3.y, mesh2.corners._3.y, mesh3.corners._3.y).min,
//      List(mesh1.corners._3.z, mesh2.corners._3.z, mesh3.corners._3.z).min)
//
//    val leftBottom = new Vertex(List(mesh1.corners._4.x, mesh2.corners._4.x, mesh3.corners._4.x).min,
//      List(mesh1.corners._4.y, mesh2.corners._4.y, mesh3.corners._4.y).min,
//      List(mesh1.corners._4.z, mesh2.corners._4.z, mesh3.corners._4.z).min)
//
//
//    new Polygon(leftTop, rightTop, rightBottom, leftBottom)
//
//  }
}
