PresetTransitioner {
  var <storage, transitionTime, transition;

  *new { |newStorage|
    ^super.new.init(newStorage)
  }

  init { |newStorage|
    storage = newStorage;
    transitionTime = Parameter.new(1, 0, 5, 0.1);
  }

  retrieve { |presetNum|
    ^storage.restore(presetNum);
  }

  retrieveMany { |presetNums|
    ^presetNums.collect({ |item| storage.restore(item) });
  }

  basicTransition { |from, to, transmit|
    var steps = transitionTime.value() / 0.05;
    if (steps < 1,
      {this.retrieve(to)},
      {
        transition.stop;
        transition = Routine({
          steps.do({ |i|
            var position, presets;
            presets = this.retrieveMany([from, to]);
            position = (i / steps + (1 / steps));
            transmit.value([presets[0], presets[1], position]);
            0.05.wait;
          })
        }).play;
      }
    )
  }
}
