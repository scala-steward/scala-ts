// Generated by ScalaTS 0.7.0: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';
export const _externalDependencyModules = [idtlt];

declare var exports: any;

export const nsAlaska = exports;

// Validator for SingletonDeclaration Alaska
export const idtltAlaska = idtlt.literal("AK");

// Super-type declaration State is ignored
export const idtltDiscriminatedAlaska = idtltAlaska;

// Deriving TypeScript type from Alaska validator
export type Alaska = typeof idtltAlaska.T;

export const AlaskaInhabitant: Alaska = "AK";

export function isAlaska(v: any): v is Alaska {
  return idtltAlaska.validate(v).ok;
}

export type AlaskaSingleton = Alaska;