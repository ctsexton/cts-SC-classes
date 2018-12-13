BiDirectionalRouter {
  var source, destination, transmitFunction, receiveFunction;
  // for lack of better names. 
  // source calls transmit on the router to send values to destination
  // destination can send values back to source by calling receive

  *new { |source, destination, transmitFunction, receiveFunction|
    ^super.newCopyArgs(source, destination, transmitFunction, receiveFunction);
  }
  
  transmit { |input|
    transmitFunction.value(input, destination);
  }

  receive { |output|
    receiveFunction.value(output, source);
  }
}
