Parameter {
  var <name, initialValue, currentValue, postSetAction, interpolationFunction;

  *new { |name, initialValue, action, interpolationFunction|
    ^super.newCopyArgs(name, initialValue, initialValue, action, interpolationFunction);
  }

  reset {
    currentValue = initialValue;
    ^currentValue;
  }

  value {
    ^currentValue;
  }

  value_ { | newValue |
    currentValue = newValue;
    postSetAction.value(name, currentValue);
    ^currentValue;
  }

  interpolate { |fromValue, toValue, position|
    var newValue = interpolationFunction.value(fromValue, toValue, position);
    this.value_(newValue);
    ^this.value;
  }
}
