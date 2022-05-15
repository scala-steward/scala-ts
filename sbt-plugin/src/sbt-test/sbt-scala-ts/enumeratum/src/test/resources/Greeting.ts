// Generated by ScalaTS 0.5.11-SNAPSHOT: https://scala-ts.github.io/scala-ts/

import * as nsBye from './Bye';
import { Bye, isBye } from './Bye';
import * as nsGoodBye from './GoodBye';
import { GoodBye, isGoodBye } from './GoodBye';
import * as nsHello from './Hello';
import { Hello, isHello } from './Hello';
import * as nsHi from './Hi';
import { Hi, isHi } from './Hi';
import * as nsWhatever from './Whatever';
import { Whatever, isWhatever } from './Whatever';

export type Greeting = Bye | GoodBye | Hello | Hi | Whatever;

export const Greeting = {
  Bye: nsBye.ByeInhabitant, 
  GoodBye: nsGoodBye.GoodByeInhabitant, 
  Hello: nsHello.HelloInhabitant, 
  Hi: nsHi.HiInhabitant
} as const;

export function isGreeting(v: any): v is Greeting {
  return (
    isBye(v) ||
    isGoodBye(v) ||
    isHello(v) ||
    isHi(v) ||
    isWhatever(v)
  );
}
