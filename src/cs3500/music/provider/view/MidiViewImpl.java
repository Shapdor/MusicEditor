package cs3500.music.provider.view;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.swing.*;

import cs3500.music.provider.controller.Controller;
import cs3500.music.provider.model.IMusicEditorModel;
import cs3500.music.provider.model.Measure;
import cs3500.music.provider.util.CompositionBuilder;

/**
 * Utilizes midi to play and display the song.
 */
public class MidiViewImpl implements View {
  private CompositionBuilder<IMusicEditorModel> builder;
  private JFrame frame;
  private Sequencer sequencer;
  private Sequence sequence;
  private Controller controller;
  private Synthesizer synth;
  private Receiver receiver;


  /**
   * A skeleton for MIDI playback.
   *
   * @param builder the builder of the model that will be played
   */
  public MidiViewImpl(CompositionBuilder<IMusicEditorModel> builder) {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = this.synth.getReceiver();
      synth.open();
      this.sequence = new Sequence(Sequence.PPQ, 520000);
      this.sequencer = MidiSystem.getSequencer();
      this.builder = builder;
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    this.frame = new JFrame();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
  }


  /**
   * A skeleton for MIDI playback.
   *
   * @param builder the builder of the model that will be played
   * @param view the JFrame to be utilized
   */
  public MidiViewImpl(CompositionBuilder<IMusicEditorModel> builder, JFrame view) {
    this.frame = view;
    try {
      this.sequence = new Sequence(Sequence.PPQ, 520000);
      this.sequencer = MidiSystem.getSequencer();
      this.builder = builder;
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

  }

  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   * <li>{@link MidiSystem#getSynthesizer()}</li>
   * <li>{@link Synthesizer}
   * <ul>
   * <li>{@link Synthesizer#open()}</li>
   * <li>{@link Synthesizer#getReceiver()}</li>
   * <li>{@link Synthesizer#getChannels()}</li>
   * </ul>
   * </li>
   * <li>{@link Receiver}
   * <ul>
   * <li>{@link Receiver#send(MidiMessage, long)}</li>
   * <li>{@link Receiver#close()}</li>
   * </ul>
   * </li>
   * <li>{@link MidiMessage}</li>
   * <li>{@link ShortMessage}</li>
   * <li>{@link MidiChannel}
   * <ul>
   * <li>{@link MidiChannel#getProgram()}</li>
   * <li>{@link MidiChannel#programChange(int)}</li>
   * </ul>
   * </li>
   * </ul>
   *
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI"> https://en.wikipedia.org/wiki/General_MIDI
   * </a>
   */
  public void startTrack() throws InvalidMidiDataException {
    IMusicEditorModel model = builder.build();
    Track result = sequence.createTrack();
    for (Measure measure : model.getPiece()) {
      for (int i = 1; i <= measure.numBeats(); i++) {
        result.add(new MidiEvent(((new ShortMessage(ShortMessage.NOTE_ON,
                measure.getBeats().get(measure.findBeat(i)).getInstrument(),
                measure.getNote().toInt(),
                measure.getBeats().get(measure.findBeat(i)).getVolume()))),
                 1000000 + measure.findBeat(i) * builder.getTempo()));
        result.add(new MidiEvent(((new ShortMessage(ShortMessage.NOTE_OFF,
                measure.getBeats().get(measure.findBeat(i)).getInstrument(),
                measure.getNote().toInt(),
                measure.getBeats().get(measure.findBeat(i)).getVolume()))),
                 1000000 + (measure.getBeatEnd(i) + 1) * builder.getTempo()));
      }
    }
  }

  /**
   * Returns the sequencer.
   *
   * @return sequencer
   */
  public Sequencer getSequencer() {
    return sequencer;
  }

  /**
   * Starts the sequencer.
   */
  @Override
  public void initialize() {
    this.controller = new Controller(builder, frame, this);
    controller.implement();
    try {
      startTrack();
      sequencer.setSequence(sequence);
      sequencer.open();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    sequencer.start();
  }

  /**
   * Returns the JFrame being used.
   *
   * @return frame
   */
  public JFrame getFrame() {
    return frame;
  }
}
