package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;


public class Fetcher extends AsyncTask<String, Void, String>{
	
	String address;
			
	@Override
	protected String doInBackground(String... params) {			
		address = params[0];
		String result = null;
		try {
			
			
//			HashMap<String, String> parameters = new HashMap<String, String>();
//			parameters.put("apiKey=", acc_key);
			result = getJSONString(address);
			Log.d("NETWORK CALL", result);
							
		} catch (IOException e) {
			e.printStackTrace();				
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	
	public static String PrepareURL(String protocolAndHost, String action, String parameter){
		
		String param = "";
		try {
			param = URLEncoder.encode(parameter, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return protocolAndHost + action + param;
	}
	
	public String getJSONString(String address) throws IOException {
		StringBuilder result = new StringBuilder();		
		HttpClient client = null;
		HttpGet get = null;
		HttpResponse response = null;
		InputStream is = null;
		try {
			client = new DefaultHttpClient();
			get = new HttpGet(address);
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			
			if(entity != null){
				is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while((line = br.readLine()) != null){
					result.append(line);
				}
				br.close();	
			}			
						
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally{
			if(is != null){
				is.close();
			}
			if(client != null){
				client = null;
			}
		}
		return result.toString();
	}
	

	
	
}