package com.tobiadeyinka.popularmovies.networking;

import android.os.AsyncTask;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;

import com.tobiadeyinka.popularmovies.R;

import org.json.*;
import java.io.IOException;

public class ReviewsQueryTask extends AsyncTask<Integer, Void, String> {

    private Context context;
    private LinearLayout reviewsSection;

    public ReviewsQueryTask(LinearLayout reviewsSection, Context context){
        this.reviewsSection = reviewsSection;
        this.context = context;
    }

    public void query(int movieId){
        this.execute(movieId);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        int id = integers[0];
        try {
            return MovieQueries.getMovieReviews(id);
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
                appendReview(tmp);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void appendReview(JSONObject review){
        try {
            String author = review.getString("author");
            String content = review.getString("content");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout l = (LinearLayout) inflater.inflate(R.layout.reviews_list_item, null);

            TextView tv1 = (TextView) l.findViewById(R.id.reviewer_name);
            tv1.setText(context.getString(R.string.reviewer, author));

            TextView tv2 = (TextView) l.findViewById(R.id.review);
            tv2.setText(context.getString(R.string.review, content));

            reviewsSection.addView(l);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
