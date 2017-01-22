import junit.framework.TestCase;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import cs3500.music.controller.IMusicEditorController;
import cs3500.music.controller.MusicEditorControllerImpl;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.Note;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IMusicEditorView;
import cs3500.music.view.MidiView;
import cs3500.music.view.mock.MockMidiView;
import cs3500.music.view.mock.MockSheetMusicView;
import cs3500.music.view.SheetMusicTextView;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 11/8/2016.
 */
public class MusicEditorInputFileTests {

  @Test
  public void testSheetMusicTextView1_() {

    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();

    try {
      IMusicEditorModel model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
              builder);
      SheetMusicTextView view = new SheetMusicTextView();
      IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
      controller.startEditor(4);
      assertEquals(view.getSheetMusic(), model.getSheetMusic());
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /* Throws an IllegalArgumentException as the file contains no notes */
  @Test(expected = IllegalArgumentException.class)
  public void testSheetMusicTextView2_EmptyFile_ThrowsEx() {
    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();
    try {
      IMusicEditorModel model = MusicReader.parseFile(new FileReader("test1.txt"),
              builder);
      SheetMusicTextView view = new SheetMusicTextView();
      IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
      controller.startEditor(4);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    fail("No exception was thrown");
  }

  @Test
  public void testSheetMusicTextView3() {
    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();

    String expectedEditorState = "    G4  G#4   A4  A#4   B4   C5  C#5   D5  D#5   E5 \n"
            + " 0  X                                            X  \n"
            + " 1  |                                            |  \n"
            + " 2  |                                  X         |  \n"
            + " 3  |                                  |            \n"
            + " 4  |                        X         |            \n"
            + " 5  |                        |                      \n"
            + " 6  |                        |         X            \n"
            + " 7  |                                  |            \n"
            + " 8  X                                  |         X  \n"
            + " 9  |                                            |  \n"
            + "10  |                                            |  \n"
            + "11  |                                               \n"
            + "12  |                                               \n"
            + "13  |                                               \n"
            + "14  |                                               \n"
            + "15  |                                               \n";
    try {
      IMusicEditorModel model = MusicReader.parseFile(new FileReader("test2.txt"),
              builder);
      SheetMusicTextView view = new SheetMusicTextView();
      IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
      controller.startEditor(4);
      TestCase.assertEquals(expectedEditorState, view.getSheetMusic());
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @Test
  public void midiTest() {
    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();

    IMusicEditorModel model = new MusicEditorModelImpl();
    try {
      model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"), builder);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    StringBuilder log = new StringBuilder();
    IMusicEditorView view = new MockMidiView(log);

    IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
    controller.startEditor(4);

    StringBuilder mockStringBuilder = new StringBuilder();

    List<Note> notes = model.getNotes();
    for (Note n : notes) {
      mockStringBuilder.append(n.getRealPitch() + " ");
    }

    // Transform to an array

    List<Integer> mock = new ArrayList<>();
    List<Integer> actual = new ArrayList<>();

    Scanner m = new Scanner(log.toString());
    Scanner a = new Scanner(mockStringBuilder.toString());

    while (m.hasNextInt()) {
      mock.add(m.nextInt());
      actual.add(a.nextInt());
    }

    // Sort the arrays
    Collections.sort(mock);
    Collections.sort(actual);

    TestCase.assertEquals(mock, actual);
  }

  @Test
  public void isPlayingTest() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    Note note = new Note("C", 4, 0, 2, 1, 50);
    model.addNote(note);


    assertTrue(model.getPlayingNotes(1).contains(note));
    assertFalse(model.getPlayingNotes(2).contains(note));
    assertFalse(model.getPlayingNotes(0).contains(note));
  }

  @Test
  public void sheetMusicViewTest() {
    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();

    IMusicEditorModel model = new MusicEditorModelImpl();
    try {
      model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"), builder);
      MockSheetMusicView view = new MockSheetMusicView();
      IMusicEditorController controller = new MusicEditorControllerImpl(model, view);
      controller.startEditor(4);
      assertEquals(view.numOfPaints(), 0);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }


  }
}

