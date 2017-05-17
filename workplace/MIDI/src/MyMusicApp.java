import org.jfugue.*; 

public class MyMusicApp 
{ 
 public static void main(String[] args) 
 { 
 Player player = new Player(); 
 Pattern pattern = new Pattern("C D E F G A B"); 
 //player.play(pattern); 
 
 
 Rhythm rhythm = new Rhythm(); 
//Bang out your drum beat  
rhythm.setLayer(1, "O..oO...O..oOO.."); 
rhythm.setLayer(2, "..*...*...*...*."); 
rhythm.setLayer(3, "^^^^^^^^^^^^^^^^"); 
rhythm.setLayer(4, "...............!"); 

//Associate percussion notes with your beat  
rhythm.addSubstitution('O', "[BASS_DRUM]i"); 
rhythm.addSubstitution('o', "Rs [BASS_DRUM]s"); 
rhythm.addSubstitution('*', "[ACOUSTIC_SNARE]i"); 
rhythm.addSubstitution('^', "[PEDAL_HI_HAT]s Rs"); 
rhythm.addSubstitution('!', "[CRASH_CYMBAL_1]s Rs"); 
rhythm.addSubstitution('.', "Ri");   
//Play the rhythm!  
Pattern pattern1 = rhythm.getPattern(); 
//pattern1.repeat(4); 
Player player1 = new Player(); 
//player1.play(pattern1);

hiphopBeat();



//V 0-15 voice sheng dao
Pattern progression = new Pattern(); 
progression.add("V2 T160 I[Guitar] C5W+E5q+G5q"); 
progression.add("V0 T160 I[Guitar] c5majw g5majw a5minw f5majw g5majw c5majw"); 
progression.add("V1 T160 I[Piano] g5majw a5minw f5majw g5majw c5majw"); 
progression.add("V9 [Hand_Clap]q+[Crash_Cymbal_1]");
Player jukebox = new Player(); 
jukebox.play(progression);

//jukebox.save(progression,"c:/music/chord.mid"); 




//"Frere Jacques"
Pattern pattern_1 = new Pattern("C5q D5q E5q C5q");

//"Dormez-vous?"
Pattern pattern2 = new Pattern("E5q F5q G5h");

//"Sonnez les matines"
Pattern pattern3 = new Pattern("G5i A5i G5i F5i E5q C5q");

//"Ding ding dong"
Pattern pattern4 = new Pattern("C5q G4q C5h");

//Put it all together
Pattern song = new Pattern();
song.add(pattern_1);
song.add(pattern_1);
song.add(pattern2);
song.add(pattern2);
song.add(pattern3);
song.add(pattern3);
song.add(pattern4);
song.add(pattern4);

//Play the song!
//player.play(song);

//*****************************************************************
//The Rw Rw Music String represents two whole-note rests
Pattern doubleMeasureRest = new Pattern("Rw Rw");

//Create the first voice
Pattern round1 = new Pattern("V0 ");
round1.add(song);

//Create the second voice
Pattern round2 = new Pattern("V1 ");
round2.add(doubleMeasureRest);
round2.add(song);

//Create the third voice
Pattern round3 = new Pattern("V2 ");
round3.add(doubleMeasureRest);
round3.add(doubleMeasureRest);
round3.add(song);

//Put the voices together
Pattern roundSong = new Pattern();
roundSong.add(round1);
roundSong.add(round2);
roundSong.add(round3);

//Play the song!
player.play(roundSong);
	




// SOUND volume, + messsage layer playe together.  sounds fregents = 0.5s
// 4 notes
// 0.5 ARRAYLIST CONTANCT.
//message AID moveToSoundAgent  -> playAgent
//playAgent, : playbehaviour beijin  stop + 1,2,3 replay + change
//moveToSoundAgent request: Speed, position, volume, destination playAgent AID 
//				inform -> PlayAgent AID : note
//				playAgent array
// 				PlayAgent user-> background music

 System.exit(0); // If using Java 1.4 or lower 
 } 
 
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

     
 }
} 
