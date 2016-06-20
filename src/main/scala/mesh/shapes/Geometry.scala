package mesh.shapes

import mesh.Mesh

import scala.math.atan2

class Geometry {

  def getAngleBetweenEdges2(edge1: Line, edge2: Line): Double = {

    val angle1 = atan2(edge1.start.y - edge1.end.y,
      edge1.start.x - edge1.end.x)
    val angle2 = atan2(edge2.start.y - edge2.end.y,
      edge2.start.x - edge2.end.x)

    math.abs(angle1-angle2)
  }

  def getAngleBetweenEdges(edge1: Line, edge2: Line): Double = {
    math.tan((edge1.slope-edge2.slope)/(1-edge1.slope*edge2.slope))
  }

  def getNewPoint(oldPoint: Vertex, slope: Double, distance: Double): Vertex = {
    val r = math.sqrt(1 + math.pow(slope, 2))
    new Vertex(oldPoint.x + distance / r, oldPoint.y + (distance * slope) / r, oldPoint.z)
  }

  def findMaximumBoundingBox(meshList: List[Mesh]): Quadrilateral = {

    val maxQuadrilateral = findMinimumBoundingBox(meshList)
    //createRectangle(maxQuadrilateral)
    maxQuadrilateral
  }

  private def findMinimumBoundingBox(meshList: List[Mesh]): Quadrilateral = {

    val cornerLeftBottom = new Vertex(meshList.map(f => f.extremes._3).min,
      meshList.map(f => f.extremes._4).min,
      0)

    val cornerLeftTop = new Vertex(meshList.map(f => f.extremes._3).min,
      meshList.map(f => f.extremes._2).max,
      0)

    val cornerRightTop = new Vertex(meshList.map(f => f.extremes._1).max,
      meshList.map(f => f.extremes._2).max,
      0)

    val cornerRightBottom = new Vertex(meshList.map(f => f.extremes._1).max,
      meshList.map(f => f.extremes._4).min,
      0)

    new Quadrilateral(cornerLeftBottom, cornerLeftTop, cornerRightTop, cornerRightBottom)

  }

  private def createRectangle(quadrilateral: Quadrilateral): Quadrilateral = {

    val leftEdge = new Line(quadrilateral.a, quadrilateral.b)
    val rightEdge = new Line(quadrilateral.d, quadrilateral.c)
    val alignedCorner = new Vertex(quadrilateral.a.x + math.abs(rightEdge.x_displacement), quadrilateral.a.y + math.abs(rightEdge.y_displacement), quadrilateral.a.z)
    new Quadrilateral(quadrilateral.a, alignedCorner, quadrilateral.c, quadrilateral.d)

  }

}
