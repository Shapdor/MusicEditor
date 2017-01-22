package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;

/**
 * Shows the sheet music of the song and plays the output with midi.
 */
public class CombinedView implements IMusicEditorGuiView, IMusicEditorMidiView {

  IMusicEditorGuiView gui = new SheetMusicView();
  IMusicEditorMidiView midi = new MidiView();

  /**
   * Initializes the view.
   */
  @Override
  public void initializeView(int tempo) {
    gui.initializeView(tempo);
    midi.initializeView(tempo);
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
    gui.acceptData(beats, notes, headings, numBeats, measureLength, repeats);
    midi.acceptData(beats, notes, headings, numBeats, measureLength, repeats);
  }

  /**
   * Updates the current beat.
   * @param currBeat current beat
   */
  @Override
  public void acceptCurrBeat(int currBeat) {
    gui.acceptCurrBeat(currBeat);
  }

  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    gui.setListeners(aListen, keyListen, mouseListen);
    midi.setListeners(aListen, keyListen, mouseListen);
  }

  @Override
  public boolean isPlayed() {
    return true;
  }

  /**
   * Plays or stop the view.
   */
  @Override
  public void togglePlay() {
    midi.togglePlay();
  }

  /**
   * Jumps to the start of the view.
   */
  @Override
  public void jumpToStart() {
    midi.jumpToStart();
    gui.jumpToStart();
  }

  /**
   * Jumps to the end of the view.
   */
  @Override
  public void jumpToEnd() {
    midi.jumpToEnd();
    gui.jumpToEnd();
  }

  /**
   * Moves right in the view.
   */
  @Override
  public void moveRight() {
    midi.moveRight();
    gui.moveRight();
  }

  /**
   * Moves left on the view.
   */
  @Override
  public void moveLeft() {
    midi.moveLeft();
    gui.moveLeft();
  }

  @Override
  public int getCurrBeat() {
    return midi.getCurrBeat();
  }

  /**
   * Checks if the view will repeat at the current beat
   */
  @Override
  public void repeat() {
    this.midi.repeat();
  }

  @Override
  public String getInputString() {
    return gui.getInputString();
  }

  @Override
  public void resetFocus() {
    gui.resetFocus();
  }
}
