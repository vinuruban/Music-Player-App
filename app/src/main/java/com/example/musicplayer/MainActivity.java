package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer; //to have audio
    private AudioManager audioManager; //to manage audio
    int volumeAtChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mediaPlayer = MediaPlayer.create(this, R.raw.neee);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);



        //play music
        Button play = (Button) findViewById(R.id.playButton);
            play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });



        //pause music
        Button pause = (Button) findViewById(R.id.pauseButton);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });



        //control volume
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); //finds max vol
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //finds current vol

        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);

        volumeControl.setMax(maxVolume); //sets to max vol
        volumeControl.setProgress(currentVolume); //sets to current vol

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                TextView volumeProgressView = (TextView) findViewById(R.id.volumeProgressView);
                volumeProgressView.setText("Volume: " + progress + "%");
                volumeAtChange = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //progress bar
        final SeekBar progressBar = (SeekBar) findViewById(R.id.progressBar);

        progressBar.setMax(mediaPlayer.getDuration()); //set the max (progress bar at the end) to be equal to the song duration

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress); //changes the position of the song at change
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0); //set volume to zero at beginning of change
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeAtChange, 0); //set volume back to current volume at end of change
            }
        });

        //below is the logic to update the progress of the song every 1 second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000); //start at 0 and update every 1 second


//                    THE PLAYER DOESNT PLAY THE SONG SMOOTHLY. THE CODE BELOW DOES, BUT PROGRESS BAR DOESNT WORK
//        final int duration = mediaPlayer.getDuration();
//        final int amountToUpdate = duration / 100;
//        new Timer().schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (!(amountToUpdate * progressBar.getProgress() >= duration)) {
//                            int p = progressBar.getProgress();
//                            p += 1;
//                            progressBar.setProgress(p);
//                        }
//                    }
//                });
//            };
//        }, amountToUpdate);

    }
}
