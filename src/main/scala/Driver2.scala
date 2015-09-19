import scala.collection.mutable.ArrayBuffer
import scala.xml.XML

object Driver2 extends App{


  val distance = 100

  val reader = new MeshReader()
  val pass1 = reader.read("files/shelly_pass1.x3d")
  val pass2 = reader.read("files/shelly_pass2.x3d")
  val pass3 = reader.read("files/shelly_pass3.x3d")

  val mesh1 = new Mesh(pass1)
  val mesh2 = new Mesh(pass2)
  val mesh3 = new Mesh(pass3)
  println("Pass1 corners "+ mesh1.corners)
  println("Pass2 corners "+ mesh2.corners)
  println("Pass3 corners "+ mesh3.corners)

  val minimumArea = findMinimumArea()
  println("Bounding box corners "+ minimumArea)

  def findMinimumArea() : Polygon = {

    //find max min x
    val leftTop = new Vertex(List(mesh1.corners._1.x,mesh2.corners._1.x,mesh3.corners._1.x).max,
      List(mesh1.corners._1.y,mesh2.corners._1.y,mesh3.corners._1.y).max,
      List(mesh1.corners._1.z,mesh2.corners._1.z,mesh3.corners._1.z).max)
    
    val rightTop = new Vertex(List(mesh1.corners._2.x,mesh2.corners._2.x,mesh3.corners._2.x).max,
      List(mesh1.corners._2.y,mesh2.corners._2.y,mesh3.corners._2.y).max,
      List(mesh1.corners._2.z,mesh2.corners._2.z,mesh3.corners._2.z).max)

    val rightBottom = new Vertex(List(mesh1.corners._3.x,mesh2.corners._3.x,mesh3.corners._3.x).min,
      List(mesh1.corners._3.y,mesh2.corners._3.y,mesh3.corners._3.y).min,
      List(mesh1.corners._3.z,mesh2.corners._3.z,mesh3.corners._3.z).min)

    val leftBottom = new Vertex(List(mesh1.corners._4.x,mesh2.corners._4.x,mesh3.corners._4.x).min,
      List(mesh1.corners._4.y,mesh2.corners._4.y,mesh3.corners._4.y).min,
      List(mesh1.corners._4.z,mesh2.corners._4.z,mesh3.corners._4.z).min)


    new Polygon(leftTop,rightTop,rightBottom,leftBottom)

  }

  val geometryHelper = new Geometry()
  val angle = geometryHelper.getAngleBetweenEdges(new Edge(minimumArea.leftTop,minimumArea.rightTop),new Edge(minimumArea.leftTop,minimumArea.leftBottom))
  println("Angle is: "+math.toDegrees(angle))

  val opposite = math.sin(angle)*100
  println("opposite =" + opposite)
  val adjacent = math.cos(angle)*100
  println("adjacent =" + adjacent)






  val facesList = pass1._1.split("-1").array
  val verticesArray = pass1._2.split(" ").array
  val verticesBuffer = new ArrayBuffer[Vertex]()
  val facesBuffer = new ArrayBuffer[Face]()

  for (i <- verticesArray.indices.by(3)) {
    val vertex = new Vertex(verticesArray(i).toDouble,
                            verticesArray(i+1).toDouble,
                            verticesArray(i+2).toDouble)
    verticesBuffer.append(vertex)
  }

  for(i <- facesList.indices) {
    val str = facesList(i).trim
    val verticies = str.split(" ")
    val face = new Face(verticesBuffer(verticies(0).toInt),
                        verticesBuffer(verticies(1).toInt),
                        verticesBuffer(verticies(2).toInt))
    facesBuffer.append(face)
  }

  val flist = facesBuffer.toList
  val list = verticesBuffer.toList
  def minx(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  def miny(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  def maxx(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  def maxy(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2

  val min_x_corner = list.reduceLeft(minx)
  val min_y_corner = list.reduceLeft(miny)
  val max_x_corner = list.reduceLeft(maxx)
  val max_y_corner = list.reduceLeft(maxy)

  val x_increase1 = math.abs(min_x_corner.x - min_y_corner.x)/2
  val x_increase2 = math.abs(max_x_corner.x - max_y_corner.x)/2
  val y_increase1 = math.abs(min_y_corner.y - min_x_corner.y)/2
  val y_increase2 = math.abs(max_y_corner.y - max_x_corner.y)/2




  val polygon1 = new Polygon(mesh1.corners._1,mesh1.corners._2,mesh1.corners._3,mesh1.corners._4)
  val polygon2 = new Polygon(mesh2.corners._1,mesh2.corners._2,mesh2.corners._3,mesh2.corners._4)
  val polygon3 = new Polygon(mesh3.corners._1,mesh3.corners._2,mesh3.corners._3,mesh3.corners._4)

  //val polygon2 = new Polygon(corner1,corner2,corner3,corner4)
  //flist.foreach(f => polygon.contains(f.centroid))

  val polygon_half = new Polygon(min_x_corner,new Vertex(max_y_corner.x-x_increase1,max_y_corner.y,max_y_corner.z),new Vertex(max_x_corner.x-x_increase1,max_x_corner.y,max_x_corner.z),
    min_y_corner)


  println("pass1 area = " + mesh1.getAreaOfFacesInPolygon(polygon1) + " pass1 area clipped = "+mesh1.getAreaOfFacesInPolygon(minimumArea))
  println("pass2 area = " + mesh2.getAreaOfFacesInPolygon(polygon2)+ " pass2 area clipped = "+mesh2.getAreaOfFacesInPolygon(minimumArea))
  println("pass3 area = " + mesh3.getAreaOfFacesInPolygon(polygon3)+ " pass3 area clipped = "+mesh3.getAreaOfFacesInPolygon(minimumArea))

  println("X-axis distances: " + minimumArea.leftTop.distanceXY(minimumArea.rightTop)+" and " +  minimumArea.leftBottom.distanceXY(minimumArea.rightBottom))
  println("X-axis distances: " + minimumArea.leftTop.distanceXY(minimumArea.leftBottom)+" and " +  minimumArea.rightTop.distanceXY(minimumArea.rightBottom))

  //val minRectangle = new Polygon(minimumArea.leftTop,minimumArea.rightTop,new Vertex())
  //list.reduceLeft((l,r) => if (r.v1.x < l.v1.x) r else l)

  //implicit val personOrdering = Ordering.by((p:Vertex) => p.x)  smallest = list.min


  println("There are " + facesList.length + " faces and " + verticesArray.length +" verticies")
  println("The vertices number is "+ verticesBuffer.size)
  println("The faces number is "+ facesBuffer.size)
  println("The first face is "+ facesBuffer.head + " and the area is: "+facesBuffer.head.area)
  println("The min x is "+ min_x_corner)
  println("The min y is "+ min_y_corner)
  println("The max x is "+ max_x_corner)
  println("The max y is "+ max_y_corner)
  println("X increase "+ x_increase1)
  println("X increase "+ x_increase2)
  println("Y increase "+ y_increase1)
  println("Y increase "+ y_increase2)


}
