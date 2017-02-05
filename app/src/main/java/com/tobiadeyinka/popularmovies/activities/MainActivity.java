package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tobiadeyinka.popularmovies.R;

/**
 * @author Tobi Adeyinka
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.popularity:
                return sortByPopularity();
            case R.id.rating:
                return sortByRating();
            default:
                return false;
        }
    }

    /*
     * Sort movies by popularity
     */
    private boolean sortByPopularity(){
        Toast.makeText(getApplicationContext(), "sort by popularity", Toast.LENGTH_SHORT).show();
        return true;
    }

    /*
     * Sort movies by rating
     */
    private boolean sortByRating(){
        Toast.makeText(getApplicationContext(), "sort by rating", Toast.LENGTH_SHORT).show();
        return true;
    }

}
