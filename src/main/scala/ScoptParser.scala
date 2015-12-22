import java.io.File

case class ScoptParser(info: Boolean = false, out: File = new File("."),
                       dimensions: String = "", width: Int = 1, length: Int = 1,
                       files: Seq[File] = Seq())



