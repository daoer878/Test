import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.jfugue.Instrument;
import org.jfugue.MusicStringParser;
import org.jfugue.Note;
import org.jfugue.Pattern;
import org.jfugue.PatternTransformer;
import org.jfugue.Player;
import org.jfugue.Rhythm;
import org.jfugue.extras.DurationPatternTransformer;
import org.jfugue.extras.GetPatternForVoiceTool;
import org.jfugue.extras.IntervalPatternTransformer;
import org.jfugue.extras.ReversePatternTransformer;

public class RemixMidi {

    private Player player;
    private File file;

    public RemixMidi(File file) {
        this.player = new Player();
        this.file = file;
    }

    /** Loads a MIDI file, and converts the MIDI into a JFugue Pattern. */
    public Pattern getPattern() {
        Pattern pattern = null;
        try {
            pattern = player.loadMidi(file);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pattern;
    }

    /**
     * Using the GetPatternForVoiceTool, isolate a single voice (or channel) of the
     * Pattern and return it.
     */
    public Pattern getPatternForVoice(Pattern pattern, int voice) {
        GetPatternForVoiceTool tool = new GetPatternForVoiceTool(voice);

        MusicStringParser parser = new MusicStringParser();
        parser.addParserListener(tool);
        parser.parse(pattern);
        Pattern voicePattern = tool.getPattern();

        return voicePattern;
    }

    /** Plays a Pattern - this is a pass-through method to JFugue's Player */
    public void play(Pattern pattern) {
        player.play(pattern);
    }

    /**
     * Using the InvertPatternTransformer, invert the given pattern.
     */
    public Pattern invertPattern(Pattern pattern) {
        InvertPatternTransformer ipt = new InvertPatternTransformer(MusicStringParser.getNote("C5"));
        Pattern invertPattern = ipt.transform(pattern);
        return invertPattern;
    }

    /**
     * Using the ReversePatternTransformer, reverse the given pattern.
     * "C D E" becomes "E D C"
     */
    public Pattern reversePattern(Pattern pattern) {
        ReversePatternTransformer rpt = new ReversePatternTransformer();
        Pattern reversePattern = rpt.transform(pattern);
        return reversePattern;
    }

    /**
     * Causes the duration of each note in the Pattern to be lengthened
     * by the provided factor.
     */
    public Pattern stretchPattern(Pattern pattern, double stretch) {
        DurationPatternTransformer dpt = new DurationPatternTransformer(stretch);
        Pattern stretchedPattern = dpt.transform(pattern);
        return stretchedPattern;
    }

    /**
     * Changes the note values of each note in the Pattern - this causes
     * the entire Pattern to be played with higher or lower pitches.   
     */
    public Pattern changeInterval(Pattern pattern, int delta) {
        IntervalPatternTransformer it = new IntervalPatternTransformer(delta);
        Pattern intervalPattern = it.transform(pattern);
        return intervalPattern;
    }

    public Pattern cleanInstrument(Pattern pattern) {
        PatternTransformer t = new PatternTransformer() {

            @Override
            public void instrumentEvent(Instrument instrument) {
                // Do nothing
            }
        };
        Pattern cleanedPattern = t.transform(pattern);
        return cleanedPattern;
    }

    private static void remixBach() {
        RemixMidi mix = new RemixMidi(new File("hiphopBb4.mid"));
        Pattern pattern = mix.getPattern();
        System.out.println("From midi: " + pattern);
        Pattern voicePattern = mix.getPatternForVoice(pattern, 0);

        Pattern cleanInstrumentPattern = mix.cleanInstrument(voicePattern);

        Pattern invertedPattern = mix.invertPattern(cleanInstrumentPattern);
        System.out.println("After inversion: " + invertedPattern);

        Pattern reversedPattern = mix.reversePattern(invertedPattern);
        System.out.println("After reversal: " + reversedPattern);
//        mix.play(reversedPattern);

//        reversedPattern = mix.stretchPattern(reversedPattern, 0.3);
        reversedPattern = mix.changeInterval(reversedPattern, +16);
//        mix.play(reversedPattern);
        System.out.println(reversedPattern);

        // Add pattern to itself, reversed again
        Pattern reversedAgain = mix.reversePattern(reversedPattern);
        reversedPattern.add(reversedAgain);

        // Repeat it
        reversedPattern.repeat(4);

        Pattern fugue = new Pattern();
        fugue.add("V0 I[Piano]");
        fugue.add(reversedPattern);
        fugue.add("V1 I[Synth_strings_1] R/0.637353");
        fugue.add(mix.changeInterval(reversedPattern, -12));
        fugue.add("V2 I[Blown_Bottle] R/0.637353 R/0.566652");
        fugue.add(mix.changeInterval(reversedPattern, -17));
        System.out.println(fugue);

        mix.play(fugue);

        try {
            fugue.savePattern(new File("fugue.jfugue"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	hiphopBeat();
        remixBach();
    }
//***************************//
    
    public static void hiphopBeat()
    {

        Rhythm rhythm = new Rhythm();

        // Each MusicString should have a total duration of an eighth note
        rhythm.addSubstitution('j', "<1>s Rs");
        rhythm.addSubstitution('k', "<6>s Rs");
        rhythm.addSubstitution('l', "<8>s Rs");
        rhythm.addSubstitution('m', "<9>s Rs");
        rhythm.addSubstitution('n', "<4>s Rs");
        rhythm.addSubstitution('o', "[BASS_DRUM]i");
        rhythm.addSubstitution('*', "[HAND_CLAP]s Rs");
        rhythm.addSubstitution('%', "[TAMBOURINE]s Rs");
        rhythm.addSubstitution('.', "Ri");

        rhythm.setLayer(3, "o...o...o...o...o...o...o...o...");
        rhythm.setLayer(4, "..*...*...*...*...*...*...*...*.");
        rhythm.setLayer(5, "...%...%...%...%...%...%...%...%");

        rhythm.setVoice(1, "jjnnjjmlnnllnnlkjjnnjjmlkkklnnnk");
        rhythm.setVoiceDetails(1, "I[CHOIR_AAHS]");

        Pattern pattern = rhythm.getPatternWithInterval("Bb4");
        pattern.insert("T95");

        Player player = new Player();
        player.play(pattern);

        try {
            player.saveMidi(pattern, new File("hiphopBb4.mid"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}


//***Sample PatternTransformer***
class InvertPatternTransformer extends PatternTransformer {

    private byte fulcrumNoteValue;

    public InvertPatternTransformer(Note note) {
        this.fulcrumNoteValue = note.getValue();
    }

    /** Transforms the given note */
    @Override
    public void noteEvent(Note note) {
        doNoteEvent(note);
    }

    /** Transforms the given note */
    @Override
    public void sequentialNoteEvent(Note note) {
        doNoteEvent(note);
    }

    /** Transforms the given note */
    @Override
    public void parallelNoteEvent(Note note) {
        doNoteEvent(note);
    }

    private void doNoteEvent(Note note) {
        byte noteValue = note.getValue();

        if (noteValue > fulcrumNoteValue) {
            note.setValue((byte) (fulcrumNoteValue - (noteValue - fulcrumNoteValue)));
            getReturnPattern().addElement(note);
        } else if (noteValue < fulcrumNoteValue) {
            note.setValue((byte) (fulcrumNoteValue - (fulcrumNoteValue - noteValue)));
            getReturnPattern().addElement(note);
        } else {
            //  No change in note value
            getReturnPattern().addElement(note);
        }
    }
    
    
    
    
    
    
}