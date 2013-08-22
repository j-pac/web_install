package com.aptoide.androidclient;

public class RegistryServicePostRequest extends HttpPostRequest {
	
	private static final String WEB_SERVICE_URL = "localhost/registry_service.php";
	private static final String TOKEN_KEY = "token";
	private static final String DEVICE_ID_KEY = "device_id";
	private static final String RESPONSE_FORMAT = "xml";

	public RegistryServicePostRequest(String token, String device_id) {
		super(WEB_SERVICE_URL);
		
		addPostParameter(TOKEN_KEY, token);
		addPostParameter(device_id, device_id);
		
	}
	

}
