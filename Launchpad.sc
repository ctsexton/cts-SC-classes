Launchpad : Grid {
  var port, output, colors, <>gridResponderFunction;
  *new {
    ^super.new(16, 16).init;
  }

  init {
    port = MIDIIn.findPort("Launchpad Mini", "Launchpad Mini MIDI 1");
    MIDIIn.connect(0, port);
    output = MIDIOut.newByName("Launchpad Mini", "Launchpad Mini MIDI 1");
    output.latency = 0;
    colors = (yellow: 127, red: 3, green: 48, off: 0);
    gridResponderFunction = {|x, y, v| [x, y, v].postln};
    this.setupMIDIdefs();
  }

  clear {
    127.do({|i| output.noteOff(0, i, 0)});
    (104..(104 + 7)).do({ |i| output.control(0, i, colors['off'])});
  }

  ledset { |section, argA, argB, argC|
    switch (section,
      'grid', {this.lightGrid(argA, argB, argC)},
      'top', {this.lightTop(argA, argB)},
      'side', {this.lightSide(argA, argB)}
    );
  }

  lightGrid { |x, y, color|
    var nn, velocity;
    nn = this.gridToMidi(x, y);
    velocity = colors[color];
    output.noteOn(0, nn, velocity);
    ^True;
  }

  lightTop { |x, color|
    output.control(0, x + 104, colors[color]);
  }

  lightSide {}

  setupMIDIdefs {
    MIDIdef.noteOn(\gridNoteOn, { |vel, nn, chann|
      var gridMsg, index;
      gridMsg = this.midiToGrid(nn, vel);
      gridResponderFunction.value('grid', [gridMsg[0], gridMsg[1]], gridMsg[2].asInteger);
    }, srcID: port.uid);
    MIDIdef.noteOff(\gridNoteOff, { |vel, nn, chann|
      var gridMsg, index;
      gridMsg = this.midiToGrid(nn, vel);
      gridResponderFunction.value('grid', [gridMsg[0], gridMsg[1]], gridMsg[2].asInteger);
    }, srcID: port.uid);
    MIDIdef.new(\topRow, { |vel, nn, channel|
      var index = nn - 104;
      var onoff = switch (vel, 0, 0, 127, 1);
      gridResponderFunction.value('top', index, onoff);
    }, msgType: \control, srcID: port.uid);
  }
}
