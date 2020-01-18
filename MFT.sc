MFT : Grid {
  var endpoint, output, <>gridResponderFunction, timers, <>onClick, <>onDoubleClick;
  *new { | selectedEndpoint |
    ^super.new(4, 4).init(selectedEndpoint);
  }

  init { | selectedEndpoint |
    endpoint = selectedEndpoint;
    output = MIDIOut.newByName(endpoint.device, endpoint.name);
    output.latency = 0;
    gridResponderFunction = {|x, y, v| [x, y, v].postln};
    this.setupMIDIdefs();
    timers = Array.fill(64, { TimerGate(0.25) });
  }

  clear {
    64.do({|i| output.control(0, i, 0)});
    64.do({|i| output.control(4, i, 0)});
  }

  ledset { |level, x, y, vel, page = 0|
    var nn, velocity, channel;
    nn = this.gridToMidi(x, y) + (page * 16);
    velocity = (vel * 127).asInteger;
    channel = switch (level, 0, 0, 1, 4, 2, 1);
    output.control(channel, nn, velocity);
    ^[nn, velocity];
  }

  setupMIDIdefs {
    MIDIdef.new(\mftInput, {
      |vel, nn, channel|
      var ev = this.midiToGrid(nn.mod(16), vel);
      ev.level = switch (channel, 0, 0, 4, 1, 1, 2);
      ev.nn = nn;
      ev.page = nn.div(16);
      gridResponderFunction.value(this, ev);
    }, chan: [0, 4], msgType: \control, srcID: endpoint.uid);
    MIDIdef.new(\mftButtons, {
      |vel, nn, channel|
      var ev = this.midiToGrid(nn.mod(16), vel);
      ev.nn = nn;
      ev.page = nn.div(16);
      onClick.value(this, ev);
      this.detectDoubleClick(ev);
    }, chan: 1, msgType: \control, srcID: endpoint.uid);
  }

  detectDoubleClick { |ev|
    if (ev.vel > 0, {
      switch (timers[ev.nn].getStatus,
        false, { timers[ev.nn].open },
        true, { onDoubleClick.value(this, ev) }
      )
    })
  }

  calculateVelocity { |vel|
    ^case
      {vel > 64} {1}
      {vel < 64} {-1}
      {0}
  }
}
