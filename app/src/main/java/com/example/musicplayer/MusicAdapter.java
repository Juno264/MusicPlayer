package com.example.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    List<Music> mMusics;
    Context context;

    public MusicAdapter(Context context, int layoutResourceId, List<Music> objects) {
        super(context, layoutResourceId, objects);

        this.context = context;
        mMusics = objects;
    }

    @Override
    public int getCount() {
        return mMusics.size();
    }

    @Override
    public Music getItem(int position) {
        return mMusics.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final  Music item=getItem(position);

        if(item != null){
            viewHolder.idTextView.setText(item.id);
            viewHolder.titleTextView.setText(item.title);
            viewHolder.uriTextView.setText(item.uri);
            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("URI", item.uri);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder {

        LinearLayout container;
        TextView idTextView;
        TextView titleTextView;
        TextView uriTextView;

        public ViewHolder(View view) {

            container = view.findViewById(R.id.container);
            idTextView = view.findViewById(R.id.id_textview);
            titleTextView = view.findViewById(R.id.title_textview);
            uriTextView = view.findViewById(R.id.uri_textview);
        }
    }


}
