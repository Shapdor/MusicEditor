package cs3500.music.view;

/**
 * Created by Sebastian on 11/21/2016.
 */
public interface IMusicEditorMidiView extends IMusicEditorView {
  /**
   * Plays or stop the music.
   */
  void togglePlay();


  /**
   * Gets the current playing beat.
   * @return current playing beat.
   */
  int getCurrBeat();

  /**
   * Checks if the view will repeat at the current beat
   */
  void repeat();

}
