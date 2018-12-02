Stopwatch {
  var started, startTime, endTime;
  *new {
    ^super.new()
  }

  start {
    startTime = Date.getDate.rawSeconds;
    ^startTime;
  }

  stop {
    endTime = Date.getDate.rawSeconds;
    ^endTime - startTime;
  }
}
