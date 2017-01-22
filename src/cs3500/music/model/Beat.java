package cs3500.music.model;

/**
 *  To represent a single, played beat, and whether it is the head of a note or not.
 */
public class Beat {
  Note notePartOf;
  boolean isHead;

  public Beat(Note notePartOf, boolean isHead) {
    this.notePartOf = notePartOf;
    this.isHead = isHead;
  }

  /**
   * Is this beat the head of a note.
   *
   * @return if this beat is a head
   */
  public boolean isHead() {
    return this.isHead;
  }

  /**
   * Returns the note that this beat is part of.
   *
   * @return the beat's note
   */
  public Note notePartOf() {
    return this.notePartOf;
  }
}
