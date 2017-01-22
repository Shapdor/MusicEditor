package cs3500.music.provider.view;


import java.awt.*;

import javax.swing.*;

import cs3500.music.provider.model.Beat;
import cs3500.music.provider.model.BeatType;
import cs3500.music.provider.model.IMusicEditorModel;

/**
 * Paints the GUI based on information sent from the provided model.
 *
 */
public class ConcreteGuiViewPanel extends JPanel {
  private IMusicEditorModel model;
  private int tick;

  /**
   * Constructs the panel inside of the GUI view frame.
   *
   * @param model the model that will be viewed
   */
  public ConcreteGuiViewPanel(IMusicEditorModel model) {
    this.model = model;
    this.tick = 0;

  }

  /**
   * Paints the gui view.
   *
   * @param g default graphics input
   */
  @Override
  public void paintComponent(Graphics g) {
    // Handle the default painting
    super.paintComponent(g);

    for (int i = 0; i < model.getPiece().size(); i++) {
      g.drawString(model.getPiece().get(i).getNote().toString(), 5, 33 +
              (model.getPiece().size() - 1) * 20 - i * 20);
    }
    for (int i = 0; i < model.getNumRows(); i++) {
      if (i % 8 == 0) {
        g.drawString(String.valueOf(i), 50 + i * 20, 15);
      }
    }
    for (int i = 0; i < model.getPiece().size(); i++) {
      for (int k = 0; k < model.getNumRows(); k++) {
        if (k % 4 == 0) {
          g.setColor(Color.BLACK);
          g.drawRect(50 + k * 20, 18 + i * 20, 80, 20);
          g.setColor(new Color(192, 192, 192));
          g.fillRect(51 + k * 20, 19 + i * 20, 79, 19);
        }
      }
    }
    for (int i = 0; i < model.getPiece().size(); i++) {
      for (int k = 0; k < model.getNumRows(); k++) {
        Beat beat = model.getPiece().get(i).getBeats().get(k);
        if (beat.equals(new Beat(BeatType.Head))) {
          g.setColor(new Color(0, beat.getInstrument() * 15, 153));
          g.fillRect(50 + k * 20, 19 + (model.getPiece().size() - 1) * 20 - i * 20, 20, 19);
        }
        if (beat.equals(new Beat(BeatType.Sustain))) {
          g.setColor(new Color(178, (152 - beat.getInstrument() * 10), 255));
          g.fillRect(50 + k * 20, 19 + (model.getPiece().size() - 1) * 20 - i * 20, 20, 19);
        }

      }
    }
    g.setColor(Color.RED);
    g.drawLine(tick, 20, tick, ((model.getPiece().size() + 1) * 20));
  }

  /**
   * Sets the tick.
   *
   * @param t the value to set the tick to
   */
  public void setTick(long t) {
    this.tick = Math.toIntExact(t);
  }

}
