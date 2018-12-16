NumericParameter : Parameter {
  var minval, maxval, <incrementSize;

  *new { |name, initialValue, min, max, incrementSize, action, interpolationFunction|
    ^super.newCopyArgs(name, initialValue, initialValue, action, interpolationFunction, min, max, incrementSize);
  }

  value_ { | newValue |
    currentValue = newValue.clip(minval, maxval);
    postSetAction.value(name, currentValue);
    ^currentValue;
  }

  increment { | step |
    this.value_((currentValue + (step * incrementSize)).round(incrementSize));
    ^currentValue;
  }

  negate { 
    this.value_(currentValue * -1);
    ^currentValue;
  }
}
