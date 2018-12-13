TrackMediator {
  var track, recallEnabled, <>userInterfaces, <>storage;
  *new { |track|
    ^super.newCopyArgs(track).init;
  }

  init {
    userInterfaces = [];
  }

  playheadPositionBus {
    ^track.bufferIndexKr;
  }

  getParameter { |parameter|
    ^track.parameters[parameter].value;
  }

  setParameter { |parameterName, newValue|
    var operation = { |parameter| parameter.value = newValue };
    ^this.operateOnParameterAndUpdateUIs(parameterName, operation);
  }

  incrementParameter { |parameterName, direction|
    var operation = { |parameter| parameter.increment(direction) };
    ^this.operateOnParameterAndUpdateUIs(parameterName, operation);
  }

  negateParameter { |parameterName|
    var operation = { |parameter| parameter.negate() };
    ^this.operateOnParameterAndUpdateUIs(parameterName, operation);
  }

  resetParameter { |parameterName|
    var operation = { |parameter| parameter.reset()};
    ^this.operateOnParameterAndUpdateUIs(parameterName, operation);
  }

  operateOnParameterAndUpdateUIs { |parameterName, operation|
    var parameter = track.parameters[parameterName];
    operation.value(parameter);
    this.updateAllUIs(parameterName, parameter.value);
    ^parameter.value;
  }

  updateAllUIs { |parameterName, value|
    userInterfaces.do({ |item|
      item.receive(parameterName, value);
    });
  }

  togglePlayback { |on|
    if (on, {track.start()}, {track.stop()});
  }

  saveSettings { |index|
    storage.save(index, track.makeMemento());
    ^index;
  }

  restoreSettings { |index|
    var settings = storage.restore(index);
    settings.keysValuesDo({ |parameterName, value|
      this.setParameter(parameterName, value);
    });
  }

  toggleRecall {
    storage.recallEnabled = storage.recallEnabled.not;
    ^storage.recallEnabled;
  }
}
