# Generated by ScalaTS 0.7.0: https://scala-ts.github.io/scala-ts/

import typing  # noqa: F401
import datetime  # noqa: F401
import time  # noqa: F401

from generated import busline  # noqa: F401
from generated.busline import BusLine
from generated import trainline  # noqa: F401
from generated.trainline import TrainLine


# Declare union Transport
Transport = typing.Union[BusLine, TrainLine]

# Fields are ignored: name
