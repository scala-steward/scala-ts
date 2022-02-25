// Generated by ScalaTS 0.5.9-SNAPSHOT: https://scala-ts.github.io/scala-ts/

import { Category, isCategory } from './Category';

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
