package cs3500.music.provider.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;

import cs3500.music.provider.controller.Controller;
import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.util.CompositionBuilder;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class GuiViewImpl extends javax.swing.JFrame implements GuiView {
  private ConcreteGuiViewPanel displayPanel;
  private Controller controller;
  private JScrollPane scroller;
  private boolean midi;

  /**
   * Constructs a new {@Code GuiViewImpl} view frame object.
   *
   * @param builder the builder of the model that will be worked with
   */
  public GuiViewImpl(CompositionBuilder<IMusicEditorModel> builder) {
    IMusicEditorModel model = builder.build();
    displayPanel = new ConcreteGuiViewPanel(model);
    controller = new Controller(builder, this);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setSize(model.getNumRows() * 20 + 58, model.getPiece().size() * 20 + 30);
    displayPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
    scroller = new JScrollPane(displayPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroller.getHorizontalScrollBar().setUnitIncrement(45);
    scroller.getVerticalScrollBar().setUnitIncrement(20);
    add(scroller);
    this.midi = true;


  }

  /**
   * Changes the position of the view.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   */
  public void changeView(int x, int y) {
    scroller.getViewport().setViewPosition(new Point(x, y));
  }

  /**
   * Determines what visual to display.
   *
   * @param b whether or not the midi should be played
   */
  public void setVisual(boolean b) {
    this.midi = b;
  }

  /**
   * Calls the implement method in controller.
   */
  @Override
  public void implement() {
    controller.implement();
  }

  /**
   * Instantiates the display panel to display the current tick.
   *
   * @param tick the current tick
   */
  @Override
  public void instantiate(long tick) {
    displayPanel.setTick(tick);
    repaint();
  }


  @Override
  public void addListeners(KeyListener keys, MouseListener mouse) {
    addKeyListener(keys);
    displayPanel.addMouseListener(mouse);
  }


  /**
   * Displays the Swing view.
   */
  @Override
  public void initialize() {
    this.setVisible(true);
    if (midi) {
      controller.implement();
    }
  }


}
