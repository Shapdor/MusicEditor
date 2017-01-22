package cs3500.music.view.mock;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;
import cs3500.music.view.IMusicEditorView;

/**
 * Mock SheetMusicView.
 */
public class MockSheetMusicView extends javax.swing.JFrame implements IMusicEditorView {

  private final MockSheetMusicPanel displayPanel;

  /**
   * Creates new SheetMusicView with a JScrollPane made from the display panel.
   */
  public MockSheetMusicView() {
    super("OOD Music Editor v1.0 - By Eduardo Fares and Sebastian Wisniowiecki");
    this.displayPanel = new MockSheetMusicPanel();
    JScrollPane scrollPane = new JScrollPane(displayPanel);

    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(scrollPane);
    this.pack();

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation(dim.width / 2 - this.getSize().width / 2,
            dim.height / 2 - this.getSize().height / 2);
  }

  @Override
  public void initializeView(int tempo) {
    this.setVisible(true);
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
    displayPanel.acceptData(beats, headings, numBeats, measureLength);
  }

  @Override
  public void acceptCurrBeat(int currBeat) {
    displayPanel.acceptCurrBeat(currBeat);
  }

  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    // Not needed
  }

  @Override
  public void jumpToStart() {
    // Not needed
  }

  @Override
  public void jumpToEnd() {
    // Not needed
  }

  @Override
  public void moveRight() {
    // Not needed
  }

  @Override
  public void moveLeft() {
    // Not needed
  }

  @Override
  public boolean isPlayed() {
    return false;
  }


  @Override
  public Dimension getPreferredSize() {
    return displayPanel.getPreferredSize();
  }

  /**
   * Returns the number of times repainted.
   * @return Number of paints.
   */
  public int numOfPaints() {
    return displayPanel.numOfPaints;
  }

}
