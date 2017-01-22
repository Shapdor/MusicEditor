package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Handles keyboard actions.
 */
public class KeyboardHandler implements KeyListener {
  private Map<Integer, Runnable> knownCommands;

  public KeyboardHandler(Map<Integer, Runnable> pressed) {
    this.knownCommands = pressed;
  }

  @Override
  public void keyPressed(KeyEvent e) {

//    System.out.println("Key Pressed: " + e.getKeyCode());
    if (knownCommands.containsKey(e.getKeyCode())) {
      knownCommands.get(e.getKeyCode()).run();
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Not needed
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not needed
  }
}
