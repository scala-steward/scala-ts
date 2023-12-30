// Generated by ScalaTS 0.5.18: https://scala-ts.github.io/scala-ts/

declare var exports: any;

export const nsIpsum = exports;

import * as nsCategory from './Category';
import type { Category } from './Category';

export const dependencyModules = [
  nsCategory,
];

export class Ipsum implements Category {
  private static instance: Ipsum;

  private constructor() {}

  public static getInstance() {
    if (!Ipsum.instance) {
      Ipsum.instance = new Ipsum();
    }

    return Ipsum.instance;
  }
}

export const IpsumInhabitant: Ipsum = Ipsum.getInstance();

export function isIpsum(v: any): v is Ipsum {
  return (v instanceof Ipsum) && (v === IpsumInhabitant);
}
