Sample {
  var <buffer, <soundfile, <frames, <name;
  *new { |path|
    ^super.new.init(path);
  }

  init { |path|
    soundfile = SoundFile.new;
    soundfile.openRead(path.fullPath.asString);
    buffer = soundfile.asBuffer();
    frames = buffer.numFrames;
    name = path.fileNameWithoutExtension;
  }
}
