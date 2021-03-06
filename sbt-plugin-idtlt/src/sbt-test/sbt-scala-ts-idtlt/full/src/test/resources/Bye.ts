// Generated by ScalaTS 0.5.8-SNAPSHOT: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';

// Validator for SingletonDeclaration Bye
export const idtltBye = idtlt.literal('Bye')

// Super-type declaration Greeting is ignored
export const idtltDiscriminatedBye = idtltBye;

// Deriving TypeScript type from Bye validator
export type Bye = typeof idtltBye.T;

export const ByeInhabitant: Bye = 'Bye';

export function isBye(v: any): v is Bye {
  return idtltBye.validate(v).ok;
}