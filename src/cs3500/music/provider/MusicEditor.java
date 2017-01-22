package cs3500.music.provider;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.model.MusicEditorCreator;
import cs3500.music.provider.util.CompositionBuilder;
import cs3500.music.provider.util.MusicReader;
import cs3500.music.provider.view.ViewCreator;


public class MusicEditor {

  /**
   * Runs the Music Editor.
   *
   * @param args two arguments: the file to be played, and the type of view
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    CompositionBuilder<IMusicEditorModel> builder = new MusicEditorCreator.Builder();
    FileReader reader = new FileReader(args[0]);
    MusicReader.parseFile(reader, builder);
    ViewCreator.createView(args[1], builder).initialize();
  }
}
