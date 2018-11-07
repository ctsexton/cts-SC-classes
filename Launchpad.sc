Launchpad : Grid {
  var port, output, colors;
  *new {
    ^super.new(16, 16).init;
  }

  init {
    //port = MIDIIn.findPort("Launchpad Mini", "Launchpad Mini MIDI 1");
    //MIDIIn.connect(0, port);
    //output = MIDIOut.newByName("Launchpad Mini", "Launchpad Mini MIDI 1");
    //output.latency = 0;
    colors = (yellow: 127, red: 3, green: 48, off: 0);
    "INITIATE".postln;
  }

  getColor { |color|
    ^colors[color];
  }


}
