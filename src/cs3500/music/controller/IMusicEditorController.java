package cs3500.music.controller;

/**
 * A controller for the music editor designed to take input from the user to do the following.
 *
 * <p>
 * - Remove notes
 * - Add notes
 * - Edit notes
 * - Add a song simultaneously or consecutively
 *</p>
 */
public interface IMusicEditorController {

  /**
   * Initializes the music editor and sets the measure length. Launches the visual representation
   * of the editor or plays the music.
   *
   * @param measureLength the measure length to set the editor to
   */
  void startEditor(int measureLength);

}
