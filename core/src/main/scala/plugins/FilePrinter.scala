package io.github.scalats.plugins

import java.io.{ File, FileOutputStream, PrintStream }

import scala.collection.immutable.ListSet

import io.github.scalats.core.Settings
import io.github.scalats.typescript.{ Declaration, TypeRef }

// TODO: Printer that gather class and interface
final class FilePrinter(outDir: File) extends BasePrinter {
  private val tracker = scala.collection.mutable.Map.empty[String, File]

  @com.github.ghik.silencer.silent(".*kind.*never used.*")
  def apply(
    conf: Settings,
    kind: Declaration.Kind,
    name: String,
    requires: ListSet[TypeRef]): PrintStream = {

    val f = tracker.getOrElseUpdate(name, {
      val n = new File(outDir, s"${name}.ts")

      // Make sure it's cleaned before the first output
      n.delete()

      n
    })

    val stream = new PrintStream(new FileOutputStream(f, true))

    printPrelude(stream)

    printImports(conf, requires, stream) { tpe =>
      s"./${tpe.name}"
    }

    stream
  }
}
