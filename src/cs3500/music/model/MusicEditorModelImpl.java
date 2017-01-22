package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.music.util.CompositionBuilder;

import static cs3500.music.model.Pitch.getPitchFromString;
import static cs3500.music.model.Pitch.getSymbol;
import static cs3500.music.model.Pitch.getSymbolFromOrdinal;

/* CHANGES IN HOMEWORK 6:

   Most of the changes come from merging the two models and adding accessor methods to preserve the
   encapsulation of our code.

   - ADDED CombineSim and CombineCon to add proper simultaneous and consecutive song addition to
   the model

   - ADDED fillcolumns to properly add any columns between the lowest note and highest note

   - CORRECTED update to respect scope of the class, delegated some of the work to the notecolumn
   class

   - ADDED getters and a setter method to allow most things to stay private in here

   - ADDED a builder for the model to make it compatible for this assignment and to make it easier
   to make a desired model.

   - ADDED better commenting and documentation :)
 */

/**
 * To represent a music editor with the ability to add, remove, or edit notes. Edits one song at a
 * time. Represents notes with their pitch and octave number (e.g. "C#6"), and converts them to a
 * "beat track". The editor also specifies a measure length, a tempo, and keeps track of the current
 *  playing beat.
 */
public final class MusicEditorModelImpl implements IMusicEditorModel {
  private int numBeats;
  private List<NoteColumn> noteColumns;
  private int currBeat;
  private int tempo;
  private int measureLength;
  private List<Repeats> repeats;

  /** Builder class to enable easy construction of the music editor. Allows setting of tempo and
   * adding of individual notes before initializing the editor.
   */
  public static final class Builder implements CompositionBuilder<IMusicEditorModel> {
    List<Note> notesToAdd = new ArrayList<>();

    /* Default values */
    int tempo = 100000;
    int measureLength = 4;

    @Override
    public IMusicEditorModel build() {
      MusicEditorModelImpl model = new MusicEditorModelImpl();
      model.initMusic(notesToAdd, measureLength, tempo);
      return model;
    }

    @Override
    public CompositionBuilder<IMusicEditorModel> setTempo(int tempo) {
      if (tempo < 1) {
        throw new IllegalArgumentException("Tempo must be at least 1.");
      }
      this.tempo = tempo;
      return this;
    }

    @Override
    public CompositionBuilder<IMusicEditorModel> addNote(int start, int end, int instrument,
                                                         int pitch, int volume) {
      String noteSymbol = getSymbolFromOrdinal(pitch);
      int octave = (int) Math.floor(pitch / 12);
      int length = end - start + 1;

      notesToAdd.add(new Note(noteSymbol, octave, instrument, length, start, volume));

      return this;
    }
  }

  /**
   * Initializes model field values to default values and sets the measure length to the
   * common time signature, or, 4.
   */
  public MusicEditorModelImpl() {
    this.numBeats = 0;
    this.noteColumns = new ArrayList<>();
    this.repeats = new ArrayList<>();
    this.currBeat = 0;
    this.tempo = 100000;
    this.measureLength = 4;
  }

  @Override
  public void initMusic(List<Note> notes, int measureLength, int tempo) {
    this.noteColumns = new ArrayList<>();
    this.tempo = tempo;
    this.measureLength = measureLength;
    this.currBeat = 0;

    List<Note> notesToAdd = new ArrayList<>();
    for (Note n : notes) {
      notesToAdd.add(new Note(getSymbol(n.pitch), n.octave, n.instrument, n.length, n.startBeat,
              n.volume));
    }

    for (Note n : notesToAdd) {
      if (n.lastBeat + 1 > this.numBeats) {
        this.numBeats = n.lastBeat + 1;
      }
      this.addNote(n);
    }
  }

  @Override
  public void addNote(Note noteToAdd) {
    /* If the note column exists, add the note to it */
    NoteColumn column = columnMatcher(noteToAdd.toString());
    if (column != null) {
      column.addNoteToColumn(noteToAdd);
    } else {
      /* Add the column for the note because doesn't exist yet */
      NoteColumn newColumn = new NoteColumn(noteToAdd.toString(), this.numBeats, noteToAdd.pitch,
              noteToAdd.octave);
      newColumn.addNoteToColumn(noteToAdd);
      this.noteColumns.add(newColumn);
    }
    update();
  }

  /**
   * Adds the given repetition to the current song.
   *
   * @param repeats the repetition to add.
   */
  @Override
  public void addRepetition(Repeats repeats) {
    this.repeats.add(repeats);
  }

  @Override
  public void removeNote(String pitchString, int noteOctave, int beatNumber) {
    Pitch notePitch = getPitchFromString(pitchString);
    NoteColumn column = columnMatcher(getSymbol(notePitch) + noteOctave);
    if (column == null) {
      throw new IllegalArgumentException("Note not found.");
    }
    column.removeNoteFromColumn(beatNumber);
    update();
  }


  @Override
  public void editNote(String pitchString, int noteOctave, int beatNumber, String fieldToEdit,
                       String editValue) {

    /* Find the note's respective column */
    Pitch notePitch = getPitchFromString(pitchString);
    NoteColumn workingColumn = columnMatcher(getSymbol(notePitch) + noteOctave);
    if (workingColumn == null) {
      throw new IllegalArgumentException("Note not found.");
    }

    /* Execute appropriate action */
    switch (fieldToEdit.toLowerCase()) {
      case "length" :
        workingColumn.editLength(beatNumber, editValue);
        break;
      case "octave" :
        workingColumn.editOctave(beatNumber, editValue, this);
        break;
      case "pitch" :
        workingColumn.editPitch(beatNumber, editValue, this);
        break;
      default :
        throw new IllegalArgumentException("Invalid editing field.");
    }
    update();
  }

  @Override
  public void combineCon(List<Note> notes) {
    List<Note> allNotes = getNotes();

    int lastBeat = 0;

    for (Note n : allNotes) {
      lastBeat = Math.max(lastBeat, n.lastBeat);
    }

    lastBeat += 1;

    for (Note n : notes) {
      this.addNote(new Note(getSymbol(n.pitch), n.octave, n.instrument, n.length,
              lastBeat + n.startBeat, n.volume));
    }
  }

  @Override
  public void combineSim(List<Note> notes) {
    for (Note n : notes) {
      this.addNote(n);
    }
  }

  @Override
  public String getSheetMusic() {

    if (this.noteColumns.isEmpty()) {
      throw new IllegalArgumentException("There are no notes to display.");
    }

    StringBuilder output = new StringBuilder();

    /* First row */
    output.append("  ");
    for (NoteColumn c : this.noteColumns) {
      output.append(c.getFormattedHeading());
    }
    output.append('\n');

    /* Print out beat track */
    for (int i = 0; i < numBeats; i++) {
      output.append(String.format("%2d", i));
      for (NoteColumn c : this.noteColumns) {
        if (c.beatTrack.get(i) == null) {
          output.append("     ");
        } else if (c.beatTrack.get(i).isHead) {
          output.append("  X  ");
        } else {
          output.append("  |  ");
        }
      }
      output.append('\n');
    }
    return output.toString();
  }

  @Override
  public int getTempo() {
    return tempo;
  }

  @Override
  public List<Note> getPlayingNotes(int beat) {
    List<Note> notes = new ArrayList<>();

    for (NoteColumn nc : noteColumns) {
      Note note = nc.isPlaying(beat);

      if (note != null) {
        notes.add(note);
      }
    }
    return notes;
  }

  /**
   * Gets the total length of the music sheet, or, number of beats.
   * @return the length of the sheet music
   */
  @Override
  public int getLength() {
    int length = 0;

    for (Note note : getNotes()) {
      length = Math.max(length, note.startBeat + note.length);
    }
    return length;
  }

  @Override
  public List<List<Beat>> getBeats() {
    List<List<Beat>> beats = new ArrayList<>();
    for (int i = 0; i < this.numBeats; i++) {
      List<Beat> beatsAtBeatI = new ArrayList<>();
      for (NoteColumn c : this.noteColumns) {
        beatsAtBeatI.add(c.getBeatAt(i));
      }
      beats.add(beatsAtBeatI);
    }
    return beats;
  }

  @Override
  public List<Note> getNotes() {
    List<Note> notes = new ArrayList<>();

    for (NoteColumn c : noteColumns) {
      notes.addAll(c.getNotes());
    }

    return notes;
  }

  @Override
  public int getNumBeats() {
    return this.numBeats;
  }

  @Override
  public int getMeasureLength() {
    return this.measureLength;
  }

  @Override
  public void setMeasureLength(int length) {
    if (length < 1) {
      throw new IllegalArgumentException("Measure length must be at least 1");
    }
    this.measureLength = length;
  }

  @Override
  public List<String> getHeadings() {
    List<String> headings = new ArrayList<>();
    for (NoteColumn c : noteColumns) {
      headings.add(c.heading);
    }
    return headings;
  }

  /**
   * Gets the repetitions of the song
   *
   * @return list of repetitions in the song
   */
  @Override
  public List<Repeats> getRepetitions() {
    return this.repeats;
  }

  /**
   * Updates the last beat values of the model and its columns to trim the length of the beat tracks
   * and remove any empty, unneeded note columns.
   */
  private void update() {
    List<NoteColumn> listToRemove = new ArrayList<>();
    this.numBeats = 0;
    for (NoteColumn c : this.noteColumns) {
      c.updateTrack();
      int colBeats = c.beatTrack.size();
      if (colBeats > this.numBeats) {
        this.numBeats = colBeats;
      }
    }

    for (NoteColumn c : this.noteColumns) {
      if (c.isEmpty()) {
        listToRemove.add(c);
      }
      else {
        c.updateTrack(this.numBeats);
      }
    }

    for (NoteColumn c : listToRemove) {
      this.noteColumns.remove(c);
    }

    fillColumns();
    this.noteColumns.sort(new ColumnComparator());
  }

  /**
   * Matches the given heading to the respective note column
   *
   * @param heading the heading of the note (e.g. "C#7")
   * @return the respective note column, or null if it doesn't exist
   */
  private NoteColumn columnMatcher(String heading) {
    NoteColumn matchedColumn = null;
    for (NoteColumn c : this.noteColumns) {
      if (heading.equals(c.heading)) {
        matchedColumn = c;
      }
    }
    return matchedColumn;
  }

  /**
   * Adds any intermediate note columns between the lowest and the highest notes, if they don't
   * exist.
   */
  private void fillColumns() {

    if (this.getNotes().isEmpty()) {
      return;
    }
    // Get the smallest and biggest notes
    List<Note> notes = getNotes();
    Note min = notes.get(0);
    Note max = notes.get(0);

    for (Note note : notes) {
      if (min.getRealPitch() > note.getRealPitch()) {
        min = note;
      }
      if (max.getRealPitch() < note.getRealPitch()) {
        max = note;
      }
    }

    Note curr = min;

    while (!(curr.toString().equals(max.toString()))) {
      NoteColumn column = columnMatcher(curr.toString());
      if (column == null) {
        this.noteColumns.add(new NoteColumn(curr.toString(),
                this.numBeats, curr.pitch, curr.octave));
      }
      curr = curr.nextNote();
    }
  }
}




