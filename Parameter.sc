Parameter {
  var currentValue, minval, maxval, <incrementSize;

  *new { |initialValue, min, max, incrementSize|
    ^super.newCopyArgs(initialValue, min, max, incrementSize);
  }

  value {
    ^currentValue;
  }

  value_ { | newValue |
    currentValue = newValue.clip(minval, maxval);
    ^currentValue;
  }

  increment { | step |
    this.value_((currentValue + (step * incrementSize)).round(incrementSize));
    ^currentValue;
  }
}
