Subscriber {
  var <>action;

  *new { |action|
    ^super.newCopyArgs(action);
  }

  update { |theChanged, event|
    action.value(theChanged, event);
  }

  subscribe { |topic|
    topic.addDependant(this);
  }
}
