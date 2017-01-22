package cs3500.music.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;

/**
 * Renders the music editor's data in a piano scroll form.
 */
public class SheetMusicView extends javax.swing.JFrame implements IMusicEditorGuiView {

  private final SheetMusicViewPanel displayPanel;
  private final JPanel buttonPanel;
  private JScrollPane scrollPane;
  private KeyListener keyListen;
  private MouseListener mouseListen;
  private ActionListener aListen;
  private List<JButton> buttons;
  private JTextField text;
  private boolean isPlayed;
  private int currBeat;


  /**
   * Creates new SheetMusicView with a JScrollPane made from the display panel.
   */
  public SheetMusicView() {
    super("OOD Music Editor v2.0 - By Eduardo Fares and Sebastian Wisniowiecki");
    this.displayPanel = new SheetMusicViewPanel();
    this.buttonPanel = new JPanel();

    this.text = new JTextField(10);
    this.buttons = new ArrayList<>();
    this.buttons.add(new JButton("Add Note"));
    this.buttons.add(new JButton("Remove Note"));
    this.buttons.add(new JButton("Edit Pitch"));
    this.buttons.add(new JButton("Edit Octave"));
    this.buttons.add(new JButton("Edit Length"));
    this.buttons.add(new JButton("Set Repeat"));


  }

  /**
   * Initializes the view.
   * @param tempo song's tempo.
   */
  @Override
  public void initializeView(int tempo) {

    this.scrollPane = new JScrollPane(displayPanel);

    this.buttonPanel.setLayout(new FlowLayout());
    this.buttonPanel.add(this.text);
    for (JButton b : this.buttons) {
      this.buttonPanel.add(b);
    }

    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    this.pack();
    this.setVisible(true);

    this.displayPanel.requestFocus();
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
    redraw();
  }


  @Override
  public void acceptCurrBeat(int currBeat) {
    this.currBeat = currBeat;
    displayPanel.acceptCurrBeat(currBeat);

    JScrollBar horiz = scrollPane.getHorizontalScrollBar();
    int windowWidth = this.getContentPane().getWidth();

    if (displayPanel.getBeatBarXVal() > horiz.getValue() + windowWidth) {
      horiz.setValue(horiz.getValue() + windowWidth);
    }
    redraw();
  }

  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    this.aListen = aListen;
    this.keyListen = keyListen;
    this.mouseListen = mouseListen;
    this.displayPanel.addKeyListener(keyListen);
    this.displayPanel.addMouseListener(mouseListen);
    for (JButton b : this.buttons) {
      b.addActionListener(aListen);
    }
  }

  @Override
  public void jumpToStart() {
    JScrollBar horiz = this.scrollPane.getHorizontalScrollBar();
    horiz.setValue(0);
  }

  @Override
  public void jumpToEnd() {
    JScrollBar horiz = this.scrollPane.getHorizontalScrollBar();
    horiz.setValue(horiz.getMaximum());
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
  public String getInputString() {
    String inputText = text.getText();
    text.setText("");
    return inputText;
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.displayPanel.requestFocus();
  }

  private void redraw() {
    this.displayPanel.validate();
    this.displayPanel.repaint();
  }
}
