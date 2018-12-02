LaunchControl {
  var port, output, <>action, colors;
  *new { |action|
    ^super.new.init(action);
  }
  init { |initAction|
    port = MIDIIn.findPort("Launch Control", "Launch Control MIDI 1");
    MIDIIn.connect(0, port);
    output = MIDIOut.newByName("Launch Control", "Launch Control MIDI 1");
    output.latency = 0;
    colors = (yellow: 127, red: 3, green: 48, off: 0);
    action = initAction;
    this.setupMIDIdefs();
  }

  ledset { |section, padNum, color|
    switch (section,
      'c', {output.noteOn(0, this.padRowNumToMidi(padNum), colors[color])},
      'd', {output.control(0, (padNum + 114), colors[color])}
    );
  }

  padRowNumToMidi { |num|
    ^switch (num,
      0, 9,
      1, 10,
      2, 11,
      3, 12,
      4, 25,
      5, 26,
      6, 27,
      7, 28,
    )
  }

  midiToPadRowNum { |num|
    ^switch (num,
      9, 0,
      10, 1,
      11, 2,
      12, 3,
      25, 4,
      26, 5,
      27, 6,
      28, 7
    );
  }
  setupMIDIdefs {
    MIDIdef.noteOn(\launchControl_pads_row_ON, {
      |vel, nn, chan|
      action.value(this, 'c', this.midiToPadRowNum(nn), 1);
    }, srcID: port.uid);
    MIDIdef.noteOff(\launchControl_pads_row_OFF, {
      |vel, nn, chan|
      action.value(this, 'c', this.midiToPadRowNum(nn), 0);
    }, srcID: port.uid);
    MIDIdef.new(\launchControl_knobs, {
      |vel, nn, chan|
      var knob, row, isSecondRow;
      isSecondRow = nn > 31;
      # row, knob = switch (isSecondRow, 
        false, {['a', nn - 21]},
        true, {['b', nn - 41]}
      );
      action.value(this, row, knob, vel);
    }, msgNum: (21..48), srcID: port.uid, msgType: \control);
    MIDIdef.new(\launchControl_additional, {
      |vel, nn, chan|
      action.value(this, 'd', nn - 114, (vel > 0).asInteger);
    }, msgNum: (114..117), srcID: port.uid, msgType: \control)
  }
}
