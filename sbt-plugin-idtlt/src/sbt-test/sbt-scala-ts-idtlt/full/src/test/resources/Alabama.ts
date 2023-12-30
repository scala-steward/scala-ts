// Generated by ScalaTS 0.5.19-SNAPSHOT: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';
export const _externalDependencyModules = [idtlt];

declare var exports: any;

export const nsAlabama = exports;

// Validator for SingletonDeclaration Alabama
export const idtltAlabama = idtlt.literal("AL");

// Super-type declaration State is ignored
export const idtltDiscriminatedAlabama = idtltAlabama;

// Deriving TypeScript type from Alabama validator
export type Alabama = typeof idtltAlabama.T;

export const AlabamaInhabitant: Alabama = "AL";

export function isAlabama(v: any): v is Alabama {
  return idtltAlabama.validate(v).ok;
}
