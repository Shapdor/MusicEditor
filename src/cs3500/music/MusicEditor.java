package cs3500.music;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.IMusicEditorController;
import cs3500.music.controller.MusicEditorControllerImpl;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.util.MusicReader;
import cs3500.music.view.CombinedView;
import cs3500.music.view.IMusicEditorView;
import cs3500.music.view.MidiView;
import cs3500.music.view.SheetMusicTextView;
import cs3500.music.view.SheetMusicView;
import cs3500.music.view.ViewAdapter;


public class MusicEditor {

  /**
   * Main method running the music editor.
   *
   * @param args the arguments for the music editor
   * @throws IOException for an invalid file
   * @throws InvalidMidiDataException when midi input is invalid
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {

    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();

    IMusicEditorModel model = MusicReader.parseFile(new FileReader(args[0]), builder);
    IMusicEditorView view;
    switch (args[1]) {
      case "text": view = new SheetMusicTextView();
        break;
      case "gui": view = new SheetMusicView();
        break;
      case "midi": view = new MidiView();
        break;
      case "combined": view = new CombinedView();
        break;
      case "adapter-text": view = new ViewAdapter("console");
        break;
      case "adapter-gui": view = new ViewAdapter("visual");
        break;
      case "adapter-midi": view = new ViewAdapter("midi");
        break;
      case "adapter-combined": view = new ViewAdapter("music");
        break;
      default:
        throw new IllegalArgumentException("Not a valid view");
    }

    IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
    controller.startEditor(4);
  }
}