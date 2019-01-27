package org.scalats.core

import scala.util.matching.Regex

//import scala.reflect.internal.util.Position
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Global, Phase}

final class CompilerPlugin(val global: Global) extends Plugin { plugin =>
  val name = "scalats"
  val description = "Scala compiler plugin for TypeScript (scalats)"
  val components: List[PluginComponent] = List(Component)

  /*
  override def processOptions(options: List[String], error: String => Unit): Unit = {
    options.foreach { opt =>
      if (opt startsWith "globalFilters=") {
        globalFilters = opt.drop(14).split(";").map(_.r).toList
      }
    }

    global.inform(s""" using global filters: ${globalFilters.mkString(",")}""")

    global.reporter = reporter
  }


  override val optionsHelp: Option[String] = Some(
    "  -P:scalats:globalFilters=...             Semi-colon separated patterns to filter the warning messages")
   */

  private object Component extends PluginComponent {
    val global: plugin.global.type = plugin.global
    val runsAfter = List("typer")
    val phaseName = plugin.name

    import global._

    def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      def apply(unit: CompilationUnit): Unit = foo(unit)
    }

    def foo(unit: CompilationUnit): Unit = {
      // 1 unit per scala file
      println(s"unit = $unit // ${unit.defined}")
      // rootMirror
    }
  }
}
