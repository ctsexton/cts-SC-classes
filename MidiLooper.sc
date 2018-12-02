MidiLooper {
  var <>data, <>state, routine, playbackFunction, timer;
  //
  *new { | playbackFunction |
    ^super.new.init(playbackFunction);
  }

  init { |pf|
    state = 'off';
    playbackFunction = pf;
    timer = Stopwatch();
    routine = Routine.new({
      data.do({ |timeAndMsg| 
        timeAndMsg[0].wait;
        playbackFunction.value(timeAndMsg[1]);
      });
    });
  }

  arm {
    data = List.new(0);
    state = 'armed';
  }

  receive { | msg |
    switch (state,
      'armed', { this.startRecording(msg)},
      'recording', {this.record(msg)},
      {}
    )
  }

  startRecording { |msg|
    timer.start();
    this.record(msg);
    state = 'recording';
  }

  record { |msg|
    var timestamp = timer.stop();
    timer.start();
    data.add([timestamp, msg]);
  }

  play { 
    routine.reset;
    routine.play;
  }

}
