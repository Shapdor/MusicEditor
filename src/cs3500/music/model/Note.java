package cs3500.music.model;

import java.util.Objects;

import static cs3500.music.model.Pitch.getPitchFromString;
import static cs3500.music.model.Pitch.getSymbol;


/**
 * To represent a note with a Pitch (e.g. D#), an octave (e.g. 7), a start beat and length. Also
 * defines the volume at which it is played and the instrument playing the note.
 */
public class Note {

  Pitch pitch;
  int octave;
  int length;
  int startBeat;
  int lastBeat;
  int instrument;
  int volume;

  // TODO Set all variables to public final? Replace edit with delete and add?
  /**
   * Constructs a note with a given pitch, octave, length, and beat at which it starts.
   *
   * @param pitchString the note's pitch
   * @param octave the note's octave
   * @param instrument the note's instrument
   * @param length the note's length
   * @param startBeat the note's starting beat
   * @param volume the note's volume
   */
  public Note(String pitchString, int octave, int instrument, int length, int startBeat,
              int volume) {
    this.pitch = getPitchFromString(pitchString);

    if (octave < 0 || octave > 999) {
      throw new IllegalArgumentException("Invalid octave.");
    }
    else {
      this.octave = octave;
    }

    this.length = length;
    this.startBeat = startBeat;
    this.lastBeat = startBeat + length - 1;
    this.instrument = instrument;
    this.volume = volume;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Note)) {
      return false;
    }
    Note that = (Note) other;
    return this.octave == that.octave
            && this.length == that.length
            && this.startBeat == that.startBeat
            && this.lastBeat == that.lastBeat;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.octave, this.length, this.startBeat, this.lastBeat);
  }

  @Override
  public String toString() {
    return getSymbol(this.pitch) + this.octave;
  }

  void setLength(int length) { // FIXME Delete and create a new note
    this.length = length;
    this.lastBeat = this.startBeat + this.length - 1;
  }

  /**
   * Returns the ordinal of the note, with 0 being the lowest note, 1 being the next note ascending
   * WITH respect to octave.
   *
   * @return real pitch of a note
   */
  public int getRealPitch() {
    return 12 * (octave + 1) + pitch.ordinal();
  }

  /**
   * Returns the next note, ascending half a step.
   *
   * @return the next note in the scale, ascending
   */
  Note nextNote() {
    if (pitch == Pitch.B) {
      return new Note("C", octave + 1, 0, 0, 0, 50);
    }
    return new Note(getSymbol(Pitch.values()[pitch.ordinal() + 1]), octave, 0, 0, 0, 50);
  }

  /**
   * Get the length of the note, in beats.
   *
   * @return Llngth of note
   */
  public int getLength() {
    return length;
  }

  public int getStartBeat() {
    return startBeat;
  }

  public int getLastBeat() {
    return  lastBeat;
  }

  public int getInstrument() {
    return instrument;
  }

  public int getVolume() {
    return volume;
  }
}
