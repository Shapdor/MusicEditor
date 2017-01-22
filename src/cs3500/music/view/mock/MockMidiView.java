package cs3500.music.view.mock;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;

import cs3500.music.view.MidiView;

/**
 * Mock Midi View.
 */
public class MockMidiView extends MidiView {

  Synthesizer synth;
  Receiver receiver;

  /**
   * Mock builder.
   *
   * @param log StringReader to log
   */
  public MockMidiView(StringBuilder log) {
    reloadView();
    try {
      this.synth = new MockMidiDevice(log);
      this.receiver = synth.getReceiver();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

}
