MFT : Grid {
  var port, output, <>gridResponderFunction;
  *new {
    ^super.new(4, 4).init;
  }

  init {
    port = MIDIIn.findPort("Midi Fighter Twister", "Midi Fighter Twister MIDI 1");
    MIDIIn.connect(0, port);
    output = MIDIOut.newByName("Midi Fighter Twister", "Midi Fighter Twister MIDI 1");
    output.latency = 0;
    gridResponderFunction = {|x, y, v| [x, y, v].postln};
    this.setupMIDIdefs();
  }

  clear {
    16.do({|i| output.control(0, i, 0)});
  }

  ledset { |level, x, y, vel|
    var nn, velocity, channel;
    nn = this.gridToMidi(x, y);
    velocity = (vel * 127).asInteger;
    channel = switch (level, 0, 0, 1, 4, 2, 1);
    output.control(channel, nn, velocity);
    ^[nn, velocity];
  }

  setupMIDIdefs {
    MIDIdef.new(\mftInput, {
      |vel, nn, channel|
      var gridMsg = this.midiToGrid(nn, vel);
      var level = switch (channel, 0, 0, 4, 1, 1, 2);
      gridResponderFunction.value(level, gridMsg[0], gridMsg[1], gridMsg[2]);
    }, msgNum: (0..15), msgType: \control, srcID: port.uid);
  }

  calculateVelocity { |vel|
    ^case
      {vel > 64} {1}
      {vel < 64} {-1}
      {0}
  }
}
