package mesh.io

import java.io.File

import mesh.shapes.Mesh

abstract class MeshFileTypeReader {
  def read(file: File): Mesh
}
