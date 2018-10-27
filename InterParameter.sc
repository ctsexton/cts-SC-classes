InterParameter : Parameter {
  var interpolationFunction;
  *new {|initialValue, minval, maxval, incrementSize, interpF|
    ^super.newCopyArgs(initialValue, minval, maxval, incrementSize, interpF);
  }

  interpolate { |fromValue, toValue, position|
    var newValue = interpolationFunction.value(fromValue, toValue, position);
    this.value_(newValue);
    ^this.value;
  }
}
