package com.example.opensearch2;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import androidwebsockets.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 30-09-2013
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketSingleton {


    private static Semaphore semaphore = new Semaphore(0);
    private WebSocketClient.Listener listener = new WebSocketClient.Listener() {
        @Override
        public void onConnect()
            {
                Log.d("TAG", "On Connect");
            }

        @Override
        public void onMessage(String message)
            {

                try {
                    JSONArray array = new JSONArray(message);

                    for (int i = 0; i < array.length(); i++) {
                        String suggestion = array.get(i).toString();
                        Log.d("TAG", "Suggestion " + suggestion);

                        addRow(mCursor, suggestion, i);
                    }
                    mContext.getContentResolver().notifyChange(mNotificationUri, null);
                    semaphore.release();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        @Override
        public void onMessage(byte[] data)
            {
                Log.d("TAG", data.toString());
                semaphore.release();

            }

        @Override
        public void onDisconnect(int code, String reason)
            {
                //To change body of implemented methods use File | Settings | File Templates.
                Log.d("TAG", reason);

            }

        @Override
        public void onError(Exception error)
            {
                Log.d("TAG", error.getMessage());
                semaphore.release();
            }
    };


    private Uri mNotificationUri;
    private static WebSocketClient web_socket_client;
    private MatrixCursor mCursor;
    private Context mContext;

    private WebSocketSingleton() {};

    public static void send(String query)
        {
            Log.d("TAG", "Query2 " + query);
            try {
                semaphore.acquire();
                if(web_socket_client.isConnected()){
                    web_socket_client.send("{\"query\":\"" + query + "\"}");
                }else{
                    semaphore.release();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    void disconnect(){
        web_socket_client.disconnect();
    }

    public void connect(){
        web_socket_client =  new WebSocketClient(java.net.URI.create("wss://192.168.1.71:9010"), listener, null);
        web_socket_client.connect();
        Log.d("TAG", "OnConnecting");
    }

    public WebSocketSingleton setNotificationUri(Uri uri){
        this.mNotificationUri = uri;
        return this;
    }

    public WebSocketSingleton setContext(Context context){
        this.mContext = context;
        return this;
    }

    public WebSocketSingleton setCursor(MatrixCursor cursor){
        this.mCursor = cursor;
        return this;
    }

    private void addRow(MatrixCursor matrix_cursor, String string, int i) {
        matrix_cursor.newRow().add(i).add(string).add(string);
    }

    private static class WebSocketHolder {
        public static final WebSocketSingleton INSTANCE = new WebSocketSingleton();
    }

    public static WebSocketSingleton getInstance() {
        return WebSocketHolder.INSTANCE;
    }


}
