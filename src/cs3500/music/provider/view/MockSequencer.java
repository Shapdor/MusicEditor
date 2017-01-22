package cs3500.music.provider.view;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * A Mock skeleton for MIDI Sequencer.
 */
public abstract class MockSequencer implements Sequencer {
  boolean isRunning;
  float tempoFactor;
  long tickLength;
  long tickPosn;

  /**
   * Starts playback of the MIDI data in the currently
   * loaded sequence.
   * Playback will begin from the current position.
   * If the playback position reaches the repeat end point,
   * and the repeat count is greater than 0, playback will
   * resume at the repeat start point for the number of
   * repetitions set with <code>setLoopCount</code>.
   * After that, or if the repeat count is 0, playback will
   * continue to play to the end of the sequence.
   *
   * <p>The implementation ensures that the synthesizer
   * is brought to a consistent state when jumping
   * to the repeat start point by sending appropriate
   * controllers, pitch bend, and program change events.
   *
   * @throws IllegalStateException if the <code>Sequencer</code> is closed.
   * @see #setLoopStartPoint
   * @see #setLoopEndPoint
   * @see #setLoopCount
   * @see #stop
   */
  @Override
  public void start() {
    isRunning = true;
  }

  /**
   * Stops recording, if active, and playback of the currently loaded sequence,
   * if any.
   *
   * @throws IllegalStateException if the <code>Sequencer</code> is closed.
   * @see #start
   * @see #isRunning
   */
  @Override
  public void stop() {
    isRunning = false;
  }

  /**
   * Indicates whether the Sequencer is currently running.  The default is <code>false</code>. The
   * Sequencer starts running when either <code>{@link #start}</code> or <code>{@link
   * #startRecording}</code> is called.  <code>isRunning</code> then returns <code>true</code> until
   * playback of the sequence completes or <code>{@link #stop}</code> is called.
   *
   * @return <code>true</code> if the Sequencer is running, otherwise <code>false</code>
   */
  @Override
  public boolean isRunning() {
    return isRunning;
  }


  /**
   * Scales the sequencer's actual playback tempo by the factor provided.
   * The default is 1.0.  A value of 1.0 represents the natural rate (the
   * tempo specified in the sequence), 2.0 means twice as fast, etc.
   * The tempo factor does not affect the values returned by
   * <code>{@link #getTempoInMPQ}</code> and <code>{@link #getTempoInBPM}</code>.
   * Those values indicate the tempo prior to scaling.
   *
   * <p>Note that the tempo factor cannot be adjusted when external
   * synchronization is used.  In that situation,
   * <code>setTempoFactor</code> always sets the tempo factor to 1.0.
   *
   * @param factor the requested tempo scalar
   * @see #getTempoFactor
   */
  @Override
  public void setTempoFactor(float factor) {
    this.tempoFactor = factor;
  }

  /**
   * Returns the current tempo factor for the sequencer.  The default is
   * 1.0.
   *
   * @return tempo factor.
   * @see #setTempoFactor(float)
   */
  @Override
  public float getTempoFactor() {
    return this.tempoFactor;
  }

  /**
   * Obtains the length of the current sequence, expressed in MIDI ticks,
   * or 0 if no sequence is set.
   *
   * @return length of the sequence in ticks
   */
  @Override
  public long getTickLength() {
    return this.tickLength;
  }

  /**
   * Obtains the current position in the sequence, expressed in MIDI
   * ticks.  (The duration of a tick in seconds is determined both by
   * the tempo and by the timing resolution stored in the
   * <code>{@link Sequence}</code>.)
   *
   * @return current tick
   * @see #setTickPosition
   */
  @Override
  public long getTickPosition() {
    return this.tickPosn;
  }

  /**
   * Sets the current sequencer position in MIDI ticks.
   *
   * @param tick the desired tick position
   * @see #getTickPosition
   */
  @Override
  public void setTickPosition(long tick) {
    this.tickPosn = tick;
  }

}
