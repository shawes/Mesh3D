package mesh

import mesh.shapes.{Face, Quadrilateral, Vertex}

import scala.collection.mutable.ArrayBuffer

/**
  *
  * @constructor creates a new mesh
  * @param values a tuple containing the (faces, vertices) as string objects
  * @param order  specifies the order of dimensions of the 3D mesh (width-length-height as XYZ)
  */
class Mesh(val values: (String, String), val order: DimensionOrder) {

  val vertices = constructVerticesList()
  val faces = constructFacesList()
  val extremes = Tuple4(vertices.reduceLeft(max_x).x,
    vertices.reduceLeft(max_y).y, vertices.reduceLeft(min_x).x, vertices.reduceLeft(min_y).y)

  def getTotalArea(polygons: List[Quadrilateral]): Double = {
    getThreeDimensionAreas(polygons).sum
  }

  def getThreeDimensionAreas(polygons: List[Quadrilateral]): List[Double] = {
    polygons.map(x => getAreaOfFacesInPolygon(x, is3DArea = true))
  }

  def getTwoDimensionAreas(polygons: List[Quadrilateral]): List[Double] = {
    polygons.map(x => getAreaOfFacesInPolygon(x, is3DArea = false))
  }

  private def getAreaOfFacesInPolygon(polygon: Quadrilateral, is3DArea: Boolean): Double = {
    var area = 0.0
    for(face <- faces if polygon.contains(face.centroid)) {
      if (is3DArea) area += face.area else area += face.area2D
    }
    area
  }

  /*
  Uses the constants X,Y,Z to determine the width and the length of the mesh as orientated
  in the mesh. It assumes the width is X, the length is Y and Z is the height.
   */
  private def constructVerticesList() : ArrayBuffer[Vertex] = {
    val verticesArray = values._2.split(" ").array
    val verticesBuffer = new ArrayBuffer[Vertex]
    for (i <- verticesArray.indices.by(3)) {
      val vertex = new Vertex(verticesArray(i + order.getFirst).toDouble,
        verticesArray(i + order.getSecond).toDouble,
        verticesArray(i + order.getThird).toDouble)
      verticesBuffer.append(vertex)
    }
    verticesBuffer
  }

  private def constructFacesList() : ArrayBuffer[Face] = {
    val facesArray: Array[String] = values._1.split("-1")
    val facesBuffer = new ArrayBuffer[Face]()
    for(i <- facesArray.indices) {
      val str:String = facesArray(i).trim
      val verticesString = str.split(" ")
      val face = new Face(vertices(verticesString(0).toInt), vertices(verticesString(1).toInt), vertices(verticesString(2).toInt))
      facesBuffer.append(face)
    }
    facesBuffer
  }

  private def min_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  private def min_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  private def max_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  private def max_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2
}
