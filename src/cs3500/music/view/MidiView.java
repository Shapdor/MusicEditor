package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Repeats;
import cs3500.music.model.Segment;

/**
 * Midi view implementation. Using a sequencer
 */
public class MidiView implements IMusicEditorMidiView {

  private Sequencer sequencer;
  private Sequence song;
  private HashMap<Integer, Track> tracks;
  private HashMap<Integer, Integer> channels;
  private Synthesizer synth;
  private Receiver receiver;
  private Transmitter transmitter;
  private float tempo;
  private boolean isPlaying;
  private long movingTime;

  private List<Repeats> repetitions;
  private List<Segment> skips;

  public MidiView() {
    reloadView();
  }

  /**
   * Initializes the view and converts the given tempo into BPM.
   * @param tempo tempo in MPQ
   */
  @Override
  public void initializeView(int tempo) {
    movingTime = tempo * 4; // Tempo is given in microseconds per quarter note
    this.tempo = 60000000 / tempo / 4; // Each note is a whole note in our model
    System.out.println(this.tempo);
    sequencer.setTempoInBPM(this.tempo);

    repetitions = new ArrayList<>();

    skips = new ArrayList<>();
  }

  /**
   * Accepts data from the controller to be rendered.
   *
   * @param beats         the beat track
   * @param notes         the notes to be played
   * @param headings      the note headings of every note column
   * @param numBeats      the number of beats in the editor
   * @param measureLength the measure length
   */
  @Override
  public void acceptData(List<List<Beat>> beats, List<Note> notes, List<String> headings,
                         int numBeats, int measureLength, List<Repeats> repeats) {

    reloadView();
    this.repetitions = repeats;
    try {
      song = new Sequence(Sequence.PPQ, 4);
      sequencer.setSequence(song);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    tracks = new HashMap();
    channels = new HashMap();

    for (Note note : notes) {
      ShortMessage on = new ShortMessage();
      ShortMessage off = new ShortMessage();
      ShortMessage instChange = new ShortMessage();
      try {
        if (!tracks.containsKey(note.getInstrument())) {
          // Create a new track
          tracks.put(note.getInstrument(), song.createTrack());
          channels.put(note.getInstrument(), channels.size());
          instChange.setMessage(ShortMessage.PROGRAM_CHANGE, channels.get(note.getInstrument()),
                  note.getInstrument(), 0);
          tracks.get(note.getInstrument()).add(new MidiEvent(instChange, 0));
        }

        on.setMessage(ShortMessage.NOTE_ON, channels.get(note.getInstrument()),
                note.getRealPitch(), note.getVolume());
        off.setMessage(ShortMessage.NOTE_OFF, channels.get(note.getInstrument()),
                note.getRealPitch(), note.getVolume());

        tracks.get(note.getInstrument()).add(new MidiEvent(on, note.getStartBeat()));
        tracks.get(note.getInstrument()).add(new MidiEvent(off, note.getLastBeat()));
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }

    togglePlay();

  }

  /**
   * Updates the current beat.
   * @param currBeat current beat.
   */
  @Override
  public void acceptCurrBeat(int currBeat) {
    // Does not accept a beat because the current beat originates from this class
  }

  /**
   * Gets the current playing beat.
   * @return current playing beat.
   */
  public int getCurrBeat() {
    return (int) sequencer.getTickPosition();
  }

  /**
   * Sets the listeners for the view.
   * @param aListen The action listener for the buttons.
   * @param keyListen The key listener for keyboard input.
   * @param mouseListen The mouse listener for mouse input.
   */
  @Override
  public void setListeners(ActionListener aListen, KeyListener keyListen,
                           MouseListener mouseListen) {
    // No listeners to set

  }

  /**
   * Checks if the representation of the editor plays music.
   * @return True if it plays, false otherwise.
   */
  @Override
  public boolean isPlayed() {
    return true;
  }

  /**
   * Plays or stop the view.
   */
  @Override
  public void togglePlay() {
    // Open the sequencer and synth
    if (isPlaying) {
      sequencer.stop();
    } else {
      try {
        sequencer.open();
        synth.open();
        sequencer.setTempoInBPM(tempo);
        System.out.println("Tempo: " + sequencer.getTempoInBPM());
        Thread.sleep(1000);
      } catch (MidiUnavailableException | InterruptedException e) {
        e.printStackTrace();
      }

      sequencer.start();
      sequencer.setTempoInBPM(tempo);

    }

    isPlaying = !isPlaying;
  }

  /**
   * Reloads the view to the default view.
   */
  public void reloadView() {
    isPlaying = false;
    if (this.sequencer != null) {
      this.sequencer.close();
    }
    try {
      sequencer = MidiSystem.getSequencer();
      synth = MidiSystem.getSynthesizer();
      receiver = synth.getReceiver();
      transmitter = sequencer.getTransmitter();
      transmitter.setReceiver(receiver);
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Jumps to the start of the view.
   */
  @Override
  public void jumpToStart() {
    this.sequencer.setMicrosecondPosition(0);
    sequencer.setTempoInBPM(this.tempo);

    for (Repeats r : this.repetitions) {
      r.togglePlayed();
    }
  }

  /**
   * Jumps to the end of the view.
   */
  @Override
  public void jumpToEnd() {
    this.sequencer.setMicrosecondPosition(sequencer.getMicrosecondLength());
    sequencer.setTempoInBPM(this.tempo);
  }

  /**
   * Moves right one step in the view.
   */
  @Override
  public void moveRight() {
    long moveTo = sequencer.getMicrosecondPosition() + movingTime;
    System.out.print(sequencer.getMicrosecondPosition() / 1000000 + " to: " + moveTo / 1000000);
    sequencer.setMicrosecondPosition(moveTo);
    sequencer.setTempoInBPM(this.tempo);
  }

  /**
   * Moves left one step in the view.
   */
  @Override
  public void moveLeft() {
    long moveTo = sequencer.getMicrosecondPosition() - movingTime;
    sequencer.setMicrosecondPosition(moveTo);
    sequencer.setTempoInBPM(this.tempo);
  }

  /**
   * Checks if the view will repeat at this given moment
   */
  @Override
  public void repeat() {
    for (int i = 0; i < repetitions.size(); i++) {
      Repeats r = repetitions.get(i);
      if (r.getEnd() == getCurrBeat() && !r.played) {
        r.togglePlayed();
        long moveTo;
        if (r.skip) {
          skips.add(new Segment(r.getStart(), r.getEnd()));
          moveTo = 0;
        } else {
          moveTo = 125000 * r.getStart();
        }
        sequencer.setMicrosecondPosition(moveTo);

        sequencer.setTempoInBPM(this.tempo);
      }
    }

    for (int i = 0; i < skips.size(); i++) {
      Segment skip = skips.get(i);
      if (skip.start == getCurrBeat()) {
        long moveTo = 125000 * skip.end;
        sequencer.setMicrosecondPosition(moveTo);
        sequencer.setTempoInBPM(this.tempo);
        skips.remove(i);
      }
    }
  }
}
