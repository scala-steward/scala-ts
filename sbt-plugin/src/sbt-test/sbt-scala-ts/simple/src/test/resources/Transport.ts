// Generated by ScalaTS 0.5.8-SNAPSHOT: https://scala-ts.github.io/scala-ts/

export interface Transport {
  name: string;
}

export function isTransport(v: any): v is Transport {
  return (
    ((typeof v['name']) === 'string')
  );
}
