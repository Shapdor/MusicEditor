package cs3500.music.provider.view;

import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.util.CompositionBuilder;

/**
 * Created by michaelfleischmann on 11/6/16.
 */
public class ConsoleViewImpl implements View {
  private CompositionBuilder<IMusicEditorModel> builder;

  /**
   * Constructs a {@Code ConsoleViewImpl} object, which prints the music of the model of the
   * builder.
   *
   * @param builder the builder that the view works on
   */
  public ConsoleViewImpl(CompositionBuilder<IMusicEditorModel> builder) {
    this.builder = builder;
  }

  /**
   * Displays the song in the console.
   */
  @Override
  public void initialize() {
    IMusicEditorModel model = builder.build();
    System.out.print(model.getMusicState());
  }
}
