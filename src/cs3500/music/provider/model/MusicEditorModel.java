package cs3500.music.provider.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelfleischmann on 10/15/16.
 */
public final class MusicEditorModel implements IMusicEditorModel {
  private final List<Measure> piece;
  private int lowest;
  private int highest;
  private int numrows;

  /**
   * Constructs a {@code MusicEditorModel} object, the main model of the editor.
   */
  public MusicEditorModel() {
    this.piece = new ArrayList<>();
    this.numrows = 0;
  }

  /**
   * Retrieves the specific measure that coincides with the note given.
   *
   * @param note the note of the measure wanted
   * @return the measure in the piece
   */
  public Measure getMeasure(Note note) {
    for (Measure measure : piece) {
      if (measure.getNote().equals(note)) {
        return measure;
      }
    }
    throw new IllegalArgumentException("No such note");
  }

  @Override
  public List<Measure> getPiece() {
    return piece;
  }

  @Override
  public int getNumRows() {
    return numrows;
  }

  /**
   * Keeps re-establishing the number of rows that the model has based on the last sustain
   * in the model.
   */
  private void findNumRows() {
    int rows = 0;
    if (piece.size() == 0) {
      numrows = 0;
      return;
    }
    for (Measure measure : piece) {
      int rowMax = Math.max(measure.getBeats().lastIndexOf(new Beat(BeatType.Sustain)),
              measure.getBeats().lastIndexOf(new Beat(BeatType.Head)));
      rows = Math.max(rowMax, rows);
    }
    numrows = rows + 1;
  }

  /**
   * Personally, my favorite mutation. It readjusts the model after each method by making sure
   * The measures are equally sized, that the numrows is the number of the last sustain,
   * and that there are no empty measures at the lowest or highest note.
   */
  private void readjust() {
    findNumRows();
    for (Measure measure : piece) {
      while (measure.getBeats().size() < numrows) {
        measure.getBeats().add(new Beat(BeatType.Rest));
      }
      while (measure.getBeats().size() > numrows) {
        measure.getBeats().remove(measure.getBeats().size() - 1);
      }
    }
    if (piece.size() == 0) {
      return;
    }
    Measure last = piece.get(piece.size() - 1);
    Measure first = piece.get(0);
    while (!last.getBeats().contains(new Beat(BeatType.Head))) {
      piece.remove(last);
      last = piece.get(piece.size() - 1);
    }
    while (!first.getBeats().contains(new Beat(BeatType.Head))) {
      piece.remove(first);
      first = piece.get(0);
    }
    lowest = first.getNote().toInt();
    highest = last.getNote().toInt();

  }

  @Override
  public void addNote(Note note, int duration, int time, int instrument, int volume) {
    if (duration < 0 || time < 0 || note == null) {
      throw new IllegalArgumentException("Invalid Note Addition");
    }
    int sustain = time + duration;
    if (piece.size() == 0) {
      piece.add(new Measure(note));
      lowest = note.toInt();
      highest = note.toInt();
    }
    if (note.toInt() < lowest) {
      for (int i = 0; i < lowest - note.toInt(); i++) {
        piece.add(i, Measure.newOffMeasure(Note.intToNote(i + note.toInt()), numrows));
      }
      lowest = note.toInt();
    }
    if (note.toInt() > highest) {
      for (int i = highest + 1; i <= note.toInt(); i++) {
        piece.add(Measure.newOffMeasure(Note.intToNote(i), numrows));
      }
      highest = note.toInt();
    }
    Measure measure = getMeasure(note);
    if (numrows < time) {
      for (int i = numrows; i < time; i++) {
        measure.getBeats().add(new Beat(BeatType.Rest));
      }
    } else {
      for (int i = time; i < sustain; i++) {
        try {
          measure.getBeats().remove(time);
        } catch (IndexOutOfBoundsException e) {
          break;
        }
      }
    }
    if (duration > 0) {
      measure.getBeats().add(time, new Beat(BeatType.Head, instrument, volume));
    }
    for (int i = time + 1; i < sustain; i++) {
      measure.getBeats().add(i, new Beat(BeatType.Sustain, instrument, volume));
    }
    readjust();
  }

  @Override
  public void addNote(Note note, int duration, int time) {
    addNote(note, duration, time, 0, 0);
  }

  @Override
  public void edit(Note note, int beat, int editlength) {
    Measure measure = getMeasure(note);
    if (editlength > 0) {
      if (measure.unavailable(editlength, measure.getBeatEnd(beat) + 1)) {
        throw new IllegalArgumentException("Unavailable Space");
      }
      while (editlength > 0) {
        measure.getBeats().add(measure.findBeat(beat) + 1, new Beat(BeatType.Sustain,
                measure.getBeats().get(measure.findBeat(beat)).getInstrument(),
                measure.getBeats().get(measure.findBeat(beat)).getVolume()));
        try {
          measure.getBeats().remove(measure.getBeatEnd(beat) + 1);
        } catch (IndexOutOfBoundsException e) {
          //nothing here, because the point of this catch is to stop trying to
          //remove the Volumes, since there are none left to remove. After it is caught
          //the method moves on.
        }
        editlength--;
        readjust();
      }
    } else if (editlength < 0) {
      if (Math.abs(editlength) > measure.getBeatEnd(beat) - measure.findBeat(beat)) {
        throw new IllegalArgumentException("Beat already removed");
      }
      while (editlength < 0) {
        measure.getBeats().remove(measure.getBeatEnd(beat));
        measure.getBeats().add(measure.getBeatEnd(beat) + 1, new Beat(BeatType.Rest));
        editlength++;
        readjust();
      }
    } else {
      return;

    }

  }

  @Override
  public void remove(Note note, int beat) {
    Measure measure = getMeasure(note);
    int actualbeat = measure.findBeat(beat);
    try {
      edit(note, beat, actualbeat - measure.getBeatEnd(beat));
    } catch (IllegalArgumentException e) {
      //Provides a way to absorb the exception.
    }
    measure.getBeats().remove(actualbeat);
    measure.getBeats().add(actualbeat, new Beat(BeatType.Rest));
    readjust();
  }

  @Override
  public void move(Note note, int beat, Direction direction, int editlength) {
    if (editlength < 0 || direction == null) {
      throw new IllegalArgumentException("Invalid Move Parameters");
    }
    Measure measure = getMeasure(note);
    switch (direction) {
      case Right:
        if (measure.unavailable(editlength, measure.getBeatEnd(beat) + 1)) {
          throw new IllegalArgumentException("Inavailable Beat");
        }
        for (int i = 0; i < editlength; i++) {
          measure.getBeats().add(measure.findBeat(beat), new Beat(BeatType.Rest));
          try {
            measure.getBeats().remove(measure.getBeatEnd(beat) + 1);
          } catch (IndexOutOfBoundsException e) {
            //nothing here, because the point of this catch is to stop trying to
            //remove the Volumes, since there are none left to remove. After it is caught
            //the method moves on.
          }

        }
        readjust();
        break;
      case Left:
        if (measure.findBeat(beat) - editlength < 0 ||
                measure.unavailable(editlength, measure.findBeat(beat) - editlength)) {
          throw new IllegalArgumentException("Inavailable Beat");
        }
        for (int i = 0; i < editlength; i++) {
          measure.getBeats().remove(measure.findBeat(beat) - 1);
          measure.getBeats().add(measure.getBeatEnd(beat) + 1, new Beat(BeatType.Rest));
        }
        readjust();
        break;
      case Down:
        Note leftnote = Note.intToNote(note.toInt() - editlength);
        addNote(leftnote, measure.getBeatEnd(beat) - measure.findBeat(beat) + 1,
                measure.findBeat(beat), measure.getBeats().get(measure.findBeat(beat)).
                        getInstrument(),
                measure.getBeats().get(measure.findBeat(beat)).getVolume());
        remove(note, beat);
        break;
      case Up:
        Note rightnote = Note.intToNote(note.toInt() + editlength);
        addNote(rightnote, measure.getBeatEnd(beat) - measure.findBeat(beat) + 1,
                measure.findBeat(beat), measure.getBeats().get(measure.findBeat(beat)).
                        getInstrument(),
                measure.getBeats().get(measure.findBeat(beat)).getVolume());
        remove(note, beat);
        break;
      default:
        throw new IllegalArgumentException("Shouldn't get here");

    }
  }

  /**
   * Part of the musicstate, gets the top portion of the music state (just the notes).
   *
   * @return the notestate
   */

  private String getNoteState() {
    String result;
    if (numrows < 10) {
      result = "";
    } else {
      result = String.format("%1$" + (String.valueOf(numrows).length() - 1) + "s", " ");
    }
    for (Measure measure : piece) {
      result += measure.getNote().toString();

    }
    return result;
  }

  /**
   * Part of the musicstate, gets the bottom portion of the music state (just the measure beats).
   *
   * @return the beatstate
   */
  private String getBeatState(int r) {
    String result = String.format("%1$" + String.valueOf(numrows).length() + "s", r);
    for (Measure measure : piece) {
      result += measure.getBeats().get(r).toString();
    }
    return result;
  }

  /**
   * Returns the piece in String form.
   *
   * @return the music state of the editor
   */

  @Override
  public String getMusicState() {
    if (piece.size() == 0) {
      return "";
    }
    String result = getNoteState();
    for (int i = 0; i < numrows; i++) {
      result += "\n" +
              getBeatState(i);
    }
    return result;
  }
}



