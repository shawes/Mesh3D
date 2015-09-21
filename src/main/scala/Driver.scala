object Driver extends App {

  val widthRatio = 2
  val lengthRatio = 6

  val reader = new MeshReader()
  val pass1 = reader.read("files/shelly_pass1.x3d")
  val pass2 = reader.read("files/shelly_pass2.x3d")
  val pass3 = reader.read("files/shelly_pass3.x3d")

  val geometry = new Geometry()
  
  val mesh1 = new Mesh(pass1)
  val mesh2 = new Mesh(pass2)
  val mesh3 = new Mesh(pass3)

  val rectangle = geometry.findMaximumBoundingBox(List(mesh1, mesh2, mesh3))

  println("Width distance: " + rectangle.leftTop.distanceXY(rectangle.rightTop) + " and " + rectangle.leftBottom.distanceXY(rectangle.rightBottom))
  println("Length distance: " + rectangle.leftTop.distanceXY(rectangle.leftBottom) + " and " + rectangle.rightTop.distanceXY(rectangle.rightBottom))

  val rectangleSubDivider = new RectangleSubDivider()
  rectangleSubDivider.setRatios(6, 18)
  val polygons = rectangleSubDivider.divideRectangle(rectangle)

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



}
