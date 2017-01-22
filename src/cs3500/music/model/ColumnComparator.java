package cs3500.music.model;

import java.util.Comparator;

/** Comparator for sorting note columns with respect to pitch and octave.
 * Note order: C C♯ D D♯ E F F♯ G G♯ A A♯ B.
 */
class ColumnComparator implements Comparator<NoteColumn> {
  @Override
  public int compare(NoteColumn c1, NoteColumn c2) {
    return c1.hashCode() - c2.hashCode();
  }
}