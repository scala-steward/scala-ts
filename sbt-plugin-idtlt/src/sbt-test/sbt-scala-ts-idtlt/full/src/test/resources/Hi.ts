// Generated by ScalaTS 0.5.18: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';
export const _externalDependencyModules = [idtlt];

declare var exports: any;

export const nsHi = exports;

// Validator for SingletonDeclaration Hi
export const idtltHi = idtlt.literal('Hi')

// Super-type declaration Greeting is ignored
export const idtltDiscriminatedHi = idtltHi;

// Deriving TypeScript type from Hi validator
export type Hi = typeof idtltHi.T;

export const HiInhabitant: Hi = 'Hi';

export function isHi(v: any): v is Hi {
  return idtltHi.validate(v).ok;
}
