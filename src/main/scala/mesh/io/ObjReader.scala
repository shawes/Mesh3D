package mesh.io

import java.io.File

import mesh.shapes.{DimensionOrder, Mesh}

/**
  *
  * Created by Steven Hawes on 24/06/16.
  */
class ObjReader(verbose: Boolean, order: DimensionOrder) extends MeshFileTypeReader {

  def read(file: File): Mesh = {
    null
  }

}
