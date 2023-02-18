package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.musicplayer.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<PlaylistModal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            showData();
        }

    }

    ActivityResultLauncher<String> permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (!result) {
                Toast.makeText(MainActivity.this, "You have to grant permission \nfrom the Settings", Toast.LENGTH_SHORT).show();
            } else {
                showData();
            }
        }
    });

    private void showData() {
        list = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, //Data means path
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            PlaylistModal modal = new PlaylistModal(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            if (new File(modal.getPath()).exists())
                list.add(modal);
        }

        if (list.size() == 0)
            binding.noSongText.setVisibility(View.VISIBLE);
        else {
            PlayListAdapter adapter = new PlayListAdapter(this, R.layout.singler_row, list);
            binding.playList.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.playList.setAdapter(new PlayListAdapter(this, R.layout.singler_row, list));
        if (MyMediaPlayer.getInstance().isPlaying()) {
            binding.rl2.setVisibility(View.VISIBLE);
            binding.titleCurrent.setText(list.get(MyMediaPlayer.getInstance().getCurrentPosition()).getTitle());
        }

        //  View footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null,false);
        //binding.playList.addFooterView(View.inflate(this,R.layout.footer_view,null));

        //  View footerView = View.inflate(this,R.layout.footer_view,null);
        //  binding.playList.addFooterView(footerView);
        // TextView title = footerView.findViewById(R.id.title_current);


    }
}