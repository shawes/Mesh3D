import scala.math.atan2

class Geometry {

  def getAngleBetweenEdges2(edge1 : Edge, edge2:Edge) : Double = {

    val angle1 = atan2(edge1.start.y - edge1.end.y,
      edge1.start.x - edge1.end.x)
    val angle2 = atan2(edge2.start.y - edge2.end.y,
      edge2.start.x - edge2.end.x)

    math.abs(angle1-angle2)
  }

  def getAngleBetweenEdges(edge1 : Edge, edge2:Edge) : Double = {
    math.tan((edge1.slope-edge2.slope)/(1-edge1.slope*edge2.slope))
  }

  def findMaximumBoundingBox(meshList: List[Mesh]): Polygon = {

    val maxQuadrilateral = findMaximumBoundingQuad(meshList)
    createRectangle(maxQuadrilateral)
  }

  private def findMaximumBoundingQuad(meshList: List[Mesh]): Polygon = {

    val cornerLeftBottom = new Vertex(meshList.map(f => f.corners._1.x).max,
      meshList.map(f => f.corners._1.y).max,
      meshList.map(f => f.corners._1.z).min)

    val cornerLeftTop = new Vertex(meshList.map(f => f.corners._2.x).max,
      meshList.map(f => f.corners._2.y).max,
      meshList.map(f => f.corners._2.z).min)

    val cornerRightTop = new Vertex(meshList.map(f => f.corners._3.x).min,
      meshList.map(f => f.corners._3.y).min,
      meshList.map(f => f.corners._3.z).min)

    val cornerRightBottom = new Vertex(meshList.map(f => f.corners._4.x).min,
      meshList.map(f => f.corners._4.y).min,
      meshList.map(f => f.corners._4.z).min)

    println("Bounding box is:")
    println("Bottom left corner " + cornerLeftBottom)
    println("Top left corner " + cornerLeftTop)
    println("Top right corner " + cornerRightTop)
    println("Bottom right corner " + cornerRightBottom)

    new Polygon(cornerLeftBottom, cornerLeftTop, cornerRightTop, cornerRightBottom)

  }

  private def createRectangle(quadrilateral: Polygon): Polygon = {

    val leftEdge = new Edge(quadrilateral.a, quadrilateral.b)
    val rightEdge = new Edge(quadrilateral.d, quadrilateral.c)
    val alignedCorner = new Vertex(quadrilateral.a.x + math.abs(rightEdge.x_disp), quadrilateral.a.y + math.abs(rightEdge.y_disp), quadrilateral.a.z)
    new Polygon(quadrilateral.a, alignedCorner, quadrilateral.c, quadrilateral.d)

  }

}
