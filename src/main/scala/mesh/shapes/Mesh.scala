package mesh.shapes

import grizzled.math.stats._

import scala.Numeric._
import scala.collection.mutable.ArrayBuffer


/**
  *
  * @constructor creates a new mesh
  * @param vertices
  * @param faces
  */
class Mesh(val vertices: List[Vertex], val faces: List[Face]) {

  val extremes = Tuple4(vertices.reduceLeft(max_x).x,
    vertices.reduceLeft(max_y).y, vertices.reduceLeft(min_x).x, vertices.reduceLeft(min_y).y)

  def getArea(quadratsList: Seq[List[Quadrat]]): Seq[List[(Double, Double, Int, Int)]] = {
    quadratsList.map(quadrats => quadrats.map(quadrat => getAreaOfFacesInPolygon(quadrat)))
  }

  private def getAreaOfFacesInPolygon(quadrat: Quadrat): (Double, Double, Int, Int) = {
    var area3d, area2d = 0.0
    var facesCount = 0
    val vertices = new ArrayBuffer[Vertex]()
    val iterator = faces.iterator
    while (iterator.hasNext) {
      val face = iterator.next()
      if (quadrat.contains(face.centroid)) {
        facesCount += 1
        area3d += face.area3D
        area2d += face.area2D

        vertices += face.v1
        vertices += face.v2
        vertices += face.v3
      }
    }

    val valuesOfZ = vertices.map(v => v.z)
    if (valuesOfZ.nonEmpty) {
    quadrat.relativeZAvg = arithmeticMean(valuesOfZ: _*)
    quadrat.relativeZStd = populationStandardDeviation(valuesOfZ: _*)
    }

    (area3d, area2d, facesCount, vertices.distinct.size)
  }


  private def min_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  private def min_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  private def max_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  private def max_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2
}
