import scala.collection.mutable.ArrayBuffer
import scala.xml.XML

object Driver2 extends App {

  val reader = new MeshReader()
  val pass1 = reader.read("files/shelly_pass1.x3d")
  val pass2 = reader.read("files/shelly_pass2.x3d")
  val pass3 = reader.read("files/shelly_pass3.x3d")

  val geometry = new Geometry()
  
  val mesh1 = new Mesh(pass1)
  val mesh2 = new Mesh(pass2)
  val mesh3 = new Mesh(pass3)
  
  println("Pass1 corners " + mesh1.corners)
  println("Pass2 corners " + mesh2.corners)
  println("Pass3 corners " + mesh3.corners)

  val maxBoundingBox = geometry.findMaximumBoundingBox(mesh1,mesh2,mesh3)
  println("Bounding box corners " + maxBoundingBox)

  val leftEdge = new Edge(maxBoundingBox.leftTop, maxBoundingBox.rightTop)
  val rightEdge = new Edge(maxBoundingBox.leftBottom, maxBoundingBox.rightBottom)
  println("Slope left = " + leftEdge.slope + " slope right= " + rightEdge.slope)

  val alignedCorner = new Vertex(maxBoundingBox.leftTop.x + math.abs(rightEdge.x_disp), maxBoundingBox.leftTop.y + math.abs(rightEdge.y_disp), maxBoundingBox.leftTop.z)

  val rectangle = new Polygon(maxBoundingBox.leftTop, alignedCorner, maxBoundingBox.rightBottom, maxBoundingBox.leftBottom)
  println("Rectangle corners " + rectangle)

  val leftEdgeRec = new Edge(maxBoundingBox.leftTop, maxBoundingBox.rightTop)
  val rightEdgeRec = new Edge(maxBoundingBox.leftBottom, maxBoundingBox.rightBottom)
  val topEdgeRec = new Edge(maxBoundingBox.leftTop, maxBoundingBox.leftBottom)
  val bottomEdgeRec = new Edge(maxBoundingBox.rightTop, maxBoundingBox.rightBottom)
  println("Slope left = " + leftEdge.slope + " slope right= " + rightEdge.slope)
  println("Slope top = " + topEdgeRec.slope + " slope bottom= " + bottomEdgeRec.slope)

  val polygon1 = new Polygon(mesh1.corners._1, mesh1.corners._2, mesh1.corners._3, mesh1.corners._4)
  val polygon2 = new Polygon(mesh2.corners._1, mesh2.corners._2, mesh2.corners._3, mesh2.corners._4)
  val polygon3 = new Polygon(mesh3.corners._1, mesh3.corners._2, mesh3.corners._3, mesh3.corners._4)

  //println("pass1 area = " + mesh1.getAreaOfFacesInPolygon(polygon1) + " pass1 area clipped = "+mesh1.getAreaOfFacesInPolygon(minimumArea))
  //println("pass2 area = " + mesh2.getAreaOfFacesInPolygon(polygon2)+ " pass2 area clipped = "+mesh2.getAreaOfFacesInPolygon(minimumArea))
  //println("pass3 area = " + mesh3.getAreaOfFacesInPolygon(polygon3)+ " pass3 area clipped = "+mesh3.getAreaOfFacesInPolygon(minimumArea))

  println("X-axis distances: " + rectangle.leftTop.distanceXY(rectangle.rightTop) + " and " + rectangle.leftBottom.distanceXY(rectangle.rightBottom))
  println("X-axis distances: " + rectangle.leftTop.distanceXY(rectangle.leftBottom) + " and " + rectangle.rightTop.distanceXY(rectangle.rightBottom))

  def calculateRatioPoint(start:Vertex, end:Vertex,a:Int,b:Int) : Vertex = {
    new Vertex((a*start.x + b*end.x)/(a+b),(a*start.y + b*end.y)/(a+b),0)
  }

  val widthRatio = 2
  val lengthRatio = 6

  val verticesMatrix = Array.ofDim[Vertex](widthRatio + 1, lengthRatio + 1)

  //put in the corners

  verticesMatrix(0)(0) = rectangle.leftTop
  verticesMatrix(widthRatio)(0) = rectangle.rightTop
  verticesMatrix(widthRatio)(lengthRatio) = rectangle.rightBottom
  verticesMatrix(0)(lengthRatio) = rectangle.leftBottom

  println("Calculating sides")
  // Sides
  for (i <- 1 to widthRatio - 1) {
    val leftVertex = calculateRatioPoint(verticesMatrix(0)(0), verticesMatrix(widthRatio)(0), widthRatio - i, i)
    //println("i="+i+" ratio="+(sideRatio-i))
    verticesMatrix(i)(0) = leftVertex
    //println(leftVertex)

    val rightVertex = calculateRatioPoint(verticesMatrix(0)(lengthRatio), verticesMatrix(widthRatio)(lengthRatio), widthRatio - i, i)
    //println("i="+i+" ratio="+(sideRatio-i))
    verticesMatrix(i)(lengthRatio) = rightVertex
    //println(rightVertex)
  }
  println("Calculating tops")
  // Tops
  for (i <- 1 to lengthRatio - 1) {
    val bottomVertex = calculateRatioPoint(verticesMatrix(0)(0), verticesMatrix(0)(lengthRatio), lengthRatio - i, i)
    //println("i="+i+" ratio="+(longRatio-i))
    verticesMatrix(0)(i) = bottomVertex
    //println(bottomVertex)

    val topVertex = calculateRatioPoint(verticesMatrix(widthRatio)(0), verticesMatrix(widthRatio)(lengthRatio), lengthRatio - i, i)
    //println("i="+i+" ratio="+(longRatio-i))
    verticesMatrix(widthRatio)(i) = topVertex
    //println(topVertex)
  }

  println("Calculating insides")
  for (i <- 1 to lengthRatio - 1) {
    for (j <- 1 to widthRatio - 1) {
      val vertex = calculateRatioPoint(verticesMatrix(0)(i), verticesMatrix(widthRatio)(i), widthRatio - j, j)
      //println("i=" + i + " ratio=" + (sideRatio - j))
      verticesMatrix(j)(i) = vertex
      //println(vertex)
    }
  }

  val polygons: ArrayBuffer[Polygon] = new ArrayBuffer[Polygon]()
  println("Calculating squares")
  for (i <- 0 to widthRatio) {
    for (j <- 0 to lengthRatio) {
      if (i + 1 <= widthRatio && j + 1 <= lengthRatio) {
        //println("OK TO ADD")
        val v1 = verticesMatrix(i)(j)
        val v2 = verticesMatrix(i + 1)(j)
        val v3 = verticesMatrix(i + 1)(j + 1)
        val v4 = verticesMatrix(i)(j + 1)
        val polygon = new Polygon(v1, v2, v3, v4)
        polygons.append(polygon)
        //println(polygon)
        println("(" + i + "," + j + ")")
        //println(polygon.contains(new Vertex(x = 618.0176666666666, y = -27.077833333333334, z = 0.0)))
      }
    }
  }


  println("There are this many polygons "+ polygons.size)

  println("--Mesh 1 area--")
  var sum = 0.0
  for(polygon <- polygons) {
    val area = mesh1.getAreaOfFacesInPolygon(polygon)
    sum += area
    println(area)
  }
  println("Mesh 1 added = "+sum+ " faces = "+ mesh1.total_faces)

  println("--Mesh 2 area--")
  sum = 0.0
  for(polygon <- polygons) {
    val area = mesh2.getAreaOfFacesInPolygon(polygon)
    sum+= area
    println(area)
  }
  println("Mesh 2 added = "+sum + " faces = "+ mesh2.total_faces)

  println("--Mesh 3 area--")
  sum = 0.0
  for(polygon <- polygons) {
    val area = mesh3.getAreaOfFacesInPolygon(polygon)
    sum+= area
    println(area)
  }
  println("Mesh 3 added = "+sum+ " faces = "+ mesh3.total_faces)


  val multiples = mesh1.faces.filter(x=>x.polys.size >1)

  if(multiples.size > 0) {
    val facemulti = multiples.head
    println("With centroid : " + facemulti.centroid + " it was found in " + facemulti.polys(0) + " and " + facemulti.polys(1))
  }


}
