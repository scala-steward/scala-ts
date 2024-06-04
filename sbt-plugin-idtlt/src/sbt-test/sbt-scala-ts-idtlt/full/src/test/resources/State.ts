// Generated by ScalaTS 0.5.20-SNAPSHOT: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';
export const _externalDependencyModules = [idtlt];

declare var exports: any;

export const nsState = exports;

import * as nsAlabama from './Alabama';
import * as nsAlaska from './Alaska';

export const dependencyModules = [
  nsAlabama,
  nsAlaska,
];

// Validator for UnionDeclaration State
export const idtltState = idtlt.union(
  nsAlabama.idtltDiscriminatedAlabama,
  nsAlaska.idtltDiscriminatedAlaska);

// Deriving TypeScript type from State validator
export type State = typeof idtltState.T;

export const idtltDiscriminatedState = idtlt.intersection(
  idtltState,
  idtlt.object({
    _type: idtlt.literal('State')
  })
);

// Deriving TypeScript type from idtltDiscriminatedState validator
export type DiscriminatedState = typeof idtltDiscriminatedState.T;

export const StateValues = {
  "AL": nsAlabama.AlabamaInhabitant, 
  "AK": nsAlaska.AlaskaInhabitant
} as const;

export type StateValuesKey = keyof typeof StateValues;

export function mapStateValues<T>(f: (_k: StateValuesKey) => T): Readonly<Record<StateValuesKey, T>> {
  return {
    "AL": f(nsAlabama.AlabamaInhabitant), 
    "AK": f(nsAlaska.AlaskaInhabitant)
  }
}

export const StateTypes = {
  Alabama: nsAlabama.AlabamaInhabitant, 
  Alaska: nsAlaska.AlaskaInhabitant
} as const;

export const State = {
  ...StateValues,
  ...StateTypes
} as const;

export const idtltStateKnownValues: ReadonlySet<State> = new Set<State>(Object.values(State) as ReadonlyArray<State>);

export function isState(v: any): v is State {
  return (
    nsAlabama.isAlabama(v) ||
    nsAlaska.isAlaska(v)
  );
}
