package cs3500.music.view.runnables;

import cs3500.music.view.IMusicEditorView;

/**
 * View's jumpToEnd runnable.
 */
public class JumpEnd implements Runnable {

  IMusicEditorView view;

  public JumpEnd(IMusicEditorView view) {
    this.view = view;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.</p>
   * @see Thread#run()
   */
  @Override
  public void run() {
    view.jumpToEnd();
  }
}
