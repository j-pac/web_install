package com.example.opensearch2;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            Toast.makeText(this, "Cenas235262e767", Toast.LENGTH_LONG).show();
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            manager.setOnCancelListener(new SearchManager.OnCancelListener() {
                @Override
                public void onCancel()
                    {
                        
                        WebSocketSingleton.getInstance().disconnect();
                        
                    }
            });

            manager.setOnDismissListener(new SearchManager.OnDismissListener() {
                @Override
                public void onDismiss()
                    {
                        WebSocketSingleton.getInstance().disconnect();
                    }
            });

            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                    {
                        onSearchRequested();
                        WebSocketSingleton.getInstance().connect();
                    }
            });
        }
}
