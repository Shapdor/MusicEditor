package cs3500.music.provider.view;

/**
 * Created by michaelfleischmann on 11/7/16.
 */

import java.util.ArrayList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * Created by michaelfleischmann on 11/7/16.
 */

/**
 * A Mock skeleton for MIDI playback.
 */
public class MockReceiver implements Receiver {
  public ArrayList<MidiMessage> messages = new ArrayList<MidiMessage>();

  @Override
  public void send(MidiMessage message, long timeStamp) {
    messages.add(message);
  }

  @Override
  public void close() {
    //need to override this but this is unused
  }

  ArrayList<MidiMessage> getMessages() {
    return messages;
  }

  public String printMessage(ShortMessage message) {
    return "" + message.getCommand() + " " + message.getChannel() + " " + message.getData1()
            + " " + message.getData2();
  }
}

