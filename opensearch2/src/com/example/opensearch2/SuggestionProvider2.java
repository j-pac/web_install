package com.example.opensearch2;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 26-09-2013
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class SuggestionProvider2 extends ContentProvider {

    private static final String URI =  "";

    @Override
    public boolean onCreate() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType(Uri uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cursor query(final Uri uri, String[] projection, String selection, final String[] selectionArgs, String sortOrder) {



//        String query = uri.getLastPathSegment().toLowerCase();
//
//        Toast.makeText(getContext().getApplicationContext(), "QUERY: " + query, Toast.LENGTH_SHORT).show();
//
        String[] matrix_columns = {"_id", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA};
//
        MatrixCursor matrix_cursor = new MatrixCursor(matrix_columns);
        WebSocketSingleton.getInstance().setCursor(matrix_cursor).setNotificationUri(uri).setContext(getContext());
        matrix_cursor.setNotificationUri(getContext().getContentResolver(), uri);
        WebSocketSingleton.send(selectionArgs[0]);

        return matrix_cursor;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void addRow(MatrixCursor matrix_cursor, String string) {
        int id = matrix_cursor.getCount();
        matrix_cursor.newRow().add(id).add(string).add(string);
    }


}