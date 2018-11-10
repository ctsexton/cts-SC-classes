Parameter {
  var currentValue, postSetAction, interpolationFunction;

  *new { |initialValue, action, interpolationFunction|
    ^super.newCopyArgs(initialValue, action, interpolationFunction);
  }

  value {
    ^currentValue;
  }

  value_ { | newValue |
    currentValue = newValue;
    postSetAction.value(currentValue);
    ^currentValue;
  }

  interpolate { |fromValue, toValue, position|
    var newValue = interpolationFunction.value(fromValue, toValue, position);
    this.value_(newValue);
    ^this.value;
  }
}
