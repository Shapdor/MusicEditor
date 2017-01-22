package cs3500.music.provider.model;

import java.util.List;

/**
 * Created by michaelfleischmann on 10/27/16.
 */
public interface IMusicEditorModel {

  /**
   * returns the piece, which includes all the information of the model.
   *
   * @return the piece
   */
  List<Measure> getPiece();

  /**
   * returns the number of rows in the piece.
   *
   * @return the number of rows in the piece
   */
  int getNumRows();

  /**
   * Adds a beat with an instrument to the model. If the note is higher or lower than all the notes
   * already present, it creates new measures that have the same numrows as the present measures.
   * it then adds a beat to the specific measure that contains the note. Then it readjusts.
   *
   * @param note       the note that will be added
   * @param duration   the duration of the note
   * @param time       the timing of the note inside the piece
   * @param instrument the instrument that will play with the beat
   * @throws IllegalArgumentException if given invalid input
   */
  void addNote(Note note, int duration, int time, int instrument, int volume)
          throws IllegalArgumentException;

  void addNote(Note note, int duration, int time) throws IllegalArgumentException;

  /**
   * Edits a given note. If the editlength is positive, it adds sustains to the note. If it is
   * negative, it removes a given amount of sustains. It replaces these lost sustains with
   * Rest.
   *
   * @param note       the note to be edited
   * @param beat       the specific beat that will be edited (numbered from 1 - the number of
   *                   beats)
   * @param editlength the length of the edit, or rather, how many sustains to be added or removed
   * @throws IllegalArgumentException if adding to the beat takes away from another beat or if the
   *                                  user is trying to remove more sustains than there are in the
   *                                  beat.
   */
  void edit(Note note, int beat, int editlength) throws IllegalArgumentException;

  /**
   * Removes a beat completely from the piece.
   *
   * @param note the note to be removed
   * @param beat the exact beat to be removed (from 1 - number of beats in measure)
   */
  void remove(Note note, int beat);

  /**
   * Moves a beat either Upwards(later start time), Downwards(earlier start time), to the Left
   * (Lower octave or pitch), or to the Right(Higher octave or pitch).
   *
   * @param note       The note of the measure
   * @param beat       The specific beat to be moved
   * @param direction  Up, Down, Left, or Right
   * @param editlength the length that the beat will be moved
   * @throws IllegalArgumentException if adding to the beat takes away from another beat or if the
   *                                  editlength is negative, or if the direction is null.
   */
  void move(Note note, int beat, Direction direction, int editlength)
          throws IllegalArgumentException;

  /**
   * Returns the piece in String form.
   *
   * @return the music state of the editor
   */
  String getMusicState();

}
