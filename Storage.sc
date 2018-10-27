Storage {
  var store;
  *new { |maxSize|
    ^super.new.init(maxSize);
  }
  init { |maxSize|
    store = Array.newClear(maxSize);
  }

  save { |index, newObject|
    store[index] = newObject;
  }

  restore { |index|
    ^store[index];
  }
}
