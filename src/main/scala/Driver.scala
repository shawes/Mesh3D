object Driver extends App {

  val reader = new MeshReader()
  val pass1 = reader.read("files/shelly_pass1.x3d")
  val pass2 = reader.read("files/shelly_pass2.x3d")
  val pass3 = reader.read("files/shelly_pass3.x3d")

  val geometry = new Geometry()
  
  val mesh1 = new Mesh(pass1)
  val mesh2 = new Mesh(pass2)
  val mesh3 = new Mesh(pass3)

  val rectangle = geometry.findMaximumBoundingBox(List(mesh1, mesh2, mesh3))

  println("Width distance: " + rectangle.a.distanceXY(rectangle.b) + " and " + rectangle.d.distanceXY(rectangle.c))
  println("Length distance: " + rectangle.a.distanceXY(rectangle.d) + " and " + rectangle.b.distanceXY(rectangle.c))

  val rectangleSubDivider = new RectangleSubDivider(widthRatio = 6, lengthRatio = 18)
  val polygons = rectangleSubDivider.divideRectangle(rectangle)

  println("There are this many polygons "+ polygons.size)

  println("--Mesh 1 started--")
  println("Area = " + mesh1.getArea(polygons))
  println("--Mesh 1 finished--")
  println()
  println("--Mesh 2 started--")
  println("Area = " + mesh2.getArea(polygons))
  println("--Mesh 2 finished--")
  println()
  println("--Mesh 3 started--")
  println("Area = " + mesh3.getArea(polygons))
  println("--Mesh 3 finished--")
  println()
}
