package cs3500.music.model;

/**
 * Represents a segment in the music editor.
 */
public class Segment {

  public final int start, end;

  /**
   * Constructor.
   * @param start Where the segment starts.
   * @param end Where the segment ends.
   */
  public Segment(int start, int end) {
    if (start >= end) {
      throw new IllegalArgumentException("Start should be less than end");
    } else {
      this.start = start + 1;
      this.end = end + 1;
    }
  }
}
