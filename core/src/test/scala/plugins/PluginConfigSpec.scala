package org.scalats.plugins

import java.net.URL

import scala.collection.immutable.Set

import scala.xml.XML

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import org.scalats.core.{ Configuration => Settings, FieldNaming, Logger }

final class PluginConfigSpec extends AnyFlatSpec with Matchers {
  lazy val logger = Logger(org.slf4j.LoggerFactory getLogger getClass)

  {
    lazy val xml = XML.loadString("<scalats></scalats>")
    val defaultCfg = Configuration()

    it should "load configuration from minimal XML" in {
      Configuration.load(xml, logger, None) should equal(defaultCfg)
    }

    it should "write XML for default configuration" in {
      Configuration.load(
        Configuration.toXml(defaultCfg), logger, None) should equal(defaultCfg)
    }
  }

  {
    lazy val xml = XML.load(getClass getResourceAsStream "/plugin-conf.xml")
    val customConfig = Configuration(
      compilationRuleSet = SourceRuleSet(
        includes = Set("ScalaParserSpec\\.scala", "Transpiler.*"),
        excludes = Set("foo")),
      typeRuleSet = SourceRuleSet(
        includes = Set("org\\.scalats\\.core\\..*"),
        excludes = Set(".*Spec", f"ScalaRuntimeFixtures$$", "object:.*ScalaParserResults", "FamilyMember(2|3)")),
      settings = Settings(
        typescriptIndent = "  ",
        prependIPrefix = false,
        prependEnclosingClassNames = false,
        fieldNaming = FieldNaming.SnakeCase))

    it should "load configuration from fully defined XML" in {
      val cfg = Configuration.load(xml, logger, None)

      cfg should equal(customConfig)
    }

    it should "write XML from full configuration" in {
      Configuration.load(
        Configuration.toXml(
          customConfig), logger, None) should equal(customConfig)
    }
  }

  {
    lazy val xml = XML.loadString("<scalats><additionalClasspath><url>file:///tmp/foo1</url><url>file:///tmp/foo2</url></additionalClasspath></scalats>")
    val cfg = Configuration(additionalClasspath = Seq(
      new URL("file:///tmp/foo1"),
      new URL("file:///tmp/foo2")))

    it should "load XML configuration with additional classpath" in {
      Configuration.load(xml, logger, None) should equal(cfg)
    }

    it should "write XML for configuration with additional classpath" in {
      Configuration.load(
        Configuration.toXml(cfg), logger, None) should equal(cfg)
    }
  }
}
