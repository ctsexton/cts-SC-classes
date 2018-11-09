Sample {
  var <buffer, <soundFile, <frames, <name;
  *new { |path|
    ^super.new.init(path);
  }

  init { |path|
    soundFile = SoundFile.new;
    soundFile.openRead(path.fullPath.asString);
    buffer = soundFile.asBuffer();
    frames = buffer.numFrames;
    name = path.fileNameWithoutExtension;
  }
}
