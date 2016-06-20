package mesh

import java.io.File

case class Config(dim: String = "",
                  size: Double = 0.0,
                  verbose: Boolean = false,
                  out: String = "",
                  files: Seq[File] = Seq())

