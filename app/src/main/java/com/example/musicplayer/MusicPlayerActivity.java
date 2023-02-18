package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.databinding.ActivityMusicPlayerBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityMusicPlayerBinding binding;
    ArrayList<PlaylistModal> list;
    PlaylistModal currentS;
    static MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = (ArrayList<PlaylistModal>) getIntent().getSerializableExtra("list");
        setMusicResource();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    binding.startTime.setText(convertToDDMS(mediaPlayer.getCurrentPosition() + ""));
                    if (mediaPlayer.isPlaying()) {
                        binding.pausePlayBtn.setImageResource(R.drawable.pause);
                        binding.icon.setRotation(x++);
                    } else {
                        binding.pausePlayBtn.setImageResource(R.drawable.play);
                        binding.icon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this, 100);

            }
        });

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.next5secBtn.setOnClickListener(v -> {
            mediaPlayer.seekTo(binding.seekbar.getProgress() + 5000);
        });
        binding.prev5secBtn.setOnClickListener(v -> {
            mediaPlayer.seekTo(binding.seekbar.getProgress() - 5000);
        });
    }

    private void setMusicResource() {
        currentS = list.get(MyMediaPlayer.currentIndex);
        binding.title.setText(currentS.getTitle());
        binding.title.setSelected(true);
        binding.totalTime.setText(convertToDDMS(currentS.getDuration()));

        binding.pausePlayBtn.setOnClickListener(v -> pausePlay());
        binding.prevBtn.setOnClickListener(v -> goToPrev());
        binding.nextBtn.setOnClickListener(v -> goToNext());

        playMusic();

    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentS.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            binding.seekbar.setProgress(0);
            binding.seekbar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToNext() {
        MyMediaPlayer.currentIndex += 1;
        if (MyMediaPlayer.currentIndex == list.size() - 1) {
            MyMediaPlayer.currentIndex = 0;
        }
        mediaPlayer.reset();
        setMusicResource();
    }

    private void goToPrev() {
        MyMediaPlayer.currentIndex -= 1;
        if (MyMediaPlayer.currentIndex == -1) {
            MyMediaPlayer.currentIndex = list.size() - 1;
        }
        mediaPlayer.reset();
        setMusicResource();
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else mediaPlayer.start();
    }

    private static String convertToDDMS(String duration) {
        Long milis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milis) % TimeUnit.MINUTES.toSeconds(1));
    }


}