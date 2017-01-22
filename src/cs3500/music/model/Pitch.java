package cs3500.music.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * To represent a note pitch in half steps. Converts all enharmonic flat pitches to their
 * corresponding sharp pitches (e.g. Db = C#).
 */
enum Pitch {
  C("C", 0),
  CSHARP("C#", 1),
  D("D", 2),
  DSHARP("D#", 3),
  E("E", 4),
  F("F", 5),
  FSHARP("F#", 6),
  G("G", 7),
  GSHARP("G#", 8),
  A("A", 9),
  ASHARP("A#", 10),
  B("B", 11);

  private String symbol;
  private int order;

  private static Map<String, Pitch> enharmonics;
  static {
    Map<String, Pitch> inputMap = new HashMap<>();
    inputMap.put("B#", Pitch.C);
    inputMap.put("E#", Pitch.F);
    inputMap.put("Cb", Pitch.B);
    inputMap.put("Fb", Pitch.E);
    inputMap.put("Eb", Pitch.DSHARP);
    inputMap.put("Db", Pitch.CSHARP);
    inputMap.put("Bb", Pitch.ASHARP);
    inputMap.put("Ab", Pitch.GSHARP);
    inputMap.put("Gb", Pitch.FSHARP);
    enharmonics = Collections.unmodifiableMap(inputMap);
  }

  Pitch(String symbol, int order) {
    this.symbol = symbol;
    this.order = order;
  }

  /**
   * Gets the pitch symbol from a given note ordinal.
   *
   * @param ordinal the note ordinal (e.g. 13 = C#)
   * @return the corresponding pitch symbol
   */
  static String getSymbolFromOrdinal(int ordinal) {
    int pitchOrdinal = ordinal % 12;
    Pitch pitch = getPitchFromOrdinal(pitchOrdinal);
    return getSymbol(pitch);
  }

  /**
   * Returns the pitch from a given note ordinal.
   *
   * @param ordinal the note ordinal (e.g. 13 = C#)
   * @return the corresponding pitch
   */
  static Pitch getPitchFromOrdinal(int ordinal) {
    int modOrdinal = ordinal % 12;
    Pitch output = null;
    for (Pitch p : Pitch.values()) {
      if (p.order == modOrdinal) {
        output = p;
      }
    }
    return output;
  }

  /**
   * Returns the pitch from a given note string (e.g. "F#"). Adjusts enharmonic tones to sharps.
   *
   * @param inputPitch the pitch string
   * @return the corresponding pitch
   */
  static Pitch getPitchFromString(String inputPitch) {
    for (Pitch p : Pitch.values()) {
      if (p.symbol.equals(inputPitch)) {
        return p;
      }
    }
    if (enharmonics.containsKey(inputPitch)) {
      return (enharmonics.get(inputPitch));
    } else {
      throw new IllegalArgumentException("Invalid pitch.");
    }
  }

  /**
   * Gets the order of the pitch (C = 0, B = 11).
   *
   * @param p the pitch
   * @return the order of the pitch
   */
  static int getOrder(Pitch p) {
    return p.order;
  }

  /**
   * Gets the symbol of the pitch (e.g. "B")
   *
   * @param p the pitch
   * @return the pitch's symbol
   */
  static String getSymbol(Pitch p) {
    return p.symbol;
  }
}