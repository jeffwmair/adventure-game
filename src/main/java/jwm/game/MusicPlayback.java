package jwm.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MusicPlayback implements Runnable 
{
	   private SourceDataLine line;
       private Thread thread;
       private AudioInputStream audioInputStream;
       private String _audioFilename;
       public void start(String audioFilename) 
       {
           thread = new Thread(this);
           thread.setName("Capture");
           _audioFilename = audioFilename;
           thread.start();
       }

       public void stop() {
           thread = null;
       }

       public void run() 
       {

           audioInputStream = null;
           InputStream inSong = MusicPlayback.class.getResourceAsStream(Consts.RESOURCES_MUSIC_FOLDER + _audioFilename);
           AudioInputStream inStreamMusic = null;
           
           try 
           {
        	   inStreamMusic = AudioSystem.getAudioInputStream(inSong);
           } 
           catch (UnsupportedAudioFileException e) 
           {
        	   // TODO Auto-generated catch block
        	   e.printStackTrace();
           } 
           catch (IOException e) 
           {
        	   // TODO Auto-generated catch block
        	   e.printStackTrace();
           }
           
           AudioFormat format = inStreamMusic.getFormat();
           DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
           
           
           if (!AudioSystem.isLineSupported(info)) 
           {
        	   System.err.println("Line matching " + info + " not supported.");
               return;
           }

           // get and open the target data line for capture
           try 
           {
               line = (SourceDataLine) AudioSystem.getLine(info);
               line.open(format, line.getBufferSize());
           } 
           catch (LineUnavailableException ex) 
           { 
        	   System.err.println("Unable to open the line: " + ex);
               return;
           } 
           catch (SecurityException ex) 
           { 
               System.err.println(ex.toString());
               return;
           } 
           catch (Exception ex) 
           { 
        	   System.err.println(ex.toString());
               return;
           }
           
           

           // play back the captured audio data
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int frameSizeInBytes = format.getFrameSize();
           int bufferLengthInFrames = line.getBufferSize() / 8;
           int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
           byte[] data = new byte[bufferLengthInBytes];
           int numBytesRead;
           
           line.start();
           while (thread != null) 
           {
               try 
               {
				
            	   if ((numBytesRead = inStreamMusic.read(data)) == -1) 
				   {
				       break;
				   }
            	   line.write(data, 0, numBytesRead);
               }
               catch (IOException e) 
               {
            	   // TODO Auto-generated catch block
            	   e.printStackTrace();
               }
           }

           // we reached the end of the stream.  stop and close the line.
           line.stop();
           line.close();
           line = null;

           // stop and close the output stream
           try 
           {
               out.flush();
               out.close();
           } 
           catch (IOException ex) 
           {
               ex.printStackTrace();
           }

           // load bytes into the audio input stream for playback

           byte audioBytes[] = out.toByteArray();
           ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
           audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

           try 
           {
               audioInputStream.reset();
           } 
           catch (Exception ex) 
           { 
               ex.printStackTrace(); 
               return;
           }
       }
}
