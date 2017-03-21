package com.tobiadeyinka.popularmovies.networking;

import android.net.Uri;
import android.view.View;
import android.os.AsyncTask;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;

import com.tobiadeyinka.popularmovies.R;

import org.json.*;
import java.io.IOException;

public class TrailersQueryTask extends AsyncTask<Integer, Void, String> {

    private Context context;
    private LinearLayout trailersSection;

    public TrailersQueryTask(LinearLayout trailersSection, Context context){
        this.trailersSection = trailersSection;
        this.context = context;
    }

    public void query(int movieId){
        this.execute(movieId);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        int id = integers[0];
        try {
            return MovieQueries.getMovieVideos(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject object = new JSONObject(s);
            JSONArray results = object.getJSONArray("results");
            int len = results.length();
            JSONObject tmp;

            for(int i = 0; i < len; i++){
                tmp = results.getJSONObject(i);
                appendTrailer(tmp);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void appendTrailer(JSONObject trailer){
        try {
            String name = trailer.getString("name");
            final String key = trailer.getString("key");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout l = (LinearLayout) inflater.inflate(R.layout.trailers_list_item, null);

            TextView tv = (TextView) l.findViewById(R.id.trailer_text);
            tv.setText(context.getString(R.string.trailer, name));

            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            trailersSection.addView(l);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
