MasterTrack {
  var <>parameters, synth, inputBus;

  *new { |masterInputBus|
    ^super.new.init(masterInputBus);
  }
  init { |masterInputBus|
    var synthUpdateNumeric = { |name, newValue|
      synth.set(name, newValue);
    };
    inputBus = masterInputBus;
    parameters = (
      reverb: NumericParameter('reverb', 0, 0, 1, 0.01, synthUpdateNumeric),
      lpf: NumericParameter('lpf', 1, 0, 1, 0.005, synthUpdateNumeric),
      hpf: NumericParameter('hpf', 0.001, 0, 1, 0.005, synthUpdateNumeric),
      distortion: NumericParameter('distortion', 0, 0, 1, 0.01, synthUpdateNumeric),
      volume: NumericParameter('volume', 0, 0, 1, 0.001, synthUpdateNumeric)
    );
    this.start;
  }

  start {
    synth = Synth.new(\master, [
      \masterBus: inputBus,
      \reverb: parameters.reverb.value,
      \lpf: parameters.lpf.value,
      \hpf: parameters.hpf.value,
      \distortion: parameters.distortion.value,
      \volume: parameters.volume.value
    ]);
  }

  stop {
    synth.free;
  }
}
