package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.sound.midi.Sequencer;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;
import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.model.MusicEditorCreator;
import cs3500.music.provider.model.Pitch;
import cs3500.music.provider.util.CompositionBuilder;
import cs3500.music.provider.view.GuiViewImpl;
import cs3500.music.provider.view.MidiViewImpl;
import cs3500.music.provider.view.MusicViewImpl;
import cs3500.music.provider.view.View;
import cs3500.music.provider.view.ViewCreator;

/**
 * Adapter for provider's view. Had to use providers' model due to difficulties created by
 * strong coupling.
 */
public class ViewAdapter implements IMusicEditorView, IMusicEditorMidiView {

  private CompositionBuilder<IMusicEditorModel> targetModel;
  private String viewType;
  private View view;
  private ActionListener aListener;
  private KeyListener kListener;
  private MouseListener mListener;

  public ViewAdapter(String viewType) {
    this.targetModel = new MusicEditorCreator.Builder();
    this.viewType = viewType;
  }

  /**
   * Initializes the view.
   *
   * @param tempo song's tempo.
   */
  @Override
  public void initializeView(int tempo) {
    targetModel.setTempo(tempo);
  }

  /**
   * Accepts data from the controller to be rendered.
   *
   * @param beats         the beat track
   * @param headings      the note headings of every note column
   * @param numBeats      the number of beats in the editor
   * @param measureLength the measure length
   */
  @Override
  public void acceptData(List<List<Beat>> beats, List<Note> notes, List<String> headings,
                         int numBeats, int measureLength, List<Repeats> repeats) {
    for (Note note : notes) {
      cs3500.music.provider.model.Note provNote = adaptNote(note);
      targetModel.addNote(note.getStartBeat(), note.getLastBeat(), note.getInstrument(),
              note.getRealPitch(), note.getVolume());
    }
    this.view = ViewCreator.createView(viewType, targetModel);
    if (this.view instanceof MusicViewImpl) {
      MusicViewImpl musicView = (MusicViewImpl) this.view;
      GuiViewImpl guiView = musicView.getGui();
      guiView.addListeners(kListener, mListener);
    }
    this.view.initialize();
  }

  /**
   * Adapts an original note to a note from the provider.
   * @param note Original note to be adapted
   * @return  Note in provider's note
   */
  private cs3500.music.provider.model.Note adaptNote(Note note) {
    return new cs3500.music.provider.model.Note(Pitch.intToPitch(note.getRealPitch() % 12),
            note.getRealPitch() / 12);
  }

  /**
   * Updates the current beat.
   *
   * @param currBeat current beat.
   */
  @Override
  public void acceptCurrBeat(int currBeat) {

  }

  /**
   * Sets the listeners for the view.
   *
   * @param aListen     The action listener for the buttons.
   * @param keyListen   The key listener for keyboard input.
   * @param mouseListen The mouse listener for mouse input.
   */
  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    this.aListener = aListen;
    this.kListener = keyListen;
    this.mListener = mouseListen;
  }

  /**
   * Jumps to the start of the view.
   */
  @Override
  public void jumpToStart() {
    if (this.view instanceof MusicViewImpl) {
      ((MusicViewImpl) this.view).getGui().changeView(0, 0);
    }
  }

  /**
   * Jumps to the end of the view.
   */
  @Override
  public void jumpToEnd() {
    if (this.view instanceof MusicViewImpl) {
      GuiViewImpl gui = ((MusicViewImpl) this.view).getGui();
      gui.changeView(gui.getWidth(), 0);
    }
  }

  /**
   * Moves right one measure in the view.
   */
  @Override
  public void moveRight() {
    // Provider's code doesn't have a way to move right
  }

  /**
   * Moves left one measure on the view.
   */
  @Override
  public void moveLeft() {
    // Provider's code doesn't have a way to move left
  }

  /**
   * Checks if the representation of the editor plays music.
   *
   * @return True if it plays, false otherwise.
   */
  @Override
  public boolean isPlayed() {
    return true;
  }

  /**
   * Toggles the play on the view.
   */
  @Override
  public void togglePlay() {
    Sequencer midiSequencer = null;

    if (this.viewType.equals("music")) {
      MusicViewImpl musicView = (MusicViewImpl) this.view;
      midiSequencer = musicView.getMidi().getSequencer();
    }
    else if (this.viewType.equals("midi")) {
      MidiViewImpl midiView = (MidiViewImpl) this.view;
      midiSequencer = midiView.getSequencer();
    }

    if (midiSequencer != null) {
      if (midiSequencer.isRunning()) {
        midiSequencer.stop();
      } else {
        midiSequencer.start();
      }
    }
  }

  @Override
  public int getCurrBeat() {
    return 0; //Stub method, not used
  }

  /**
   * Checks if the view will repeat at the current beat
   */
  @Override
  public void repeat() {
    //Stub method
  }
}
