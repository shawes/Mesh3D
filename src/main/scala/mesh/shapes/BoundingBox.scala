package mesh.shapes

import scala.collection.parallel.ParSeq

/**
  *
  * Created by Steven Hawes on 27/06/16.
  */
class BoundingBox(a: Vertex, b: Vertex, c: Vertex, d: Vertex) extends Quadrilateral(a, b, c, d) {

  def this(meshes: ParSeq[Mesh]) =
    this(new Vertex(meshes.map(f => f.extremes._3).min, meshes.map(f => f.extremes._4).min, 0),
      new Vertex(meshes.map(f => f.extremes._3).min, meshes.map(f => f.extremes._2).max, 0),
      new Vertex(meshes.map(f => f.extremes._1).max, meshes.map(f => f.extremes._2).max, 0),
      new Vertex(meshes.map(f => f.extremes._1).max, meshes.map(f => f.extremes._4).min, 0))


}
