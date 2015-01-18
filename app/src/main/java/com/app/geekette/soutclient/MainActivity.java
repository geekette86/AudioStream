package com.app.geekette.soutclient;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends Activity {
    private Button start,stop;
    String hostname ="hostip";
    public byte[] buffer;
    public static DatagramSocket socket;
    private int port=1234;
    AudioRecord recorder;

    private int sampleRate = 48000;/**8000, 16000, 22050, 24000, 32000, 44100, 48000 Choose the best for the device and the bandwidth **/
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private boolean status = true;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById (R.id.button1);
        stop = (Button) findViewById (R.id.button2);
    }
    public void stop(View view){
        status = false;
        recorder.release();
        Toast.makeText(getApplicationContext(),"Stop record",Toast.LENGTH_SHORT).show();
    }
    public void start(View view){
        status = true;
        Toast.makeText(getApplicationContext(),"Start record",Toast.LENGTH_SHORT).show();
        startStreaming();
    }
    public void startStreaming() {


        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    DatagramSocket socket = new DatagramSocket();

                    byte[] buffer = new byte[minBufSize];


                    DatagramPacket packet;

                    final InetAddress destination = InetAddress.getByName(hostname);



                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);

try {
    recorder.startRecording();

}catch(IllegalStateException a){}
                    while(status == true) {


                        //reading data from MIC into buffer
                        minBufSize = recorder.read(buffer, 0, buffer.length);

                        //putting buffer in the packet
                        packet = new DatagramPacket (buffer,buffer.length,destination,port);

                        socket.send(packet);



                    }



                } catch(UnknownHostException e) {
                    Log.e("Ex", "UnknownHostException");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("EX", "IOException");
                }
            }

        });
        t.start();
    }
}
