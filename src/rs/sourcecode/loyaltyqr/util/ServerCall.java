package rs.sourcecode.loyaltyqr.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerCall {
	
	private static final String urlLogin = "http://213.136.75.129/qrl/QRLoyalty/public/api/authenticate";
	private static final String urlGifts = "http://213.136.75.129/qrl/QRLoyalty/public/api/gifts";
	private static final String urlCodeValidate = "http://213.136.75.129/qrl/QRLoyalty/public/api/validate";
	private static final String urlUpdateCodes = "http://213.136.75.129/qrl/QRLoyalty/public/api/update-codes";
	
	public static String loginUser(String email, String password){
		JSONObject obj = new JSONObject();
		try {
			obj.put("email", email);
			obj.put("password", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		DefaultHttpClient client;
		InputStream is = null;
		StringBuilder result = new StringBuilder();
		try{
			client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urlLogin);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity(obj.toString());
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity != null){
				is = httpEntity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while((line=br.readLine()) != null){
					result.append(line);
				}
				br.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}				
			}
			client = null;
		}
		return result.toString();
	}
	
	public static String validateCode(String token, String secret){
		JSONObject obj = new JSONObject();
		try {
			obj.put("company_id", token);
			obj.put("secret", secret);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}		
		
		DefaultHttpClient client;
		InputStream is = null;
		StringBuilder result = new StringBuilder();
		try{
			client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urlCodeValidate);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity(obj.toString());
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity != null){
				is = httpEntity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while((line=br.readLine()) != null){
					result.append(line);
				}
				br.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}				
			}
			client = null;
		}
		return result.toString();
	}
	
	public static String updateCodes(String company_id, ArrayList<Integer> codes){
		JSONObject obj = new JSONObject();
		try {
			obj.put("company_id", company_id);
			for(int i=0; i<codes.size(); i++){
				obj.put("id"+i, codes.get(i));
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}		
		
		DefaultHttpClient client;
		InputStream is = null;
		StringBuilder result = new StringBuilder();
		try{
			client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urlUpdateCodes);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity(obj.toString());
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity != null){
				is = httpEntity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while((line=br.readLine()) != null){
					result.append(line);
				}
				br.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}				
			}
			client = null;
		}
		return result.toString();
	}
	
}
