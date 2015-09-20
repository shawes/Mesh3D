
import math._
import Double._

class Polygon(val leftTop: Vertex, val rightTop: Vertex, val rightBottom: Vertex, val leftBottom: Vertex) {

  val points = List(leftTop, rightTop, rightBottom, leftBottom)
  val edges = List((leftTop,rightTop),(rightTop,rightBottom),(rightBottom,leftBottom),(leftBottom,leftTop))
  def contains(vertex: Vertex): Boolean = {
    //isInsideSquare(vertex)
    //inBoundingBox(vertex)
    rayCasting2(vertex)
    //isInsideUsingScalar(vertex)
  }

  def triangleArea(A:Vertex,B:Vertex,C:Vertex):Double= {
     (C.x*B.y-B.x*C.y)-(C.x*A.y-A.x*C.y)+(B.x*A.y-A.x*B.y)
  }
  def isInsideSquare(P:Vertex):Boolean = {
    val areaWithPoint = triangleArea(leftTop, rightTop, P) + triangleArea(rightTop, rightBottom, P) + triangleArea(rightBottom, leftBottom, P) + triangleArea(leftBottom, leftTop, P)
    areaWithPoint < area
  }

  def isInsideUsingScalar(p : Vertex): Boolean = {

    //(0<AM⋅AB<AB⋅AB)∧(0<AM⋅AD<AD⋅AD)
    val AP = createVector(leftTop,p)
    val AB = createVector(leftTop,rightTop)
    val AD = createVector(leftTop,leftBottom)



    (0 < scalarProduct(AP,AB) && scalarProduct(AP,AB) < scalarProduct(AB,AB)) && (0 < scalarProduct(AP,AD) && scalarProduct(AP,AD) < scalarProduct(AD,AD))
  }

  def scalarProduct(v1: Vertex, v2: Vertex) : Double = {
    (v1.x * v2.x) + (v1.y * v2.y)
  }

  def createVector(v1: Vertex, v2: Vertex):Vertex = {
    new Vertex(v2.x-v1.x,v2.y-v1.y,v2.z-v1.z)
  }

 private val epsilon = 0.1

  val area = leftTop.distanceXY(rightTop)*leftTop.distanceXY(leftBottom)

  private def inBoundingBox(ver: Vertex): Boolean = {
//  if(ver.x == leftTop.x ) {
//    ((ver.x+epsilon) > leftTop.x && (ver.x+epsilon) < rightBottom.x) && (ver.y > leftBottom.y && ver.y < rightTop.y)
//  }
//    else if(ver.x == rightBottom.x) {
//      ((ver.x+epsilon) > leftTop.x && (ver.x+epsilon) < rightBottom.x) && (ver.y > leftBottom.y && ver.y < rightTop.y)
//    }
//    else if(ver.y == leftBottom.y) {
//      (ver.x > leftTop.x && ver.x < rightBottom.x) && (ver.y+epsilon > leftBottom.y && ver.y+epsilon < rightTop.y)
//    }
//
//    else if(ver.y == rightTop.y) {
//      (ver.x > leftTop.x && ver.x < rightBottom.x) && (ver.y+epsilon > leftBottom.y && ver.y+epsilon < rightTop.y)
//    } else {
    val inside_left_x = ver.x > leftTop.x
    println(ver.x + " is greater than " + leftTop.x + " " + inside_left_x)
    val inside_right_x = ver.x < leftBottom.x
    println(ver.x + " is less than " + leftBottom.x + " " + inside_right_x)
    val inside_bottom_y = ver.y > rightBottom.y
    println(ver.y + " is greater than " + rightBottom.y + " " + inside_bottom_y)
    val inside_top_y = ver.y  < rightTop.y
    println(ver.y + " is greater than " + rightTop.y + " " + inside_left_x)

    inside_left_x && inside_right_x && inside_bottom_y && inside_top_y

    //(ver.x.>(leftTop.x) & ver.x.<(rightBottom.x)) && (ver.y.>(leftBottom.y) & ver.y.<(rightTop.y))




  }

  private def rayCasting(centroid: Vertex): Boolean = {

    var result = 0
    var j = points.size - 1
    for (i <- points.indices) {
      if (points(i).x < centroid.x && points(j).x >= centroid.x ||
        points(j).x < centroid.x && points(i).x >= centroid.x) {
        if (points(i).y + (centroid.x - points(i).x) / (points(j).x - points(i).x) * (points(j).y - points(i).y) < centroid.y) {
          result += 1
        }
      }
      j = i
    }
    result % 2 == 0

  }

  private def raySegI(p: Vertex, e: (Vertex, Vertex)): Boolean = {
    val epsilon = 0.00001
    if (e._1.y > e._2.y)
      return raySegI(p, (e._2, e._1))
    if (p.y == e._1.y || p.y == e._2.y)
      return raySegI(new Vertex(p.x, p.y + epsilon,0), e)
    if (p.y > e._2.y || p.y < e._1.y || p.x > max(e._1.x, e._2.x))
      return false
    if (p.x < min(e._1.x, e._2.x))
      return true
    val blue = if (abs(e._1.x - p.x) > MinValue) (p.y - e._1.y) / (p.x - e._1.x) else MaxValue
    val red = if (abs(e._1.x - e._2.x) > MinValue) (e._2.y - e._1.y) / (e._2.x - e._1.x) else MaxValue
    blue >= red
  }

  private def rayCasting2(p: Vertex) = edges.count(raySegI(p, _)) % 2 != 0

  override def toString = points.toString()

}
