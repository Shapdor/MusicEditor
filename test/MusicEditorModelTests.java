import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.Note;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Sebastian on 10/21/2016.
 */
public class MusicEditorModelTests {

  String baseEditorState = "    C2   D2   G2   A2 \n"
          + " 0            X       \n"
          + " 1       X    |       \n"
          + " 2  X    |    |       \n"
          + " 3  |    |    |    X  \n"
          + " 4            |    |  \n"
          + " 5                 |  \n"
          + " 6                 |  \n";

  List<Note> noteList = new ArrayList<>(Arrays.asList(new Note("A", 2, 0, 4, 3, 50),
          new Note("G", 2, 0, 5, 0, 50),
          new Note("B#", 2, 0, 2, 2, 50),
          new Note("D", 2, 0, 3, 1, 50)));

  @Test
  public void testInitMusicWithValidInput() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);

    assertEquals(baseEditorState, model.getSheetMusic());
  }

  /* Throws IllegalArgument Exception for trying to print sheet music for an empty song */
  @Test(expected = IllegalArgumentException.class)
  public void testInitMusicWithEmptySong_GetSheetMusic_ThrowsEx() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(new ArrayList<>(), 4, 100);
    model.getSheetMusic();

    fail("No exception was thrown");
  }

  @Test
  public void testInitMusicRestartsEditor() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);

    List<Note> newList = new ArrayList<>(Arrays.asList(new Note("A", 2, 0, 2, 2, 50)));
    model.initMusic(newList, 4, 100);

    assertNotEquals(baseEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_ExistingColumnDoesNotIncreaseNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("C", 2, 0, 2, 5, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5  X              |  \n"
            + " 6  |              |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_ExistingColumnIncreasesNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("C", 2, 0, 4, 5, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5  X              |  \n"
            + " 6  |              |  \n"
            + " 7  |                 \n"
            + " 8  |                 \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_NewColumnDoesNotIncreaseNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("C", 3, 0, 4, 3, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2   C3 \n"
            + " 0            X            \n"
            + " 1       X    |            \n"
            + " 2  X    |    |            \n"
            + " 3  |    |    |    X    X  \n"
            + " 4            |    |    |  \n"
            + " 5                 |    |  \n"
            + " 6                 |    |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_NewColumnIncreasesNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("C", 3, 0, 4, 5, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2   C3 \n"
            + " 0            X            \n"
            + " 1       X    |            \n"
            + " 2  X    |    |            \n"
            + " 3  |    |    |    X       \n"
            + " 4            |    |       \n"
            + " 5                 |    X  \n"
            + " 6                 |    |  \n"
            + " 7                      |  \n"
            + " 8                      |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_AddIdenticalNote_MakesNoChange() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("A", 2, 0, 4, 3, 50);
    model.addNote(addNote);

    assertEquals(baseEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_AddNoteInsideOfAnotherNote() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("G", 2, 0, 2, 2, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    X       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5                 |  \n"
            + " 6                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_AddNoteOnTopOfOtherNote_AddNoteLonger() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("G", 2, 0, 5, 2, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    X       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5            |    |  \n"
            + " 6            |    |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_AddNoteStretchingIntoExistingNote_ShorterThanSecondNote() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("D", 2, 0, 3, 0, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0       X    X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5                 |  \n"
            + " 6                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testAddNote_AddNoteStretchingIntoExistingNote_AddNoteLonger_IncreasesNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("A", 2, 0, 3, 6, 50);
    model.addNote(addNote);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5                 |  \n"
            + " 6                 X  \n"
            + " 7                 |  \n"
            + " 8                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testRemoveNoteSuccessfully() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("A", 2, 0, 3, 6, 50);
    model.addNote(addNote);
    model.removeNote("A", 2, 4);

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |       \n"
            + " 4            |       \n"
            + " 5                    \n"
            + " 6                 X  \n"
            + " 7                 |  \n"
            + " 8                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());

  }

  @Test
  public void testRemoveNoteSuccessfully_RemovesEmptyColumn_UpdatesRows() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    Note addNote = new Note("A", 2, 0, 3, 6, 50);
    model.addNote(addNote);
    model.removeNote("A", 2, 4);
    model.removeNote("A", 2, 6);

    String expectedEditorState = "    C2   D2   G2 \n"
            + " 0            X  \n"
            + " 1       X    |  \n"
            + " 2  X    |    |  \n"
            + " 3  |    |    |  \n"
            + " 4            |  \n";


    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  /* Throws IllegalArgumentException for note not found */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNote_NoteNotFound_ThrowsEx() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.removeNote("A", 124, 4);

    fail("Exception not thrown");
  }

  /* Throws IllegalArgumentException for note not found */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNote_NoteNotFound2_ThrowsEx() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.removeNote("F#", 2, 4);

    fail("Exception not thrown");
  }

  @Test
  public void testEditNote_LengthSuccessfully() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "length", "3");

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4  |         |    |  \n"
            + " 5                 |  \n"
            + " 6                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testEditNote_LengthSuccessfully_IncreasesNumBeats() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "length", "6");

    String expectedEditorState = "    C2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4  |         |    |  \n"
            + " 5  |              |  \n"
            + " 6  |              |  \n"
            + " 7  |                 \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testEditNote_LengthTo0_RemovesNote() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "length", "0");

    String expectedEditorState = "    D2   G2   A2 \n"
            + " 0       X       \n"
            + " 1  X    |       \n"
            + " 2  |    |       \n"
            + " 3  |    |    X  \n"
            + " 4       |    |  \n"
            + " 5            |  \n"
            + " 6            |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }


  @Test
  public void testEditNote_OctaveSuccessfully() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "octave", "1");

    String expectedEditorState = "    C1   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5                 |  \n"
            + " 6                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  @Test
  public void testEditNote_PitchSuccessfully() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "pitch", "C#");

    String expectedEditorState = "   C#2   D2   G2   A2 \n"
            + " 0            X       \n"
            + " 1       X    |       \n"
            + " 2  X    |    |       \n"
            + " 3  |    |    |    X  \n"
            + " 4            |    |  \n"
            + " 5                 |  \n"
            + " 6                 |  \n";

    assertEquals(expectedEditorState, model.getSheetMusic());
  }

  /* Throws exception for invalid field selection */
  @Test(expected = IllegalArgumentException.class)
  public void testEditNote_InvalidField_ThrowsEx() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    model.initMusic(noteList, 4, 100);
    model.editNote("C", 2, 2, "efbauf", "C#");

    fail("No exception was thrown");
  }

  /* Throws exception for illegal note in list */
  @Test(expected = IllegalArgumentException.class)
  public void testInitMusic_IllegalNote_ThrowsEx() {
    IMusicEditorModel model = new MusicEditorModelImpl();
    List<Note> newList = new ArrayList<>(noteList);
    newList.add(new Note("gfds", 342, 0, 432423, 432, 50));
    model.initMusic(noteList, 4, 100);

    fail("No exception was thrown.");
  }
}
