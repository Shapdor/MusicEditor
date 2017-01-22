package cs3500.music.provider.model;

/**
 * Created by michaelfleischmann on 10/30/16.
 */
public enum BeatType {
  Head, Sustain, Rest;

  @Override
  public String toString() {
    switch (this) {
      case Head:
        return "  X  ";
      case Sustain:
        return "  |  ";
      case Rest:
        return "     ";
      default:
        throw new IllegalArgumentException("Nope");
    }

  }
}
