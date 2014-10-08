package rs.sourcecode.loyaltyqr.util;

import android.os.AsyncTask;

public class ValidateCodeAsyncTask extends AsyncTask<String, Void, String>{

	ValidateCodeInterface validateCodeInterface;
	
	public void setValidateCodeInterface(ValidateCodeInterface validateCodeInterface) {
		this.validateCodeInterface = validateCodeInterface;
	}

	@Override
	protected String doInBackground(String... params) {
		String company_id = params[0];
		String secret = params[1];
		return ServerCall.validateCode(company_id, secret);
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result == null || result.isEmpty()){
			validateCodeInterface.error();
		}else{
			validateCodeInterface.success(result);
		}
	}

}
