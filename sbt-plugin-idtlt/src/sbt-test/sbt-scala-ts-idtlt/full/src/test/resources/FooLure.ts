// Generated by ScalaTS 0.5.8-SNAPSHOT: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';

// Validator for SingletonDeclaration FooLure
export const idtltFooLure = idtlt.literal('FooLure')

// Super-type declaration Feature is ignored
export const idtltDiscriminatedFooLure = idtltFooLure;

// Deriving TypeScript type from FooLure validator
export type FooLure = typeof idtltFooLure.T;

export const FooLureInhabitant: FooLure = 'FooLure';

export function isFooLure(v: any): v is FooLure {
  return idtltFooLure.validate(v).ok;
}