package cs3500.music.view.mock;

import java.awt.Graphics;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import cs3500.music.model.Beat;

/**
 * Mock SheetMusicPanel.
 */
public class MockSheetMusicPanel extends JPanel {
  List<List<Beat>> beats;
  int numBeats;
  int currBeat;
  int measureLength;
  List<String> noteHeadings;

  int numOfPaints = 0;

  @Override
  public void paintComponents(Graphics g) {
    super.paintComponents(g);
    numOfPaints++;
  }

  /**
   * Accepts data from the controller to be rendered.
   *
   * @param beats the beat track
   * @param headings the note headings of every note column
   * @param numBeats the number of beats in the editor
   * @param measureLength the measure length
   */
  void acceptData(List<List<Beat>> beats, List<String> headings,
                         int numBeats, int measureLength) {
    this.beats = beats;
    this.numBeats = numBeats;
    this.measureLength = measureLength;
    this.noteHeadings = headings;

    for (List<Beat> bList : this.beats) {
      Collections.reverse(bList);
    }
    Collections.reverse(this.noteHeadings);
  }

  void acceptCurrBeat(int currBeat) {
    this.currBeat = currBeat;
  }
}
