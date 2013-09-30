package com.example.opensearch2;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 25-09-2013
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */

public class SearchableActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);


        handleSearch(getIntent());

    }

    private void handleSearch(Intent intent) {

        String query = null;

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            query = uri.toString();
        }


        if(query != null) {
            Toast.makeText(this, "RECEIVED: "+ query + "!!! :D", Toast.LENGTH_SHORT).show();
        }



    }
}