package cs3500.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * To represent a track of notes for a specific note (e.g. C#5). Also constructs a "beat track" for
 * the column for processing of individual beats.
 */
class NoteColumn {
  String heading;
  List<Beat> beatTrack;
  private List<Note> notes;
  private final Pitch columnPitch;
  private final int columnOctave;

  NoteColumn(String columnNote, int numBeats, Pitch columnPitch, int columnOctave) {
    this.heading = columnNote;
    this.beatTrack = new ArrayList<>(Collections.nCopies(numBeats, null));
    this.notes = new ArrayList<>();
    this.columnPitch = columnPitch;
    this.columnOctave = columnOctave;
  }

  @Override
  public boolean equals(Object that) {
    if (!(that instanceof NoteColumn)) {
      return false;
    }
    NoteColumn other = (NoteColumn)that;
    return this.hashCode() == that.hashCode();
  }

  @Override
  public int hashCode() {
    return this.columnOctave * 12 + Pitch.getOrder(this.columnPitch);
  }


  /**
   * Gets all the notes in this column.
   *
   * @return notes in this column
   */
  List<Note> getNotes() {
    return new ArrayList<>(notes);
  }

  /**
   * Gets the beat for the note at the given beat number. Null if not played, or returns a beat
   * object if played at that beat.
   *
   * @param beatNum the beat to get
   * @return the beat at the beat number
   */
  Beat getBeatAt(int beatNum) {
    return this.beatTrack.get(beatNum);
  }

  /**
   * Adds the given note to this column.
   *
   * @param noteToAdd the note to add
   */
  void addNoteToColumn(Note noteToAdd) {
    if (noteToAdd.length == 0) {
      return;
    }
    if (noteToAdd.lastBeat > this.beatTrack.size() - 1) {
      updateTrack(noteToAdd.lastBeat + 1);
    }
    mergeNotes(noteToAdd);
    updateTrack();
  }

  /**
   * Removes given note from column if it exists.
   *
   * @param beatNumber beat number of the note
   */
  void removeNoteFromColumn(int beatNumber) {
    Beat selectedBeat = this.beatTrack.get(beatNumber);
    if (selectedBeat == null) {
      throw new IllegalArgumentException("No note at this beat.");
    }
    else {
      Note workingNote = selectedBeat.notePartOf;
      this.notes.remove(workingNote);
      updateTrack();
    }
  }

  /**
   * Edits the octave of the note at the given beat if it exists. Removes the note from this column
   * if the octave is different and adds it to the note's new respective column.
   *
   * @param beatNumber beat number of the note
   * @param editValue the octave to change the note to
   * @param model the working music editor model
   */
  void editOctave(int beatNumber, String editValue, IMusicEditorModel model) {
    int editOctave = Integer.parseInt(editValue);
    Note editNote = this.beatTrack.get(beatNumber).notePartOf;
    if (editOctave == editNote.octave) {
      return;
    }
    this.notes.remove(editNote);
    updateTrack();
    editNote.octave = editOctave;
    model.addNote(editNote);
  }

  /**
   * Edits the length of the note at the given beat if it exists. Readjusts the size of the
   * beatTrack to accomadate the note if it stretches past this column's last beat.
   *
   * @param beatNumber beat number of the note
   * @param editValue the octave to change the note to
   */
  void editLength(int beatNumber, String editValue) {
    int editLength = Integer.parseInt(editValue);
    Note editNote = this.beatTrack.get(beatNumber).notePartOf;
    if (editLength == editNote.length) {
      return;
    }
    if (editLength == 0) {
      this.removeNoteFromColumn(beatNumber);
      updateTrack();
      return;
    }
    this.notes.remove(editNote);
    editNote.setLength(editLength);
    if (editNote.lastBeat > this.beatTrack.size() - 1) {
      updateTrack(editNote.lastBeat);
    }
    else {
      updateTrack();
    }
    this.addNoteToColumn(editNote);
  }

  /**
   * Edits the pitch of the note at the given beat if it exists. Removes the note from this column
   * if the pitch is different and adds it to the note's new respective column.
   *
   * @param beatNumber beat number of the note
   * @param editValue the pitch to change the note to
   * @param model the working music editor model
   */
  void editPitch(int beatNumber, String editValue, IMusicEditorModel model) {
    Pitch editPitch = Pitch.getPitchFromString(editValue);
    Note editNote = this.beatTrack.get(beatNumber).notePartOf;
    if (editPitch.equals(editNote.pitch)) {
      return;
    }
    this.notes.remove(editNote);
    updateTrack();
    editNote.pitch = editPitch;
    model.addNote(editNote);
  }

  /**
   * Updates the beat track to reflect any changes in track length from any changes to the notes
   * in the column.
   *
   * @param trackSize a custom track length
   */
  void updateTrack(int trackSize) {
    this.beatTrack = new ArrayList<>(Collections.nCopies(trackSize, null));
    for (Note n : this.notes) {
      this.beatTrack.set(n.startBeat, new Beat(n, true));
      for (int i = 1; i < n.length; i++) {
        this.beatTrack.set(n.startBeat + i, new Beat(n, false));
      }
    }
  }

  /**
   * Updates the beat track to reflect any changes in track length from any changes to the notes
   * in the column. Decides track size from column's list of notes.
   */
  void updateTrack() {
    int numBeats = 0;
    for (Note n : this.notes) {
      if (n.lastBeat + 1 > numBeats) {
        numBeats = n.lastBeat + 1;
      }
    }
    updateTrack(numBeats);
  }

  /**
   * Handles any note merges if adding the given note would result in any overlaps. Edits the notes
   * such that there will be no overlaps and the desired output is preserved.
   *
   * @param noteToAdd the note to add to the column
   */
  private void mergeNotes(Note noteToAdd) {

    /* Case that note is added at a note head or has a note head in its duration */
    for (int i = 0; i < noteToAdd.length; i++) {
      Beat currBeat = this.beatTrack.get(noteToAdd.startBeat + i);
      if (currBeat != null && currBeat.isHead) {
        Note secondNote = currBeat.notePartOf;
        /* Handle if both notes have same note head beat */
        if (i == 0 && noteToAdd.length > secondNote.length) {
          this.notes.remove(secondNote);
          break;
        }
        else if (i == 0 && noteToAdd.length <= secondNote.length) {
          return;
        }
        /* Handle if the note to add extends past the first note */
        if (secondNote.lastBeat < noteToAdd.lastBeat) {
          secondNote.setLength(noteToAdd.lastBeat - secondNote.startBeat + 1);
        }
        noteToAdd.setLength(secondNote.startBeat - noteToAdd.startBeat);
        break;
      }
    }

    /* Case that note is added on top of an existing note, not same heads */
    if (this.beatTrack.get(noteToAdd.startBeat) != null) {
      Note prevNote = this.beatTrack.get(noteToAdd.startBeat).notePartOf;
      if (prevNote.lastBeat > noteToAdd.lastBeat) {
        noteToAdd.setLength(prevNote.lastBeat - noteToAdd.startBeat + 1);
      }
      prevNote.setLength(noteToAdd.startBeat - prevNote.startBeat);
    }

    this.notes.add(noteToAdd);
  }

  /**
   * Does this column contain any notes.
   *
   * @return if this note column is empty
   */
  boolean isEmpty() {
    return this.notes.isEmpty();
  }


  /**
   * Returns the note if its head is located at the given beat.
   *
   * @param beat the beat where the note would play
   * @return a note if the beat has a note head in the given beat, null otherwise
   */
  Note isPlaying(int beat) {
    Beat selectedBeat = this.beatTrack.get(beat);
    if (selectedBeat != null && selectedBeat.isHead) {
      return selectedBeat.notePartOf;
    }
    return null;
  }

  /**
   * Returns the heading of the column formatted to fit centered in a 5 character space.
   *
   * @return the formatted heading
   */
  String getFormattedHeading() {
    switch (this.heading.length()) {
      case 2:
        return "  " + this.heading + " ";
      case 3:
        return " " + this.heading + " ";
      case 4:
        return " " + this.heading;
      case 5:
        return this.heading;
      default:
        throw new IllegalArgumentException("There is an illegal column.");
    }
  }
}

