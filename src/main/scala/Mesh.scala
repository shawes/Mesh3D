import scala.collection.mutable.ArrayBuffer

class Mesh(val tuple: (String, String), is3DArea: Boolean) {

  val vertices = constructVerticesList()
  val faces = constructFacesList()
  val corners = Tuple4(vertices.reduceLeft(min_x),
    vertices.reduceLeft(max_y),vertices.reduceLeft(max_x),vertices.reduceLeft(min_y))

  def getTotalArea(polygons: List[Polygon]): Double = {
    getAreas(polygons).sum
  }

  def getAreas(polygons: List[Polygon]): List[Double] = {
    polygons.map(x => getAreaOfFacesInPolygon(x))
  }

  def getAreaOfFacesInPolygon(polygon : Polygon) : Double = {
    var area = 0.0
    for(face <- faces if polygon.contains(face.centroid)) {
      if (is3DArea) area += face.area else area += face.area2D
    }
    area
  }

  /*
  Note the y and z vertices are reversed due to the axis of the mesh
   */
  private def constructVerticesList() : ArrayBuffer[Vertex] = {
    val verticesArray = tuple._2.split(" ").array
    val verticesBuffer = new ArrayBuffer[Vertex]
    for (i <- verticesArray.indices.by(3)) {
      val vertex = new Vertex(verticesArray(i).toDouble,
        verticesArray(i+2).toDouble,
        verticesArray(i+1).toDouble)
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
