import scala.xml.XML
import java.io.File

class MeshReader() {

  def read(file: File) : (String,String) = {
    val xml = XML.loadFile(file)

    val faces = (xml \\ "X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \ "@coordIndex").text
    val vertices = (xml \\"X3D" \ "Scene" \ "Shape" \ "IndexedFaceSet" \"Coordinate" \ "@point").text

    new Tuple2(faces,vertices)
  }
}