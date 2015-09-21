import scala.collection.mutable.ArrayBuffer

class RectangleSubDivider() {

  private var widthRatio, lengthRatio = 1

  def setRatios(width: Int, length: Int): Unit = {
    widthRatio = width
    lengthRatio = length
  }

  def divideRectangle(rectangle: Polygon): List[Polygon] = {

    val verticesMatrix = Array.ofDim[Vertex](widthRatio + 1, lengthRatio + 1)

    addCorners(verticesMatrix, rectangle)
    calculateSides(verticesMatrix)
    calculateTops(verticesMatrix)
    calculateInsidePoints(verticesMatrix)
    calculateQuadrats(verticesMatrix)

  }

  private def addCorners(vertices: Array[Array[Vertex]], rectangle: Polygon): Unit = {
    vertices(0)(0) = rectangle.leftTop
    vertices(widthRatio)(0) = rectangle.rightTop
    vertices(widthRatio)(lengthRatio) = rectangle.rightBottom
    vertices(0)(lengthRatio) = rectangle.leftBottom
  }

  private def calculateSides(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 to widthRatio - 1) {
      vertices(i)(0) = calculateRatioPoint(vertices(0)(0), vertices(widthRatio)(0), widthRatio - i, i)
      vertices(i)(lengthRatio) = calculateRatioPoint(vertices(0)(lengthRatio), vertices(widthRatio)(lengthRatio), widthRatio - i, i)
    }
  }

  private def calculateTops(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 to lengthRatio - 1) {
      vertices(0)(i) = calculateRatioPoint(vertices(0)(0), vertices(0)(lengthRatio), lengthRatio - i, i)
      vertices(widthRatio)(i) = calculateRatioPoint(vertices(widthRatio)(0), vertices(widthRatio)(lengthRatio), lengthRatio - i, i)
    }
  }

  private def calculateRatioPoint(start: Vertex, end: Vertex, a: Int, b: Int): Vertex = {
    new Vertex((a * start.x + b * end.x) / (a + b), (a * start.y + b * end.y) / (a + b), 0)
  }

  private def calculateInsidePoints(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 to lengthRatio - 1) {
      for (j <- 1 to widthRatio - 1) {
        val vertex = calculateRatioPoint(vertices(0)(i), vertices(widthRatio)(i), widthRatio - j, j)
        vertices(j)(i) = vertex
      }
    }
  }

  private def calculateQuadrats(vertices: Array[Array[Vertex]]): List[Polygon] = {
    val polygons: ArrayBuffer[Polygon] = new ArrayBuffer[Polygon]()
    for (i <- 0 to widthRatio) {
      for (j <- 0 to lengthRatio) {
        if (i + 1 <= widthRatio && j + 1 <= lengthRatio) {
          polygons.append(new Polygon(vertices(i)(j), vertices(i + 1)(j), vertices(i + 1)(j + 1), vertices(i)(j + 1)))
        }
      }
    }
    polygons.toList
  }
}
