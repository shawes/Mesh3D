import scala.collection.mutable.ArrayBuffer

class Mesh (val tuple : Tuple2[String,String]){

  println("Creating a mesh")

  val vertices = constructVerticesList()
  val faces = constructFacesList()
  val corners = Tuple4(vertices.reduceLeft(min_x),
    vertices.reduceLeft(max_y),vertices.reduceLeft(max_x),vertices.reduceLeft(min_y))

  var total_faces = 0

  def getAreaOfFacesInPolygon(polygon : Polygon) : Double = {
    var area = 0.0
    for(face <- faces if polygon.contains(face.centroid)) {
      area += face.area
      face.used =true
      face.polys.append(polygon)
      total_faces += 1
    }
    area
  }

  private def constructVerticesList() : ArrayBuffer[Vertex] = {
    val verticesArray = tuple._2.split(" ").array
    val verticesBuffer = new ArrayBuffer[Vertex]
    for (i <- verticesArray.indices.by(3)) {
      val vertex = new Vertex(verticesArray(i).toDouble,
        verticesArray(i+2).toDouble,
        verticesArray(i+1).toDouble)
      verticesBuffer.append(vertex)
    }
    println("Vertices = " +verticesBuffer.size)
    verticesBuffer

  }

  private def constructFacesList() : ArrayBuffer[Face] = {
    val facesArray = tuple._1.split("-1").array
    val facesBuffer = new ArrayBuffer[Face]()
    for(i <- facesArray.indices) {
      val str = facesArray(i).trim
      val verticies = str.split(" ")
      val face = new Face(vertices(verticies(0).toInt),
        vertices(verticies(1).toInt),
        vertices(verticies(2).toInt))
      facesBuffer.append(face)
    }
    println("Faces = " +facesBuffer.size)
    facesBuffer
  }


  private def min_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  private def min_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  private def max_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  private def max_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2
}
