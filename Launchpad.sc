Launchpad : Grid {
  var endpoint, output, colors, <>gridResponder, <>topResponder, <>sideResponder;
  *new { |selectedEndpoint|
    ^super.new(16, 16).init(selectedEndpoint);
  }

  init { |selectedEndpoint|
    endpoint = selectedEndpoint;
    output = MIDIOut.newByName(endpoint.device, endpoint.name);
    output.latency = 0;
    colors = (yellow: 127, red: 3, green: 48, off: 0);
    gridResponder = {|ev| ev.postln};
    topResponder = {|ev| ev.postln};
    sideResponder = {|ev| ev.postln};
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
    output.noteOn(0, this.gridToMidi(x, y), colors[color])
  }

  lightTop { |x, color|
    output.control(0, x + 104, colors[color]);
  }

  lightSide { |index, color|
    var nn = [8, 24, 40, 56, 72, 88, 104, 120];
    output.noteOn(0, nn[index], colors[color]);
  }

  setupMIDIdefs {
    var gridNotes = difference((0..128), [8, 24, 40, 56, 72, 88, 104, 120]);
    var sideIndex = (
      8: 0,
      24: 1,
      40: 2,
      56: 3,
      72: 4,
      88: 5,
      104: 6,
      120: 7
    );

    MIDIdef.noteOn(\lpGridOn, { |vel, nn, chann|
      var msg, index;
      msg = this.midiToGrid(nn, vel);
      msg.vel = 1;
      msg.nn = 8 * msg.y + msg.x;
      msg.section = \grid;
      gridResponder.value(msg);
    }, noteNum: gridNotes, srcID: endpoint.uid);
    MIDIdef.noteOff(\lpGridOff, { |vel, nn, chann|
      var msg, index;
      msg = this.midiToGrid(nn, vel);
      msg.vel = 0;
      msg.nn = 8 * msg.y + msg.x;
      msg.section = \grid;
      gridResponder.value(msg);
    }, noteNum: gridNotes, srcID: endpoint.uid);
    MIDIdef.new(\lpTop, { |vel, nn, channel|
      var onoff = (vel > 0).asInteger;
      var msg = (
        index: nn - 104,
        vel: onoff,
        section: \top
      );
      topResponder.value(msg);
    }, msgType: \control, srcID: endpoint.uid);
    MIDIdef.noteOn(\lpSideOn, { |vel, nn, channel|
      var msg = (
        index: sideIndex[nn],
        vel: 1,
        section: \side
      );
      sideResponder.value(msg);
    }, noteNum: [8, 24, 40, 56, 72, 88, 104, 120], srcID: endpoint.uid);
    MIDIdef.noteOff(\lpSideOff, { |vel, nn, channel|
      var msg = (
        index: sideIndex[nn],
        vel: 0,
        section: \side
      );
      sideResponder.value(msg);
    }, noteNum: [8, 24, 40, 56, 72, 88, 104, 120], srcID: endpoint.uid);
  }
}
