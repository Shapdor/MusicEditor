package cs3500.music.model;

/**
 * Represents a repetition in the music player.
 */
public class Repeats {
  public final boolean skip;
  private Segment seg;
  public boolean played;

  /**
   * Constructor.
   * @param seg Segment to repeat.
   * @param skip Represents if the player will skip the segment after looping.
   */
  public Repeats(Segment seg, boolean skip) {
    this.seg = seg;
    this.skip = skip;
    this.played = false;
  }

  /**
   * Returns the start of the segment to be repeated.
   * @return Start of the segment to be repeated.
   */
  public int getStart() {
    return seg.start;
  }

  /**
   * Returns the end of the segment to be repeated.
   * @return End of the segment to be repeated.
   */
  public int getEnd() {
    return seg.end;
  }

  /**
   * Toggles whether this repeat has been played or not as to not repeat indefinitely.
   */
  public void togglePlayed() {
    this.played = !this.played;
  }
}
