// Generated by ScalaTS 0.7.0: https://scala-ts.github.io/scala-ts/
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

// Validator for UnionDeclaration StateUnion
export const idtltStateUnion = idtlt.union(
  nsAlabama.idtltDiscriminatedAlabama,
  nsAlaska.idtltDiscriminatedAlaska);

// Deriving TypeScript type from StateUnion validator
export type StateUnion = typeof idtltStateUnion.T;

export const idtltDiscriminatedStateUnion = idtlt.intersection(
  idtltStateUnion,
  idtlt.object({
    _type: idtlt.literal('StateUnion')
  })
);

// Deriving TypeScript type from idtltDiscriminatedStateUnion validator
export type DiscriminatedStateUnion = typeof idtltDiscriminatedStateUnion.T;

export const StateUnionValues = {
  "AL": nsAlabama.AlabamaInhabitant, 
  "AK": nsAlaska.AlaskaInhabitant
} as const;

export type StateUnionValuesKey = keyof typeof StateUnionValues;

// Aliases for the Union utilities
export const StateValues = StateUnionValues;

export type StateValuesKey = StateUnionValuesKey;

export function mapStateUnionValues<T>(f: (_k: StateUnionValuesKey) => T): Readonly<Record<StateUnionValuesKey, T>> {
  return {
    "AL": f(nsAlabama.AlabamaInhabitant), 
    "AK": f(nsAlaska.AlaskaInhabitant)
  }
}

export function mapStateValues<T>(f: (_k: StateValuesKey) => T): Readonly<Record<StateValuesKey, T>> {
  return mapStateUnionValues<T>(f);
}

export const StateUnionTypes = {
  Alabama: nsAlabama.AlabamaInhabitant, 
  Alaska: nsAlaska.AlaskaInhabitant
} as const;

export const StateUnion = {
  ...StateUnionValues,
  ...StateUnionTypes
} as const;

export const idtltStateUnionKnownValues: ReadonlySet<StateUnion> = new Set<StateUnion>(Object.values(StateUnion) as ReadonlyArray<StateUnion>);

export function isStateUnion(v: any): v is StateUnion {
  return (
    nsAlabama.isAlabama(v) ||
    nsAlaska.isAlaska(v)
  );
}

export const idtltStateKnownValues: ReadonlySet<State> =
  idtltStateUnionKnownValues;

export const State = StateUnion;

export class StateSingleton {
  public readonly Alabama: nsAlabama.AlabamaSingleton = nsAlabama.AlabamaInhabitant;

  public readonly Alaska: nsAlaska.AlaskaSingleton = nsAlaska.AlaskaInhabitant;

  public readonly cities: Readonly<Map<nsState.State, ReadonlySet<string>>> = (() => { const __buf1360151735: Map<nsState.State, ReadonlySet<string>> = new Map(); __buf1360151735.set(this.Alaska, new Set([ "Anchorage", "Juneau" ])); __buf1360151735.set(this.Alabama, new Set([ "Birmingham" ])); return __buf1360151735 })();

  private static instance: StateSingleton;

  private constructor() {}

  public static getInstance() {
    if (!StateSingleton.instance) {
      StateSingleton.instance = new StateSingleton();
    }

    return StateSingleton.instance;
  }
}

export const StateSingletonInhabitant: StateSingleton = StateSingleton.getInstance();

export function isStateSingleton(v: any): v is StateSingleton {
  return (v instanceof StateSingleton) && (v === StateSingletonInhabitant);
}

export const idtltStateSingleton =
  idtlt.unknown.and(_unknown => idtlt.Err(
    'Cannot validator instance for singleton StateSingleton'));

export const StateInhabitant = StateSingletonInhabitant;


// Validator for CompositeDeclaration State
export const idtltState = idtltStateUnion;

export function isState(v: any): v is State {
  return isStateUnion(v);
}

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

// Workaround for local type references in the same module
type privateState = State;

namespace nsState {
  export type State = privateState;
}
