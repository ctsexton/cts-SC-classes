Topic {
  var <title;

  *new { |title|
    ^super.newCopyArgs(title);
  }

  publish { |event|
    this.changed(event);
  }
}
