package mesh.quadrats

import mesh.shapes.{Quadrilateral, Vertex}

import scala.collection.mutable.ArrayBuffer

class RectangleSubDivider(val widthRatio: Int, val lengthRatio: Int) {


  def divideRectangle(rectangle: Quadrilateral): List[Quadrilateral] = {

    val verticesMatrix = Array.ofDim[Vertex](widthRatio + 1, lengthRatio + 1)

    addCorners(verticesMatrix, rectangle)
    calculateSides(verticesMatrix)
    calculateTops(verticesMatrix)
    calculateInsidePoints(verticesMatrix)
    calculateQuadrats(verticesMatrix)

  }

  private def addCorners(vertices: Array[Array[Vertex]], rectangle: Quadrilateral): Unit = {
    vertices(0)(0) = rectangle.a
    vertices(widthRatio)(0) = rectangle.b
    vertices(widthRatio)(lengthRatio) = rectangle.c
    vertices(0)(lengthRatio) = rectangle.d
  }

  private def calculateSides(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 until widthRatio) {
      vertices(i)(0) = calculateRatioPoint(vertices(0)(0), vertices(widthRatio)(0), widthRatio - i, i)
      vertices(i)(lengthRatio) = calculateRatioPoint(vertices(0)(lengthRatio), vertices(widthRatio)(lengthRatio), widthRatio - i, i)
    }
  }

  private def calculateTops(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 until lengthRatio) {
      vertices(0)(i) = calculateRatioPoint(vertices(0)(0), vertices(0)(lengthRatio), lengthRatio - i, i)
      vertices(widthRatio)(i) = calculateRatioPoint(vertices(widthRatio)(0), vertices(widthRatio)(lengthRatio), lengthRatio - i, i)
    }
  }

  private def calculateRatioPoint(start: Vertex, end: Vertex, a: Int, b: Int): Vertex = {
    new Vertex((a * start.x + b * end.x) / (a + b), (a * start.y + b * end.y) / (a + b), 0)
  }

  private def calculateInsidePoints(vertices: Array[Array[Vertex]]): Unit = {
    for (i <- 1 until lengthRatio) {
      for (j <- 1 until widthRatio) {
        val vertex = calculateRatioPoint(vertices(0)(i), vertices(widthRatio)(i), widthRatio - j, j)
        vertices(j)(i) = vertex
      }
    }
  }

  private def calculateQuadrats(vertices: Array[Array[Vertex]]): List[Quadrilateral] = {
    val polygons: ArrayBuffer[Quadrilateral] = new ArrayBuffer[Quadrilateral]()
    for (i <- 0 to widthRatio) {
      for (j <- 0 to lengthRatio) {
        if (i + 1 <= widthRatio && j + 1 <= lengthRatio) {
          polygons.append(new Quadrilateral(vertices(i)(j), vertices(i + 1)(j), vertices(i + 1)(j + 1), vertices(i)(j + 1)))
        }
      }
    }
    polygons.toList
  }
}
