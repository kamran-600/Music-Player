package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlayListAdapter extends ArrayAdapter<PlaylistModal> {

    private Context context;
    private int mResource;
    private ArrayList<PlaylistModal> list;

    public PlayListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PlaylistModal> list) {
        super(context, resource, list);
        this.context = context;
        this.mResource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        TextView title = convertView.findViewById(R.id.title);
        ImageButton popupButton = convertView.findViewById(R.id.popUp_button);
        ImageView ppButton = convertView.findViewById(R.id.pp_button);
        title.setText(getItem(position).getTitle());


        if (MyMediaPlayer.currentIndex == position) {
            title.setTextColor(Color.BLUE);
            title.setSelected(true);
        }


        convertView.setOnClickListener(v -> {
            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.currentIndex = position;
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("list", list);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });


        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.inflate(R.menu.pop_menu);
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                        case R.id.rename:
                            Toast.makeText(context, "rename", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        });


        return convertView;
    }
}
