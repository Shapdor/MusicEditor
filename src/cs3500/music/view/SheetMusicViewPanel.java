package cs3500.music.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import cs3500.music.model.Beat;

/**
 * A panel to visually render the beats and measures of the music editor.
 */
public class SheetMusicViewPanel extends JPanel {

  List<List<Beat>> beats;
  int numBeats;
  int currBeat;
  int measureLength;
  List<String> noteHeadings;

  int bSize = 25;
  int noteHeadSize = 0;

  /**
   * Paints the component.
   * @param g graphic to be painted.
   */
  @Override
  public void paintComponent(Graphics g) {
    // Handle the default painting
    super.paintComponent(g);

    for (int i = 0; i < noteHeadings.size(); i++) {
      g.drawString(noteHeadings.get(i), bSize, (bSize * (i + 2)) - bSize / 4);
    }

    for (int i = 0; i < numBeats; i++) {
      List<Beat> selectBeats = beats.get(i);
      for (int j = 0; j < beats.get(i).size(); j++) {
        Beat selectBeat = selectBeats.get(j);
        if (selectBeat != null) {
          if (selectBeat.isHead()) {
            g.setColor(Color.BLACK);
          } else {
            g.setColor(Color.GREEN);
          }
          g.fillRect(bSize * (i + 2), bSize * (j + 1), bSize, bSize);
        }
      }
    }

    g.setColor(Color.BLACK);

    g.drawString("Beat", bSize * 2, bSize / 2);
    for (int i = 0; i <= Math.ceil(numBeats / measureLength); i++) {
      g.drawString("" + i * measureLength, bSize * (measureLength * i) + bSize * 2,
              bSize);
      for (int j = 0; j < beats.get(i).size(); j++) {
        g.drawRect(bSize * (measureLength * i) + bSize * 2,
                bSize * (j + 1), measureLength * bSize, bSize);
      }
    }

    g.setColor(Color.RED);

    if (currBeat == 0) {
      g.drawLine(bSize * 2, bSize, bSize * 2, bSize + (noteHeadSize * bSize));
    }
    else {
      g.drawLine(bSize + (bSize * currBeat), bSize, bSize + (bSize * currBeat),
              bSize + (noteHeadSize * bSize));
    }
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
    this.noteHeadSize = headings.size();
    this.setPreferredSize(new Dimension(bSize * (6 + numBeats), (2 + this.noteHeadSize) * bSize));

    for (List<Beat> bList : this.beats) {
      Collections.reverse(bList);
    }
    Collections.reverse(this.noteHeadings);
  }

  /**
   * Updates the current beat.
   * @param currBeat current beat.
   */
  void acceptCurrBeat(int currBeat) {
    this.currBeat = currBeat;
  }

  /**
   * Gets the X coordinate value of the beat's tracking bar.
   * @return X coordinate of beat bar.
   */
  int getBeatBarXVal() {
    return currBeat * bSize;
  }
}
