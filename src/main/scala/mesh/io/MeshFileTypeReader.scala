package mesh.io

import java.io.File

import mesh.shapes.Mesh

/**
  *
  * Created by Steven Hawes on 24/06/16.
  */
abstract class MeshFileTypeReader {
  def read(file: File): Mesh
}
