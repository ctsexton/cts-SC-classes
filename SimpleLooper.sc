SimpleLooper {
  var <>data, <>state, routine, <>playbackFunction, timer;
  //
  *new { | playbackFunction, postSetState |
    ^super.new.init(playbackFunction, postSetState);
  }

  init { |argA, argB|
    state = Parameter(0, 'off', argB, {});
    playbackFunction = argA;
    timer = Stopwatch();
    routine = Routine.new({
      loop {
        data.do({ |timeAndMsg| 
          timeAndMsg[0].wait;
          switch (timeAndMsg[1], 
            'end_loop', {},
            { playbackFunction.value(timeAndMsg[1]) }
          )
        });
      }
    });
  }

  reset {
    this.stop;
    state.value = 'off';
  }

  arm {
    data = List.new(0);
    state.value = 'armed';
  }

  receive { | msg |
    switch (state.value,
      'armed', { this.startRecording(msg)},
      'recording', {this.record(msg)},
      {}
    )
  }

  startRecording { |msg|
    timer.start();
    this.record(msg);
    state.value = 'recording';
  }

  record { |msg|
    var timestamp = timer.stop();
    timer.start();
    data.add([timestamp, msg]);
  }

  engage { 
    var timestamp = timer.stop();
    data.add([timestamp, 'end_loop']);
    this.play();
  }

  stop {
    routine.stop();
    state.value = 'stopped';
  }

  play {
    routine.reset();
    routine.play();
    state.value = 'playing';
  }

  switchToNextState {
    switch (state.value,
      'off', { this.arm() },
      'recording', { this.engage() },
      'playing', { this.stop() },
      'stopped', { this.play() },
      {}
    )
  }

}
