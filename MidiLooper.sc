MidiLooper {
  var data, state, routine, playback;
  //
  *new { | playbackFunction |
    ^super.new.init(playbackFunction);
  }

  init { |playbackFunction|
    state = 'off';
    routine = Routine.new({ |data|
      data.do({ |msg| 
        playbackFunction.value(msg);
        1.yield;
      });
    })
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
    data.add(msg);
    state = 'recording';
  }

  record { |msg|
    data.add(msg);
  }

  play { 
    playback = ();
  }

}
