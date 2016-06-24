package mesh.io

import java.io.File

import mesh.shapes.{DimensionOrder, Mesh}
import org.apache.commons.io.FilenameUtils

class MeshReader(verbose: Boolean, order: DimensionOrder) {

  def read(file: File): Mesh = {
    FilenameUtils.getExtension(file.getName) match {
      case "x3d" => new X3DReader(verbose, order).read(file)
      case "obj" => new ObjReader(verbose, order).read(file)
      case _ => throw new IllegalArgumentException("File type is not supported")
    }
  }

}