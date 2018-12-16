TimerGate {
  var status, gate;

  *new { |timelimit|
    ^super.new.init(timelimit)
  }

  init { |timelimit|
    status = false;
    gate = Routine.new({
      status = true;
      timelimit.wait;
      status = false;
    })
  }

  open {
    gate.reset.play;
  }

  close {
    gate.stop;
  }

  getStatus {
    ^status;
  }

  throttle { |function|
    switch (status,
      false, {function.value; gate.reset.play}
    )
  }

  fireIfOpen { |function|
    switch (status,
      true, {function.value; gate.stop}
    )
  }

  fireIfClosed { |function|
    switch (status,
      false, {function.value}
    )
  }
}
