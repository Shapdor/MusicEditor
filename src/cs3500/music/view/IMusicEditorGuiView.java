package cs3500.music.view;

/**
 * Represents a visual representation of the song in the editor.
 */
public interface IMusicEditorGuiView extends IMusicEditorView {
  /**
   * Gets the string in the text field of the GUI.
   * @return text field string.
   */
  String getInputString();

  /**
   * Resets the focus to the main window from the text field.
   */
  void resetFocus();
}
