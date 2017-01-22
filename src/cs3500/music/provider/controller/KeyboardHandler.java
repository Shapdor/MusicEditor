package cs3500.music.provider.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by michaelfleischmann on 11/12/16.
 */
public class KeyboardHandler implements KeyListener {
  Controller controller;

  KeyboardHandler(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    //Nothing happens
  }

  @Override
  public void keyPressed(KeyEvent e) {
    controller.putKeys();
    try {
      controller.getKeyPressedMap().get(e.getExtendedKeyCode()).run();
    } catch (NullPointerException n) {
      // Allows the NullPointerException to be absorbed.
    } catch (IllegalArgumentException l) {
      System.out.print(l.getMessage() + "\n");
    }
    controller.readjustView();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    //Nothing happens
  }

}
