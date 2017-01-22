package cs3500.music.provider.view;

import cs3500.music.provider.controller.Controller;
import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.util.CompositionBuilder;

/**
 * A class that combines and displays the midi and gui views.
 */
public class MusicViewImpl implements View {
  GuiViewImpl gui;
  MidiViewImpl midi;
  Controller controller;
  CompositionBuilder<IMusicEditorModel> builder;


  /**
   * Constructs a MusicViewImpl
   *
   * @param builder The builder of the Model being used.
   */
  public MusicViewImpl(CompositionBuilder<IMusicEditorModel> builder) {
    this.builder = builder;
    this.gui = new GuiViewImpl(builder);
    this.gui.setVisual(false);
    this.midi = new MidiViewImpl(builder, gui);
    this.controller = new Controller(builder, gui, midi);
  }

  /**
   * Initializes the view.
   */
  @Override
  public void initialize() {
    midi.initialize();
    gui.initialize();
    while (true) {
      gui.instantiate((midi.getSequencer().getMicrosecondPosition() * 21 /
              builder.getTempo()) - 60);
    }
  }

  public GuiViewImpl getGui() {
    return this.gui;
  }

  public MidiViewImpl getMidi() {
    return this.midi;
  }
}
