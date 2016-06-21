package mesh.shapes

import mesh.DimensionOrder

import scala.collection.mutable.ArrayBuffer

/**
  *
  * @constructor creates a new mesh
  * @param values a tuple containing the (faces, vertices) as string objects
  * @param order  specifies the order of dimensions of the 3D mesh (width-length-height as XYZ)
  */
class Mesh(val values: (Iterator[Char], Iterator[Char]), val order: DimensionOrder) {

  val vertices = constructVerticesList()
  val faces = constructFacesList()
  val extremes = Tuple4(vertices.reduceLeft(max_x).x,
    vertices.reduceLeft(max_y).y, vertices.reduceLeft(min_x).x, vertices.reduceLeft(min_y).y)

  def getTotalArea(polygons: List[Quadrilateral]): Double = {
    getThreeDimensionAreas(Seq(polygons)).head.sum
  }

  def getThreeDimensionAreas(quadratsList: Seq[List[Quadrilateral]]): Seq[List[Double]] = {
    quadratsList.map(quadrats => quadrats.map(quadrat => getAreaOfFacesInPolygon(quadrat, is3DArea = true)))
  }

  def getTwoDimensionAreas(quadratsList: Seq[List[Quadrilateral]]): Seq[List[Double]] = {
    quadratsList.map(quadrats => quadrats.map(quadrat => getAreaOfFacesInPolygon(quadrat, is3DArea = false)))
  }

  private def getAreaOfFacesInPolygon(polygon: Quadrilateral, is3DArea: Boolean): Double = {
    var area = 0.0
    val iterator = faces.iterator
    while (iterator.hasNext) {
      val face = iterator.next()
      if (polygon.contains(face.centroid)) {
        if (is3DArea) area += face.area else area += face.area2D
      }
    }
    area
  }

  /*
  Uses the constants X,Y,Z to determine the width and the length of the mesh as orientated
  in the mesh. It assumes the width is X, the length is Y and Z is the height.
   */

  private def constructVerticesList(): ArrayBuffer[Vertex] = {
    val iterator = values._2
    val verticesBuffer = new ArrayBuffer[Vertex]

    while (iterator.hasNext) {
      val x = getNextNumber(iterator)
      val y = getNextNumber(iterator)
      val z = getNextNumber(iterator)

      verticesBuffer += getVertexInDimensionOrder(x, y, z)

      }
    println("Constructed vertices, there were " + verticesBuffer.size)
      verticesBuffer
  }

  private def getVertexInDimensionOrder(x: Double, y: Double, z: Double): Vertex = {
    order.getThird match {
      case 0 => new Vertex(z, y, x)
      case 1 => new Vertex(x, z, y)
      case _ => new Vertex(x, y, z)
    }
  }

  private def constructFacesList(): ArrayBuffer[Face] = {
    val iterator = values._1
    val facesBuffer = new ArrayBuffer[Face]()
    while (iterator.hasNext) {
      val face = new Face(vertices(getNextNumber(iterator).toInt), vertices(getNextNumber(iterator).toInt), vertices(getNextNumber(iterator).toInt))
      facesBuffer += face
      getNextNumber(iterator).toInt // This is the -1 separator
    }
    println("Constructed faces, there were " + facesBuffer.size)
    facesBuffer
  }

  private def getNextNumber(iterator: Iterator[Char]): Double = {
    val numberString: StringBuilder = new StringBuilder()

    while ((numberString.isEmpty || numberString.last != ' ') && iterator.hasNext) {
      numberString + iterator.next()
    }
    var number = 0.0
    if (numberString.nonEmpty) {
      try {
        number = numberString.toString().trim.toDouble
      } catch {
        case e: java.lang.NumberFormatException => println("Can't parse this " + numberString.toString())
      }
    }
    number
  }

  private def min_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x < s2.x) s1 else s2
  private def min_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y < s2.y) s1 else s2
  private def max_x(s1: Vertex, s2: Vertex): Vertex = if (s1.x > s2.x) s1 else s2
  private def max_y(s1: Vertex, s2: Vertex): Vertex = if (s1.y > s2.y) s1 else s2
}
