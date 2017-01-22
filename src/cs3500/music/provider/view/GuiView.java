package cs3500.music.provider.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Created by michaelfleischmann on 11/12/16.
 */

public interface GuiView extends View {

  /**
   * Calls the implement method in controller.
   */
  void implement();

  /**
   * Instantiates the display panel to display the current tick.
   *
   * @param tick the current tick
   */
  void instantiate(long tick);

  /**
   * Adds the necessary controller listeners to the view.
   * @param keys adds a keylistener to the view.
   * @param mouse adds a mouselistener to the view.
   */
  void addListeners(KeyListener keys, MouseListener mouse);

}
