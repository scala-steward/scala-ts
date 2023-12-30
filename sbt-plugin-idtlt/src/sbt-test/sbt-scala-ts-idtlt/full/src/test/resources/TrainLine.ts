// Generated by ScalaTS 0.5.18: https://scala-ts.github.io/scala-ts/
import * as idtlt from 'idonttrustlikethat';
export const _externalDependencyModules = [idtlt];

declare var exports: any;

export const nsTrainLine = exports;

// Validator for InterfaceDeclaration TrainLine
export const idtltTrainLine = idtlt.object({
  name: idtlt.string,
  startStationId: idtlt.string,
  endStationId: idtlt.string,
});

// Super-type declaration Transport is ignored

// Deriving TypeScript type from TrainLine validator
export type TrainLine = typeof idtltTrainLine.T;

export const idtltDiscriminatedTrainLine = idtlt.intersection(
  idtltTrainLine,
  idtlt.object({
    _type: idtlt.literal('TrainLine')
  })
);

// Deriving TypeScript type from idtltDiscriminatedTrainLine validator
export type DiscriminatedTrainLine = typeof idtltDiscriminatedTrainLine.T;

export const discriminatedTrainLine: (_: TrainLine) => DiscriminatedTrainLine = (v: TrainLine) => ({ _type: 'TrainLine', ...v });

export function isTrainLine(v: any): v is TrainLine {
  return (
    ((typeof v['name']) === 'string') &&
    ((typeof v['startStationId']) === 'string') &&
    ((typeof v['endStationId']) === 'string')
  );
}
