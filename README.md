# MusicEditor

OOD Music Editor by Sebastian Wisniowiecki and Eduardo Fares

TO RUN, TYPE THIS IN YOUR COMMAND LINE INTERFACE WHEN IN THE PROJECT DIRECTORY:
java -jar MusicEditor.jar (filename) (gui-type)

Running "zoot-lw.txt" with the "combined" gui view is recommended.

----------------------------------------
Changes from HW5 to MusicEditorModelImpl:

   Most of the changes come from merging the two models and adding accessor methods to preserve the
   encapsulation of our code.

   - ADDED CombineSim and CombineCon to add proper simultaneous and consecutive song addition to
   the model

   - ADDED fillcolumns to properly add any columns between the lowest note and highest note

   - CORRECTED update to respect scope of the class, delegated some of the work to the notecolumn
   class

   - ADDED getters and a setter method to allow most things to stay private in here

   - ADDED a builder for the model to make it compatible for this assignment and to make it easier
   to make a desired model.

   - ADDED better commenting and documentation :)

----------------------------------------

Changes from HW6 to Music Editor Views:

	- ADDED a keyboard handler for commands

	- ADDED a few setters and getters for easier exchange of data between controller and view

	- ADDED a combined view that incorporates the features of the MidiView and the SheetMusicView

	- ADDED support for commands in the controller

----------------------------------------

Changes from HW7:

About the design:

The music editor is designed in the Model-View-Controller design pattern.

----------------------------------------

Changes in HW8 view package for Supply Code:

-Changed GetInputString to GetInputStrings to remove requirement to parse input, as per customers' demands. This enabled
the customers to use our buttons and textfield.
-Added a resetFields to reset all the textfields once the necessary information is extracted
-Removed Thread.sleep() in MidiView as it was unnecessary and for a previous, unoptimized version of the code
-Made Beat constructor public 
-Added getter methods to Note instead of having fields as public
-Removed MockMidiView from actual MidiView into its own class so there is no link between the two

Changes in HW8 to adapt to Provided Code
-Main method takes in the song name followed by the view name.
-Added a ViewAdapter class to accomodate providers' views to our model and controller implementation.

----------------------------------------

---MODEL---

The model consists of a few classes - the model itself, Beat, Note, NoteColumn,
a ColumnComparator, and an enum for Pitch.

Purposes of the Classes:
Beat - To represent a played beat. This class is used for the "beat track" Has a boolean field isHead 
designating if it is the head of a note. Also has a field to associate it with the note that it is 
representing. If the object EXISTS, it is a played beat. A rest is represented with null, this is 
so that for-each loops are faster. 

Note - To represent a note with a pitch, ovtave, length, starting beat, and as will be needed later,
and instrument and a volume. The constructor also calculates a lastbeat field and updates it whenever
any changes are made to the note length.

NoteColumn - Okay, here's the weird part. The NoteColumn stores all the notes of one pitch, so there's
a separate NoteColumn for C#7, C#6, B3, and so on. The NoteColumn has a heading representing its pitch,
its pitch and octave, a list of notes it contains, and a "beat track". A beat track is simply a list of
beats in order of playing of when the note is played, making it easy to print the editor and represent
it a view. It looks a little something like this:

(Actually an arraylist, but for the sake of simplicity)
Beats[0]  Beats[1]  Beats[2]  Beats[3]  Beats[4]
             X         -         -        
  null      Beat      Beat      Beat      null
          isHead T  isHead F  isHead F

The NoteColumn also automatically merges any overlapping notes, neat, right? At least I think it's kinda neat...

ColumnComparator - A simple comparator using hashcodes to sort note columns according to their pitch.

Pitch - An enum that represents the actual pitches of the notes (e.g. C#, Db, E, F) and stores the symbol and the
ordinal (the order that they're in, from C = 0 to B = 11). It contains a whole bunch of utility methods to convert 
ordinals to pitch strings, strings to pitch objects, everything that is needed in the model computations. Also
handles enharmonic pitches such as B# -> C, Db -> C#, and so on so that all intermediate pitches are represented
as sharps.

---CONTROLLER---

Just created a stub controller to marry the model to the view.

StartEditor initializes the model and the view, and sends the model's initial data to the view. It also sets the
model's tempo

SendDataToView sends all the important data from the model for the view to use.

---VIEWS---

MidiView

Plays the song using the midi library. 
Takes in the beat tracks, if it reads the head of the note it sends an on message to the midi reciever, if it reads a
note but it's not head it wont send any message and if it reads a null it'll send a note off message.
The thread is slept between each beat in order to play in the right tempo.

Press space bar to play/pause, home to play from beginning, end to go to the end, left and right arrows keys to 
skip through measures.


SheetMusicView

A horizontal GUI view of the sheet music. Takes in the beats, note headings, number of beats, and measure length 
from the model. Uses the beat track to generate which note is played at each beat. A black block represents the 
head of a note, a green block represents a non-head note, and the background color represents no note played. 
The track is separated into groups of beats decided by the measure length. Also numbers the beginning beat of every 
measure.

Use the text field and buttons to edit the respective note values

Use the following formats in the text field:

Add Note - Pitch Octave StartBeat Length
Remove Note - Pitch Octave Beat
Edit Note - Pitch Octave EditValue

SheetMusicTextView

The same output as getSheetMusic in the model. Represents the beat track the same way as in SheetMusicView but 
represents a note head with a "X", a non-head note as "|", and no note as a whitespace. The music is shown 
vertically and outputted into the console. All the beats are numbered on the left.

CombinedView

Combines the features of the MidiView and the SheetMusicView. Click space bar to pause/play music as in the MidiView.
Use buttons and text field to edit notes as in the SheetMusicView. All keyboard commands will work the same as in their
respective views.


---PROVIDER---

The code in the provider package was supplied by another group that had the same assignment. In the View package, we
provide an adapter class that adapts our model and controller to the providers' views. 



