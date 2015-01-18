import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

class Server {

AudioInputStream audioInputStream;
static AudioInputStream ais;
static AudioFormat format;
static boolean status = true;
static int port = 1234;
static int sampleRate = 48000;/**8000, 16000, 22050, 24000, 32000, 44100, 48000 Choose the best for the device and the bandwidth **/

static DataLine.Info dli;
static SourceDataLine sdl;

public static void main(String args[]) throws Exception {

    DatagramSocket serverSocket = new DatagramSocket(port);

  

    byte[] rData = new byte[4096];

    format = new AudioFormat(sampleRate, 16, 1, true, false);
    dli = new DataLine.Info(SourceDataLine.class, format);
    sdl = (SourceDataLine) AudioSystem.getLine(dli);
    sdl.open(format);
    sdl.start();

    FloatControl volume = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
    volume.setValue(1.00f);

    DatagramPacket rPacket = new DatagramPacket(rData,
            rData.length);
     
    ByteArrayInputStream baiss = new ByteArrayInputStream(
            rPacket.getData());
    System.out.println(baiss);
    while (status == true) {
        serverSocket.receive(rPacket);
      System.out.println(rPacket);
        ais = new AudioInputStream(baiss, format, rPacket.getLength());
        Sstream(rPacket.getData());
       
    }
    sdl.drain();
    sdl.close();
}

public static void Sstream(byte soundbytes[]) {
    try {
        sdl.write(soundbytes, 0, soundbytes.length);
       
    } catch (Exception e) {
      
        e.printStackTrace();
    }
}
}
