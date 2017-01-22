package cs3500.music.provider.controller;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.*;

import cs3500.music.provider.model.Direction;
import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.model.Measure;
import cs3500.music.provider.model.Note;
import cs3500.music.provider.util.CompositionBuilder;
import cs3500.music.provider.view.GuiViewImpl;
import cs3500.music.provider.view.MidiViewImpl;

/**
 * Created by michaelfleischmann on 11/14/16.
 */
public class Controller {
  private KeyListener keys;
  private MouseHandler mouse;
  private IMusicEditorModel model;
  private JFrame view;
  private MidiViewImpl midi;
  private HashMap<Integer, Runnable> keyPressedMap;

  /**
   * A constructor for the Controller.
   *
   * @param builder the builder of the model of the music to be represented
   * @param view the view to be displayed
   */
  public Controller(CompositionBuilder<IMusicEditorModel> builder, JFrame view) {
    this.view = view;
    this.keys = new KeyboardHandler(this);
    this.mouse = new MouseHandler();
    this.model = builder.build();
    this.keyPressedMap = new HashMap<>();
  }

  /**
   * A constructor for the Controller.
   *
   * @param builder the builder of the model of the music to be represented
   * @param view which type of view to produce
   * @param midi the midi to be played
   */
  public Controller(CompositionBuilder<IMusicEditorModel> builder, JFrame view, MidiViewImpl midi) {
    this.view = view;
    this.midi = midi;
    this.keys = new KeyboardHandler(this);
    this.mouse = new MouseHandler();
    this.model = builder.build();
    this.keyPressedMap = new HashMap<>();
  }

  /**
   * Assigns actions to certain key presses.
   */
  public void putKeys() {
    Measure measure = model.getPiece().get(model.getPiece().size() - (mouse.getPos().y + 1));
    keyPressedMap.put(82, () -> {
      model.remove(measure.getNote(), measure.hashBeat().get(mouse.getPos().x));
    });
    keyPressedMap.put(83, () -> {
      model.edit(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), -1);
    });
    keyPressedMap.put(76, () -> {
      model.edit(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), 1);
    });
    keyPressedMap.put(65, () -> {
      model.addNote(measure.getNote(), 1, mouse.getPos().x);
    });
    keyPressedMap.put(73, () -> {
      measure.changeBeatInstrument(measure.hashBeat().get(mouse.getPos().x), 1);
    });
    keyPressedMap.put(75, () -> {
      measure.changeBeatInstrument(measure.hashBeat().get(mouse.getPos().x), -1);
    });
    keyPressedMap.put(84, () -> {
      model.getPiece().add(Measure.newOffMeasure(
              Note.intToNote(1 + model.getPiece().get(model.getPiece().size() - 1).
                      getNote().toInt()), model.getNumRows()));
    });
    keyPressedMap.put(71, () -> {
      model.getPiece().add(0, Measure.newOffMeasure(
              Note.intToNote(model.getPiece().get(0).getNote().toInt() - 1), model.getNumRows()));
    });
    keyPressedMap.put(37, () -> {
      model.move(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), Direction.Left, 1);
    });
    keyPressedMap.put(38, () -> {
      model.move(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), Direction.Up, 1);
    });
    keyPressedMap.put(39, () -> {
      model.move(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), Direction.Right, 1);
    });
    keyPressedMap.put(40, () -> {
      model.move(measure.getNote(), measure.hashBeat().get(mouse.getPos().x), Direction.Down, 1);
    });
    keyPressedMap.put(80, () -> {
      if (midi.getSequencer().isRunning()) {
        midi.getSequencer().stop();
      } else {
        midi.getSequencer().start();
      }
    });
    keyPressedMap.put(17, () -> {
      if (view instanceof GuiViewImpl) {
        ((GuiViewImpl) view).changeView(0, 0);
      }
    });
    keyPressedMap.put(27, () -> {
      if (view instanceof GuiViewImpl) {
        ((GuiViewImpl) view).changeView(view.getWidth(), 0);
      }
    });


  }


  public void implement() {
    view.addKeyListener(keys);
    try {
      view.getContentPane().getComponent(0).addMouseListener(mouse);
    } catch (ArrayIndexOutOfBoundsException e) {
      //Absorbs a thrown ArrayIndexOutOfBoundsException
    }
  }

  public void readjustView() {
    view.repaint();
    view.setSize(model.getNumRows() * 20 + 58, model.getPiece().size() * 20 + 30);
    try {
      view.getContentPane().getComponent(0).setPreferredSize(new Dimension(view.getWidth(),
              view.getHeight()));
    } catch (ArrayIndexOutOfBoundsException e) {
      //Absorbs a thrown ArrayIndexOutOfBoundsException
    }
  }

  public HashMap<Integer, Runnable> getKeyPressedMap() {
    return keyPressedMap;
  }


}
