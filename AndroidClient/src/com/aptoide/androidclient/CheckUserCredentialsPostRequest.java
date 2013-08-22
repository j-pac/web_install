package com.aptoide.androidclient;

public class CheckUserCredentialsPostRequest extends HttpPostRequest {

	private static final String WEB_SERVICE_URL = "https://www.aptoide.com/webservices/checkUserCredentials";
	private static final String USER_KEY = "user";
	private static final String PASSHASH_KEY = "passhash";
	private static final String RESPONSE_FORMAT = "xml";
	
	public CheckUserCredentialsPostRequest(String user, String password) {
		super(WEB_SERVICE_URL);
		
//		String passhash = sha1(password);
		
		addPostParameter(USER_KEY, user);
//		addPostParameter(PASSHASH_KEY, passhash);
	}
	
}
