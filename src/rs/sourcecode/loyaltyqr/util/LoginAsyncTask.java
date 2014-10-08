package rs.sourcecode.loyaltyqr.util;

import android.os.AsyncTask;

public class LoginAsyncTask extends AsyncTask<String, Void, String>{
	
	LoginInterface loginInterface;
		
	public void setLoginInterface(LoginInterface loginInterface) {
		this.loginInterface = loginInterface;
	}

	@Override
	protected String doInBackground(String... params) {
		String email = params[0];
		String password = params[1];
		return ServerCall.loginUser(email, password);
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result == null || result.isEmpty()){
			loginInterface.error();
		}else{
			loginInterface.success(result);
		}
	}

}
