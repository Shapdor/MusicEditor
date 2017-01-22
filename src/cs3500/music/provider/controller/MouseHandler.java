package cs3500.music.provider.controller;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Created by michaelfleischmann on 11/12/16.
 */
public class MouseHandler implements MouseListener {
  private Point pos = new Point(0, 2);

  /**
   * Assigns the point where the mouse is click to variable pos.
   *
   * @param e the mouse event
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    pos = e.getPoint();
  }

  /**
   * Nothing happens when the mouse is pressed.
   *
   * @param e the mouse event
   */
  @Override
  public void mousePressed(MouseEvent e) {
    //Nothing happens
  }

  /**
   * Nothing happens when the mouse is released.
   *
   * @param e the mouse event
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    //Nothing happens
  }

  /**
   * Nothing happens when the mouse is entered.
   *
   * @param e the mouse event
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    //Nothing happens
  }

  /**
   * Nothing happens when the mouse is exited.
   *
   * @param e the mouse event
   */
  @Override
  public void mouseExited(MouseEvent e) {
    //Nothing happens
  }

  /**
   * Creates a new point.
   *
   * @return the generated point
   */
  public Point getPos() {
    return new Point((pos.x - 51) / 20, (pos.y - 21) / 20);
  }

  /**
   * Gets the x coordinate of the position.
   *
   * @return the x  coordinate of the position
   */
  public final int holdPos() {
    return getPos().x;
  }
}
