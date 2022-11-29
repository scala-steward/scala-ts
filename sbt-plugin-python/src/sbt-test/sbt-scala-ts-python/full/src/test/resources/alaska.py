# Generated by ScalaTS 0.5.16-SNAPSHOT: https://scala-ts.github.io/scala-ts/

from dataclasses import dataclass  # noqa: F401
import typing  # noqa: F401
import datetime  # noqa: F401
import time  # noqa: F401


# Declare singleton Alaska
Alaska = typing.Literal["AK"]
AlaskaInhabitant: Alaska = "AK"


class AlaskaInvariantsFactory:
    @classmethod
    def entryName(self) -> str:
        return "AK"


@dataclass
class IAlaskaInvariants:
    entryName: str


AlaskaInvariants = IAlaskaInvariants(
    entryName=AlaskaInvariantsFactory.entryName(),
)
