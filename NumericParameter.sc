NumericParameter : Parameter {
  var minval, maxval, <incrementSize;

  *new { |initialValue, min, max, incrementSize, action, interpolationFunction|
    ^super.newCopyArgs(initialValue, action, interpolationFunction, min, max, incrementSize);
  }

  value_ { | newValue |
    currentValue = newValue.clip(minval, maxval);
    postSetAction.value(currentValue);
    ^currentValue;
  }

  increment { | step |
    this.value_((currentValue + (step * incrementSize)).round(incrementSize));
    ^currentValue;
  }
}
