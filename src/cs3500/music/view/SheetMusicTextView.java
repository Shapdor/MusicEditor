package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;

/**
 * A console text representation of the music editor.
 */
public class SheetMusicTextView implements IMusicEditorView {
  private List<List<Beat>> beats;
  private List<String> headings;
  private int numBeats;

  /**
   * Initializes the view.
   * @param tempo song's tempo.
   */
  @Override
  public void initializeView(int tempo) {
    // There is nothing to initialize
  }

  /**
   * Accepts data from the controller to be rendered.
   *
   * @param beats the beat track
   * @param headings the note headings of every note column
   * @param numBeats the number of beats in the editor
   * @param measureLength the measure length
   */
  @Override
  public void acceptData(List<List<Beat>> beats, List<Note> notes, List<String> headings,
                         int numBeats, int measureLength, List<Repeats> repeats) {
    this.beats = beats;
    this.headings = headings;
    this.numBeats = numBeats;
    System.out.print(getSheetMusic());
  }

  /**
   * Updates the current beat.
   * @param currBeat current beat.
   */
  @Override
  public void acceptCurrBeat(int currBeat) {
    // Stub
  }

  /**
   * Sets the listeners for the view.
   * @param aListen The action listener for the buttons.
   * @param keyListen The key listener for keyboard input.
   * @param mouseListen The mouse listener for mouse input.
   */
  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    // No listeners applicable
  }

  /**
   * Jumps to the start of the view.
   */
  @Override
  public void jumpToStart() {
    // Can't move through text
  }

  /**
   * Jumps to the end of the view.
   */
  @Override
  public void jumpToEnd() {
    // Can't move through text
  }

  /**
   * Moves right one measure in the view.
   */
  @Override
  public void moveRight() {
    // Can't move through text
  }

  /**
   * Moves left one measure on the view.
   */
  @Override
  public void moveLeft() {
    // Can't move through text
  }

  /**
   * Checks if the representation of the editor plays music.
   * @return True if it plays, false otherwise.
   */
  @Override
  public boolean isPlayed() {
    return false;
  }


  /**
   * Represents the current song as a formatted string, giving each note its own track and numbering
   * every beat of the song. Labels the head of a note as "X", a non-head note beat as "|" and a
   * rest as a blank.
   *
   * @return the formatted string representing the current song
   */
  public String getSheetMusic() {

    if (this.beats.isEmpty()) {
      throw new IllegalArgumentException("There are no notes to display.");
    }

    StringBuilder output = new StringBuilder();

    /* First row */
    output.append("  ");
    for (String s : this.headings) {
      output.append(getFormattedHeading(s));
    }
    output.append('\n');

    /* Print out beat track */
    for (int i = 0; i < numBeats; i++) {
      output.append(String.format("%2d", i));
      for (int j = 0; j < this.beats.get(i).size(); j++) {
        List<Beat> currBeatList = this.beats.get(i);
        if (currBeatList.get(j) == null) {
          output.append("     ");
        } else if (currBeatList.get(j).isHead()) {
          output.append("  X  ");
        } else {
          output.append("  |  ");
        }
      }
      output.append('\n');
    }
    return output.toString();
  }

  /**
   * Formats the heading of the note column, by centering it if possible in a 5 character space.
   *
   * @return the formatted note column.
   */
  public String getFormattedHeading(String heading) {
    switch (heading.length()) {
      case 2:
        return "  " + heading + " ";
      case 3:
        return " " + heading + " ";
      case 4:
        return " " + heading;
      case 5:
        return heading;
      default:
        throw new IllegalArgumentException("There is an illegal column.");
    }
  }
}
