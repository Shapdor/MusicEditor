package cs3500.music.provider.model;

/**
 * Created by michaelfleischmann on 10/15/16.
 */
public enum Pitch {
  C(0, "C"), Cs(1, "C#"), D(2, "D"), Ds(3, "D#"), E(4, "E"), F(5, "F"), Fs(6, "F#"), G(7, "G"),
  Gs(8, "G#"), A(9, "A"), As(10, "A#"), B(11, "B");
  private final int pitchnum;
  private final String thepitch;

  Pitch(int pitchnum, String thepitch) {
    this.pitchnum = pitchnum;
    this.thepitch = thepitch;
  }

  /**
   * Returns the pitch number.
   *
   * @return the pitch number
   */
  public int toInt() {
    return pitchnum;
  }

  /**
   * Takes an int and returns a pitch that coincides with it.
   *
   * @param i the int to be worked with
   * @return a pitch
   */
  public static Pitch intToPitch(int i) {
    switch (i) {
      case 0:
        return C;
      case 1:
        return Cs;
      case 2:
        return D;
      case 3:
        return Ds;
      case 4:
        return E;
      case 5:
        return F;
      case 6:
        return Fs;
      case 7:
        return G;
      case 8:
        return Gs;
      case 9:
        return A;
      case 10:
        return As;
      case 11:
        return B;
      default:
        throw new IllegalArgumentException("Invalid Pitch");
    }
  }

  @Override
  public String toString() {
    return thepitch;
  }


}
