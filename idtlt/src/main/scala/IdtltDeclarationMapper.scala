package io.github.scalats.idtlt

import java.io.PrintStream

import io.github.scalats.ast._
import io.github.scalats.core.{
  DeclarationMapper,
  Emitter,
  Field,
  FieldMapper,
  Settings,
  TypeMapper
}

final class IdtltDeclarationMapper extends DeclarationMapper {

  def apply(
      parent: DeclarationMapper.Resolved,
      settings: Settings,
      typeMapper: TypeMapper.Resolved,
      fieldMapper: FieldMapper,
      declaration: Declaration,
      context: DeclarationMapper.Context,
      out: PrintStream
    ): Option[Unit] = {
    import settings.{ lineSeparator => lineSep, indent }

    val typeNaming = settings.typeNaming(settings, _: TypeRef)

    import declaration.name
    val tpeName = typeNaming(declaration.reference)

    val interfaceTypeGuard = Emitter.interfaceTypeGuard(
      indent + indent,
      _: String,
      _: Iterable[Member],
      { t =>
        val tn = typeNaming(t)
        s"ns${tn}.is${tn}"
      },
      settings
    )

    def valueRightHand(owner: SingletonDeclaration, v: Value): Unit = {
      val vd = ValueBodyDeclaration(ValueMemberDeclaration(owner, v), v)

      apply(parent, settings, typeMapper, fieldMapper, vd, context, out)
        .getOrElse(
          parent(vd, context, out)
        )
    }

    def deriving = s"""// Deriving TypeScript type from ${tpeName} validator
export type ${tpeName} = typeof idtlt${tpeName}.T${lineSep}
"""

    def discrimitedDecl =
      s"""export const idtltDiscriminated${tpeName} = idtlt.intersection(
${indent}idtlt${tpeName},
${indent}idtlt.object({
${indent}${indent}${settings.discriminator.text}: idtlt.literal('${tpeName}')
${indent}})
)${lineSep}

// Deriving TypeScript type from idtltDiscriminated${tpeName} validator
export type Discriminated${tpeName} = typeof idtltDiscriminated${tpeName}.T${lineSep}"""

    declaration match {
      case iface @ InterfaceDeclaration(
            _,
            fields,
            Nil,
            superInterface,
            false
          ) =>
        Some {
          out.println(s"""// Validator for InterfaceDeclaration ${tpeName}
export const idtlt${tpeName} = idtlt.object({""")

          fields.foreach {
            emitField(settings, fieldMapper, typeMapper, out, iface, _)
          }

          out.println(s"})${lineSep}")

          superInterface.foreach { si =>
            out.println(s"""
// Super-type declaration ${si.name} is ignored""")
          }

          out.print(s"""
$deriving
$discrimitedDecl

export const discriminated${tpeName}: (_: ${tpeName}) => Discriminated${tpeName} = (v: ${tpeName}) => ({ ${settings.discriminator.text}: '${tpeName}', ...v })${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return (
${interfaceTypeGuard(tpeName, fields.toList)}
${indent})${lineSep}
}""")
        }

      case i: InterfaceDeclaration =>
        Some {
          out.println(s"// Not supported: InterfaceDeclaration '${name}'")

          if (i.typeParams.nonEmpty) {
            out.println(s"// - type parameters: ${i.typeParams mkString ", "}")
          }

          out.println(s"""
export function is${tpeName}(v: any): boolean {
${indent}return v && false${lineSep}
}""")

        }

      case CompositeDeclaration(name, members) =>
        Some {
          val ms = members.filter {
            case SingletonDeclaration(_, values, _) =>
              values.nonEmpty

            case _ => true
          }

          if (ms.tail.isEmpty) {
            // Excluding empty singleton, only a single type
            ms.headOption.foreach(d => parent(d, context, out))
          } else {
            val ps = members.toList.sortBy(_.name).flatMap { m =>
              val res = m match {
                case member: InterfaceDeclaration => {
                  val iface = member.copy(name = s"I${member.name}")

                  parent(iface, context, out)

                  Some(iface)
                }

                case member: SingletonDeclaration => {
                  val single = member.copy(name = s"${member.name}Singleton")

                  parent(single, context + ("noSingletonAlias" -> "true"), out)

                  out.println(
                    s"""
export const ${m.name}Inhabitant = ${m.name}SingletonInhabitant${lineSep}"""
                  )

                  if (members.size == 1) {
                    // Singleton is not a companion object
                    Some(single)
                  } else {
                    None
                  }
                }

                case member: EnumDeclaration => {
                  val enu = member.copy(name = s"${member.name}Enum")

                  parent(enu, context, out)

                  Some(enu)
                }

                case member: UnionDeclaration => {
                  val union = member.copy(name = s"${member.name}Union")

                  parent(
                    union,
                    context + ("idtltUnionAliasing" -> tpeName),
                    out
                  )

                  Some(union)
                }

                case member: TaggedDeclaration => {
                  val tagged = member.copy(name = s"${member.name}Tagged")

                  parent(tagged, context, out)

                  Some(tagged)
                }

                case member => {
                  parent(member, context, out)

                  Option.empty[Declaration]
                }
              }

              out.println()

              res
            }

            out.print(s"""
// Validator for CompositeDeclaration ${name}
export const idtlt${tpeName} = """)

            ps.headOption.foreach { pt =>
              if (ps.tail.isEmpty) {
                // With singleton excluded, only a single type

                val n = typeNaming(pt.reference)

                out.print(s"""idtlt${n}${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return is${n}(v)${lineSep}
}""")
              } else {
                out.println(s"""idtlt.intersection(""")

                val pst = ps.map(d => typeNaming(d.reference))

                out.print(pst.map { n =>
                  s"${indent}idtltDiscriminated${n}"
                } mkString ",\n")

                out.println(s""")${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return (""")

                out.print(
                  pst.map(n => s"${indent}${indent}is${n}(v)") mkString " &&\n"
                )

                out.print(s"""
${indent})${lineSep}
}""")
              }
            }

            out.println(s"""

$deriving
$discrimitedDecl

// Workaround for local type references in the same module
type private${tpeName} = ${tpeName}$lineSep

namespace ns${tpeName} {
${indent}export type $tpeName = private${tpeName}$lineSep
}""")

          }
        }

      case UnionDeclaration(_, fields, possibilities, None) =>
        Some {
          out.println(s"""// Validator for UnionDeclaration ${tpeName}
export const idtlt${tpeName} = idtlt.union(""")

          val ps = possibilities.toList.sortBy(_.name)
          val pst = ps.map(typeNaming)

          out.print(pst.map { n =>
            s"${indent}ns${n}.idtltDiscriminated${n}"
          } mkString ",\n")

          out.println(s")${lineSep}")

          if (fields.nonEmpty) {
            // TODO: Intersection?

            out.println(s"""
// Fields are ignored: ${fields.map(_.name) mkString ", "}""")
          }

          out.print(s"""
$deriving
$discrimitedDecl

export const ${tpeName}Values = {
""")

          val singletons = ps.flatMap {
            case pt @ SingletonTypeRef(nme, values) => {
              val sd = SingletonDeclaration(nme, values, None)
              val ptpeName = typeNaming(pt)
              val inhabitant = s"ns${ptpeName}.${ptpeName}Inhabitant"

              if (values.headOption.nonEmpty) {
                values.flatMap { v =>
                  List(
                    { () =>
                      out.print(s"${indent}")
                      valueRightHand(sd, v)
                      out.print(s": ${inhabitant}")
                    }
                  )
                }
              } else {
                List(() => out.print(s"${indent}${nme}: ${inhabitant}"))
              }
            }

            case _ =>
              List.empty[() => Unit]
          }

          singletons.zipWithIndex.foreach {
            case (print, i) =>
              if (i > 0) {
                out.println(", ")
              }

              print()
          }

          if (singletons.nonEmpty) {
            out.println()
          }

          out.println(s"""} as const${lineSep}

export type ${tpeName}ValuesKey = keyof typeof ${tpeName}Values${lineSep}""")

          val aliasing = context.get("idtltUnionAliasing")

          aliasing.foreach { base =>
            out.println(s"""
// Aliases for the Union utilities
export const ${base}Values = ${tpeName}Values${lineSep}

export type ${base}ValuesKey = ${tpeName}ValuesKey${lineSep}""")

          }

          if (singletons.nonEmpty) {
            out.print(s"""
export function map${tpeName}Values<T>(f: (_k: ${tpeName}ValuesKey) => T): Readonly<Record<${tpeName}ValuesKey, T>> {
${indent}return {
""")
            ps.flatMap {
              case pt @ SingletonTypeRef(nme, values) => {
                val sd = SingletonDeclaration(nme, values, None)
                val ptpeName = typeNaming(pt)
                val inhabitant = s"ns${ptpeName}.${ptpeName}Inhabitant"

                if (values.headOption.nonEmpty) {
                  values.flatMap { v =>
                    List(
                      { () =>
                        out.print(s"${indent}${indent}")
                        valueRightHand(sd, v)
                        out.print(s": f(${inhabitant})")
                      }
                    )
                  }
                } else {
                  List(() =>
                    out.print(s"${indent}${indent}${nme}: f(${inhabitant})")
                  )
                }
              }

              case _ =>
                List.empty[() => Unit]
            }.zipWithIndex.foreach {
              case (print, i) =>
                if (i > 0) {
                  out.println(", ")
                }

                print()
            }

            out.println(s"""
${indent}}
}""")

            aliasing.foreach { base =>
              out.println(s"""
export function map${base}Values<T>(f: (_k: ${base}ValuesKey) => T): Readonly<Record<${base}ValuesKey, T>> {
${indent}return map${tpeName}Values<T>(f)${lineSep}
}""")

            }
          }

          out.print(s"""
export const ${tpeName}Types = {
""")

          val aliases = ps.collect {
            case pt @ SingletonTypeRef(_, _) =>
              val ptpeName = typeNaming(pt)
              val inhabitant = s"ns${ptpeName}.${ptpeName}Inhabitant"

              { () =>
                out.print(
                  s"${indent}${ptpeName}: ${inhabitant}"
                )
              }

          }

          aliases.zipWithIndex.foreach {
            case (print, i) =>
              if (i > 0) {
                out.println(", ")
              }

              print()
          }

          if (aliases.nonEmpty) {
            out.println()
          }

          out.println(s"""} as const${lineSep}

export const ${tpeName} = {
${indent}...${tpeName}Values,
${indent}...${tpeName}Types
} as const${lineSep}

export const idtlt${tpeName}KnownValues: ReadonlySet<${tpeName}> = new Set<${tpeName}>(Object.values(${tpeName}) as ReadonlyArray<${tpeName}>)${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return (""")

          out.print(pst.map { n =>
            s"${indent}${indent}ns${n}.is${n}(v)"
          } mkString " ||\n")

          out.println(s"""
${indent})${lineSep}
}""")

          aliasing.foreach { base =>
            out.println(s"""
export const idtlt${base}KnownValues: ReadonlySet<${base}> =
  idtlt${tpeName}KnownValues${lineSep}

export const ${base} = ${tpeName}${lineSep}""")
          }
        }

      case _: UnionDeclaration =>
        Some {
          out.println(s"// Not supported: UnionDeclaration '${name}'")
        }

      case decl @ TaggedDeclaration(id, field) =>
        Some {
          val member = Field(field.name)
          val tmapper = typeMapper(settings, decl, member, _: TypeRef)
          val tagged = tmapper(field.typeRef)

          val fieldTpe = Emitter.defaultTypeMapping(
            settings,
            member,
            field.typeRef,
            settings.typeNaming(settings, _),
            tr = tmapper
          )

          out.println(s"""// Validator for TaggedDeclaration ${tpeName}
export type ${tpeName} = ${fieldTpe} & { __tag: '${id}' }${lineSep}

export function ${tpeName}<T extends ${fieldTpe}>(${field.name}: T): ${tpeName} & T {
  return ${field.name} as (${tpeName} & T)${lineSep}
}

export const idtlt${tpeName} = ${tagged}.tagged<${tpeName}>()${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return idtlt${tpeName}.validate(v).ok${lineSep}
}""")
        }

      case decl @ EnumDeclaration(_, possibilities, values) =>
        Some {
          out.println(s"""// Validator for EnumDeclaration ${tpeName}
export const idtlt${tpeName} = idtlt.union(""")

          out.print(possibilities.map { v =>
            s"${indent}idtlt.literal('${v}')"
          } mkString ",\n")

          out.println(s""")

$deriving
$discrimitedDecl

export const idtlt${tpeName}Values: Array<${tpeName}> = [""")

          out.print(possibilities.map { v =>
            s"${indent}'${v}'"
          } mkString ",\n")
          out.println(s"""\n]${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent} return idtlt${tpeName}.validate(v).ok${lineSep}
}""")

          if (values.nonEmpty) {
            val sd = SingletonDeclaration(decl.name, values, None)

            out.println(s"""
class ${tpeName}Extra {""")

            values.toList.zipWithIndex.foreach {
              case (v, i) =>
                if (i > 0) {
                  out.println()
                }

                parent(ValueMemberDeclaration(sd, v), context, out)
            }

            out.println(s"""}

export ${tpeName}Invariants = new ${tpeName}Extra()${lineSep}""")
          }
        }

      case decl @ SingletonDeclaration(_, values, Some(superInterface)) =>
        Some {
          // Singleton as inhabitant of the superInterface

          out.print(s"""// Validator for SingletonDeclaration ${tpeName}
export const idtlt${tpeName} = """)

          values.headOption match {
            case Some(LiteralValue(_, _, single)) => {
              if (values.toList.drop(1).isEmpty) {
                out.println(s"idtlt.literal(${single})${lineSep}")
              } else {
                out.println("idtlt.object({")

                values.foreach {
                  case LiteralValue(nme, _, v) =>
                    out.println(s"${indent}${nme}: idtlt.literal(${v}),")

                  case v =>
                    out.println(s"${indent}/* Unsupported '${v.getClass.getSimpleName}': ${v.name} */")
                }

                out.println(s"})${lineSep}")
              }
            }

            case _ =>
              out.println(s"idtlt.literal('${name}')")

          }

          out.print(s"""
// Super-type declaration ${superInterface.name} is ignored""")

          out.print(s"""
export const idtltDiscriminated${tpeName} = idtlt${tpeName}${lineSep}

$deriving
export const ${tpeName}Inhabitant: ${tpeName} = """)

          // See UnionDeclaration ps.flatMap
          values.headOption match {
            case Some(LiteralValue(_, _, raw)) => {
              // See TypeMapper#IDTLT_TYPE_MAPPER_1
              val constDecl = decl.noSuperInterface

              if (values.size > 1) {
                out.print(s"{\n${indent}")

                values.zipWithIndex.foreach {
                  case (v, i) =>
                    if (i > 0) {
                      out.print(s",\n${indent}")
                    }

                    out.print(s"${v.name}: ")
                    valueRightHand(constDecl, v)
                }

                out.print("\n}")
              } else {
                out.print(raw)
              }
            }

            case _ =>
              out.print(s"'${name}'")
          }

          out.print(s"""${lineSep}

export function is${tpeName}(v: any): v is ${tpeName} {
${indent}return idtlt${tpeName}.validate(v).ok${lineSep}
}""")

          if (!context.contains("noSingletonAlias")) {
            // For compatiblity with Composite integration

            out.print(s"""

export type ${tpeName}Singleton = ${tpeName}${lineSep}""")
          }
        }

      case decl: SingletonDeclaration =>
        Some {
          parent(decl, context + ("noSingletonAlias" -> "true"), out)

          out.print(s"""
export const idtlt${tpeName} =
  idtlt.unknown.and(_unknown => idtlt.Err(
    'Cannot validator instance for singleton ${tpeName}'))${lineSep}
""")
        }

      case ValueBodyDeclaration(
            LiteralValue(_, tagged @ TaggedRef(_, _), v)
          ) =>
        Some {
          val n = typeNaming(tagged)

          out.print(s"ns${n}.${n}($v)")
        }

      case _ =>
        None
    }
  }

  // ---

  private def emitField(
      settings: Settings,
      fieldMapper: FieldMapper,
      typeMapper: TypeMapper.Resolved,
      o: PrintStream,
      owner: Declaration,
      member: Member
    ): Unit = {
    val tsField = fieldMapper(settings, owner.name, member.name, member.typeRef)

    o.println(
      s"${settings.indent}${tsField.name}: ${typeMapper(settings, owner, tsField, member.typeRef)},"
    )
  }
}
