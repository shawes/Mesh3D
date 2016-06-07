package mesh

import scala.collection.mutable.ArrayBuffer

class Mesh(val tuple: (String, String), val order: DimensionOrder) {

  // These values will need to change depending on the orientation of the axis in mesh
  val X = 1
  val Y = 2
  val Z = 0

  val vertices = constructVerticesList()
  val faces = constructFacesList()
  val corners = Tuple4(vertices.reduceLeft(min_x),
    vertices.reduceLeft(max_y),vertices.reduceLeft(max_x),vertices.reduceLeft(min_y))

  def getTotalArea(polygons: List[Quadrilateral]): Double = {
    getAreas(polygons).sum
  }

  def getAreas(polygons: List[Quadrilateral]): List[Double] = {
    polygons.map(x => getAreaOfFacesInPolygon(x, is3DArea = true))
  }

  private def getAreaOfFacesInPolygon(polygon: Quadrilateral, is3DArea: Boolean): Double = {
    var area = 0.0
    for(face <- faces if polygon.contains(face.centroid)) {
      if (is3DArea) area += face.area else area += face.area2D
    }
    area
  }

  def get2DAreas(polygons: List[Quadrilateral]): List[Double] = {
    polygons.map(x => getAreaOfFacesInPolygon(x, is3DArea = false))
  }

  /*
  Uses the constants X,Y,Z to determine the width and the length of the mesh as orientated
  in the mesh. It assumes the width is X, the length is Y and Z is the height.
   */
  private def constructVerticesList() : ArrayBuffer[Vertex] = {
    val verticesArray = tuple._2.split(" ").array
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
    val facesArray: Array[String] = tuple._1.split("-1")
    val facesBuffer = new ArrayBuffer[Face]()
    for(i <- facesArray.indices) {
      val str:String = facesArray(i).trim
      val verticies = str.split(" ")
      val face = new Face(vertices(verticies(0).toInt), vertices(verticies(1).toInt), vertices(verticies(2).toInt))
      facesBuffer.append(face)
    }
    facesBuffer
  }

  private def min_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  private def min_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  private def max_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  private def max_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2
}
