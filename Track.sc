Track {
  var trackIndex, initialSample, trackOutputBus, <>parameters, <bufferIndexKr, synth, synthPhasor;

  *new { |trackIndex, initialSample, trackOutputBus|
    ^super.new.init(trackIndex, initialSample, trackOutputBus);
  }

  init { |argA, argB, argC|
    var synthUpdateNumeric, synthUpdateBuffer, interpolateNumeric, interpolateBuffer;

    trackIndex = argA;
    initialSample = argB;
    trackOutputBus = argC;
    bufferIndexKr = Bus.control();
    bufferIndexKr.set(0);
    synthPhasor = Bus.audio();

    synthUpdateNumeric = { |name, newValue|
      synth.set(name, newValue);
    };

    synthUpdateBuffer = { |name, newValue|
      synth.set('sample', newValue.buffer);
    };

    interpolateNumeric = { |fromVal, toVal, position|
      (fromVal * (1 - position)) + (toVal * position);
    };

    interpolateBuffer = { |fromVal, toVal, position|
      case 
        { position >= 0.5 } {toVal}
        { position < 0.5 } {fromVal};
    };

    parameters = (
      position: NumericParameter('position', 0, 0, 0.999, 0.01, synthUpdateNumeric, interpolateNumeric),
      window: NumericParameter('window', 1, 0.001, 1, 0.01, synthUpdateNumeric, interpolateNumeric),
      rate: NumericParameter('rate', 1, -2, 2, 0.01, synthUpdateNumeric, interpolateNumeric),
      lpfCutoff: NumericParameter('lpfCutoff', 1, 0, 1, 0.005, synthUpdateNumeric, interpolateNumeric),
      hpfCutoff: NumericParameter('hpfCutoff', 0, 0, 1, 0.005, synthUpdateNumeric, interpolateNumeric),
      volume: NumericParameter('volume', 0, 0, 1, 0.01, synthUpdateNumeric, interpolateNumeric),
      sample: Parameter('sample', initialSample, synthUpdateBuffer, interpolateBuffer)
    );
  }

  start {
    synth = Synth.new(\windowLooper, [
      \feedback: synthPhasor,
      \sample: parameters.sample.value.buffer,
      \rate: parameters.rate.value,
      \window: parameters.window.value,
      \position: parameters.position.value,
      \playhead: bufferIndexKr,
      \volume: parameters.volume.value,
      \lpfCutoff: parameters.lpfCutoff.value,
      \hpfCutoff: parameters.hpfCutoff.value,
      \status: 0,
      \outputBus: trackOutputBus
    ]);
  }

  stop {
    synth.set(\status, 1);
  }

  makeMemento {
    ^parameters.collect({ |parameter| parameter.value});
  }
}
