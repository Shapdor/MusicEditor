package cs3500.music.model;

import java.util.List;

/**
 * To represent a music editor with the ability to add, remove, or edit notes. Edits one song at a
 * time. Represents notes with their pitch and octave number (e.g. "C#6"), and converts them to a
 * "beat track". The editor also specifies a measure length, a tempo, and keeps track of the current
 *  playing beat.
 */
public interface IMusicEditorModel {

  /**
   * Initializes the music editor with the given song(s) and the measure length. Adds all the notes
   * to the editor and readies it for editing. If more than one song is provided, combines the songs
   * and inserts them into the editor.
   *
   * @param notes a collection of lists of notes, each to represent a song
   * @param measureLength the length of the song's measures in beats
   */
  void initMusic(List<Note> notes, int measureLength, int tempo);

  /**
   * Add the given note to the current song.
   *
   * @param noteToAdd the note to add to the song
   */
  void addNote(Note noteToAdd);

  /**
   * Adds the given repetition to the current song.
   * @param repeats the repetition to add.
   */
  void addRepetition(Repeats repeats);

  /**
   * Remove the note that occupies the given beat number (does not have to be the head!) in the
   * given pitch and octave.
   *
   * @param pitchString the pitch of the targeted note
   * @param noteOctave the octave of the targeted note
   * @param beatNumber the beat that the targeted note takes up
   * @throws IllegalArgumentException if given note is not found
   */
  void removeNote(String pitchString, int noteOctave, int beatNumber);

  /**
   * Edit one of the fields of the the note at the given pitch, octave, and beat with the value
   * given from editValue.
   *
   * <p>The possible fields to edit are:
   * - "Pitch"
   * - "Length"
   * - "Octave"</p>
   *
   * @param pitchString the pitch of the targeted note
   * @param noteOctave the octave of the targeted note
   * @param beatNumber the beat that the targeted note takes up
   * @param fieldToEdit the field to change
   * @param editValue the value to change the fieldToEdit to
   * @throws IllegalArgumentException if given note is not found
   * @throws IllegalArgumentException if an invalid field to edit is given
   */
  void editNote(String pitchString, int noteOctave, int beatNumber, String fieldToEdit,
                String editValue);

  /**
   * Combines the given notes with the editor's current notes so that the given song plays
   * immediately after the current song ends.
   *
   * @param notes notes to merge
   */
  void combineCon(List<Note> notes);

  /**
   * Combines the given notes with the editor's current notes so that they play simultaneously.
   *
   * @param notes notes to merge
   */
  void combineSim(List<Note> notes);

  /**
   * Represents the current song as a formatted string, giving each note its own track and numbering
   * every beat of the song. Labels the head of a note as "X", a non-head note beat as "|" and a
   * rest as a blank.
   * @return the formatted string representing the current song
   */
  String getSheetMusic();

  /**
   * Gets the current tempo of the song in beats per minute (BPM).
   *
   * @return tempo in BPM
   */
  int getTempo();

  /**
   * Gets the notes that are going to be played at the given beat (Only returns note heads at
   * current beat).
   *
   * @param beat the beat to look for notes
   * @return the notes at the given beat that are at their head
   */
  List<Note> getPlayingNotes(int beat);

  /**
   * Gets the total length of the song in beats (total number of beats).
   *
   * @return the number of beats in the song
   */
  int getLength();

  /**
   * Gets the entire "beat track", a by-beat representation of the song's current notes.
   *
   * @return the song's beat track
   */
  List<List<Beat>> getBeats();

  /**
   * Gets all the notes currently in the song.
   *
   * @return the collection of this song's notes
   */
  List<Note> getNotes();

  int getNumBeats();

  /**
   * Gets the editor's set measure length, in beats.
   *
   * @return the length of each measure
   */
  int getMeasureLength();

  /**
   * Gets the editor's set measure length, in beats.
   *
   * @param length the new measure length
   */
  void setMeasureLength(int length);

  /**
   * Gets the headings of every note (e.g. "C#7") in the song.
   *
   * @return the headings of every note in the song
   */
  List<String> getHeadings();

  /**
   * Gets the repetitions of the song
   * @return list of repetitions in the song
   */
  List<Repeats> getRepetitions();
}
