package cs3500.music.provider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michaelfleischmann on 10/15/16.
 */
public class Measure {
  private final Note note;
  private List<Beat> beats;

  /**
   * Constructs a {@code Measure} object. It is essentially the list of beats for a note.
   *
   * @param note the note of the measure
   */
  public Measure(Note note) {
    this.note = note;
    this.beats = new ArrayList<Beat>();
  }

  /**
   * Represents a measure.
   *
   * @param note the note that makes up the measure
   * @param beats how many beats the note lasts
   */
  public Measure(Note note, ArrayList<Beat> beats) {
    for (int i = 0; i < beats.size(); i++) {
      if (beats.get(0).getType().equals(BeatType.Sustain) ||
              (beats.get(i).equals(BeatType.Sustain) &&
                      (beats.get(i - 1).getType().equals(BeatType.Rest)))) {
        throw new IllegalArgumentException("Invalid Measure");
      }
    }
    this.note = note;
    this.beats = beats;
  }

  /**
   * Returns the number of Beats in a measure.
   *
   * @return the number of Beats in a measure
   */

  public Note getNote() {
    return note;
  }

  public List<Beat> getBeats() {
    return beats;
  }

  /**
   * Get the number of beats in the measure.
   *
   * @return the number of beats
   */
  public int numBeats() {
    int result = 0;
    for (Beat b : beats) {
      if (b.getType().equals(BeatType.Head)) {
        result++;
      }
    }
    return result;
  }

  /**
   * Finds the starting time of a beat number (from 1 - number of beats) in a measure.
   *
   * @param beatnumber the (counting from 1 - number of beats) beat number
   * @return the starting time of the beat number
   * @throws IllegalArgumentException if the beat number is higher that numBeats or lower than 1
   */
  public int findBeat(int beatnumber) {
    ArrayList<Integer> beatlist = new ArrayList<Integer>();
    for (int i = 0; i < beats.size(); i++) {
      if (beats.get(i).getType().equals(BeatType.Head)) {
        beatlist.add(i);
      }
    }
    if (beatnumber > beatlist.size() || beatnumber < 1) {
      throw new IllegalArgumentException("Invalid Beat Number");
    }
    return beatlist.get(beatnumber - 1);
  }

  /**
   * Creates and returns a HashMap of all the beats in the measure.
   *
   * @return a HashMap of beats
   */
  public HashMap<Integer, Integer> hashBeat() {
    HashMap<Integer, Integer> result = new HashMap<>();
    int value = 0;
    for (int i = 0; i < beats.size(); i++) {
      if (beats.get(i).getType().equals(BeatType.Head)) {
        value += 1;
        result.put(i, value);
      }
      if (beats.get(i).getType().equals(BeatType.Sustain)) {
        result.put(i, value);
      }
    }
    return result;
  }


  /**
   * Finds the last sustain of the beat specified.
   *
   * @param beat the (counting from 1 - number of beats) beat number
   * @return the ending time of the beat number
   */
  public int getBeatEnd(int beat) {
    int actualbeat = findBeat(beat) + 1;
    while (actualbeat != beats.size() && beats.get(actualbeat).getType().equals(BeatType.Sustain)) {
      actualbeat = actualbeat + 1;
    }
    return actualbeat - 1;

  }

  /**
   * Returns true if the space in the measure (time - time + duration)
   * does not contain another beat.
   *
   * @param duration the duration of the questioned space
   * @param time     the starting time of the beat
   * @return true or false depending on whether the beat is available
   * @throws IllegalArgumentException if the time is less than 0
   */
  public boolean unavailable(int duration, int time) {
    int end;
    boolean availability = false;
    if (duration + time >= beats.size()) {
      end = beats.size();
    } else {
      end = duration + time;
      for (int i = time; i < end; i++) {
        availability = availability || hashBeat().containsKey(i);
      }
    }
    return availability;
  }

  /**
   * Changes the instrument of the beat.
   *
   * @param beatnumber the number of the beat to be changed
   * @param instrument the number of the instrument to be used
   */
  public void changeBeatInstrument(int beatnumber, int instrument) {
    for (int i = findBeat(beatnumber); i <= getBeatEnd(beatnumber); i++) {
      beats.get(i).setInstrument(beats.get(i).getInstrument() + instrument);
    }
  }

  /**
   * Static method that creates a new measure with as many BeatType.Rest as there are number of
   * rows.
   *
   * @param note    the note of the measure
   * @param numrows the number of rows to be added
   * @return a new Measure with the note and specified number of rows
   */
  public static Measure newOffMeasure(Note note, int numrows) {
    ArrayList<Beat> result = new ArrayList<>();
    for (int i = 0; i < numrows; i++) {
      result.add(new Beat(BeatType.Rest));
    }
    return new Measure(note, result);
  }
}