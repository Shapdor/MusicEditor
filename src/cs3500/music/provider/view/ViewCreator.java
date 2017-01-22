package cs3500.music.provider.view;

import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.util.CompositionBuilder;

/**
 * A class that creates a view of the given builder based on the input view type.
 */
public class ViewCreator {

  /**
   * Concrete class that creates a new instance of a View.
   *
   * @param view    determines which view will be created
   * @param builder the builder of the model that the view will work with
   */
  public static View createView(String view, CompositionBuilder<IMusicEditorModel> builder) {
    switch (view) {
      case "console":
        return new ConsoleViewImpl(builder);
      case "midi":
        return new MidiViewImpl(builder);
      case "visual":
        return new GuiViewImpl(builder);
      case "music":
        return new MusicViewImpl(builder);
      default:
        throw new IllegalArgumentException("Not a valid view type");
    }
  }
}
