/*******************************************************************************
 * Copyright (c) 2012 rmateus.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package cm.aptoide.pt.webservices.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import cm.aptoide.com.actionbarsherlock.app.SherlockActivity;
import cm.aptoide.pt.AptoideThemePicker;
import cm.aptoide.pt.R;
import cm.aptoide.pt.util.Algorithms;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CreateUser extends SherlockActivity /*SherlockActivity */{

	EditText username_box;
	EditText password_box;
	String username;
	String password;
	CheckBox checkShowPass;

	public static final int REQUEST_CODE = 20;
	public static final String WEB_SERVICE_CREATEUSER = "http://webservices.aptoide.com/webservices/createUser";

	Context context;
	private boolean suceed=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AptoideThemePicker.setAptoideTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_create_user);
//		getSupportActionBar().hide();
		context = this;
		username_box = (EditText) findViewById(R.id.email_box);
		password_box = (EditText) findViewById(R.id.password_box);
		checkShowPass = (CheckBox) findViewById(R.id.show_create_passwd);
		checkShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                	password_box.setTransformationMethod(null);
                } else {
                	password_box.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
		checkShowPass.setEnabled(true);
	}

	public void signUp(View v){
		username = username_box.getText().toString();
		password = password_box.getText().toString();
		if(username.trim().length()>0&&password.trim().length()>0){
			new CreateUserTask().execute(username.trim(),password.trim());
		}else{
			Toast toast= Toast.makeText(context,
					context.getString(R.string.check_your_credentials), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 30);
					toast.show();
		}

	}

	@Override
	public void finish() {
		if(suceed){
			Intent i = new Intent();
			i.putExtra("username", username_box.getText().toString());
			i.putExtra("password", password_box.getText().toString());
			setResult(RESULT_OK,i);
		}

		super.finish();
	}

	public enum EnumCreateUserResponse{
		OK,FAIL
	}

	public class CreateUserTask extends AsyncTask<String, Void, JSONObject>{
		ProgressDialog pd = new ProgressDialog(context);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.show();
			pd.setMessage(context.getString(R.string.please_wait));
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject status;
			String data = null;
			StringBuilder sb = null;
			try{
				HttpURLConnection connection = (HttpURLConnection) new URL(WEB_SERVICE_CREATEUSER).openConnection();
				String password_sha1 = Algorithms.computeSHA1sum(params[1]);
				String hmac = Algorithms.computeHmacSha1(params[0]+password_sha1, "bazaar_hmac");

				connection.setConnectTimeout(10000);
				connection.setReadTimeout(10000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
			    data += "&" + URLEncoder.encode("passhash", "UTF-8") + "=" + URLEncoder.encode(password_sha1,"UTF-8");
			    data += "&" + URLEncoder.encode("hmac", "UTF-8") + "=" + URLEncoder.encode(hmac,"UTF-8");
			    data += "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("json","UTF-8");

			    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			    wr.write(data);
			    wr.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                wr.close();
                br.close();
                status = new JSONObject(sb.toString());

			}catch(Exception e){
				return null;
			}


			return status;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);

			EnumCreateUserResponse result = EnumCreateUserResponse.FAIL;
			try{
				result = EnumCreateUserResponse.valueOf(json.getString("status").toUpperCase());
			}catch(Exception e){
				e.printStackTrace();
			}

			switch (result) {
			case OK:
				suceed=true;
				pd.setCancelable(false);
				pd.dismiss();
				finish();
				break;

			case FAIL:
				try{
					Toast toast= Toast.makeText(context,
							json.getString("errors"), Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 30);
							toast.show();
				}catch (Exception e) {
					Toast toast= Toast.makeText(context,
							context.getString(R.string.unkown_error), Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 30);
							toast.show();
				}
				if(pd!=null){
					pd.dismiss();
				}


				break;
			default:
				break;
			}

		}

	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == android.R.id.home) {
//			finish();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}

