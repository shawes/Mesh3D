package mesh

import java.io.File

case class Config(dim: String = "XYZ",
                  size: Seq[Double] = Seq(),
                  verbose: Boolean = false,
                  out: String = "",
                  files: Seq[File] = Seq())

