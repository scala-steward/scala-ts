// Generated by ScalaTS 0.5.11-SNAPSHOT: https://scala-ts.github.io/scala-ts/

import * as nsFeature from './Feature';
import { Feature, isFeature } from './Feature';

export class Bar implements Feature {
  private static instance: Bar;

  private constructor() {}

  public static getInstance() {
    if (!Bar.instance) {
      Bar.instance = new Bar();
    }

    return Bar.instance;
  }
}

export const BarInhabitant: Bar = Bar.getInstance();

export function isBar(v: any): v is Bar {
  return (v instanceof Bar) && (v === BarInhabitant);
}
