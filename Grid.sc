Grid {
  var rows, columns;
  *new { |rows, columns|
    ^super.newCopyArgs(rows, columns);
  }

  midiToGrid { |nn, vel|
    var x, y, velocity;
    x = nn.mod(columns);
    y = nn.div(rows);
    velocity = this.calculateVelocity(vel);
    ^(x: x, y: y, vel: velocity);
  }

  gridToMidi { |x, y|
    ^y * rows + x;
  }

  calculateVelocity { |vel|
    ^vel / 127;
  }
}
