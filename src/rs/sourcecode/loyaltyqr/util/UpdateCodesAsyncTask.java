package rs.sourcecode.loyaltyqr.util;

import java.util.ArrayList;

import android.os.AsyncTask;

public class UpdateCodesAsyncTask extends AsyncTask<Void, Void, String>{

	UpdateCodesInterface updateCodesInterface;
	String company_id;
	ArrayList<Integer> codes = new ArrayList<Integer>();
	
	public void setUpdateCodesInterface(UpdateCodesInterface updateCodesInterface) {
		this.updateCodesInterface = updateCodesInterface;
	}
	
	public UpdateCodesAsyncTask(String company_id, ArrayList<Integer> codes){
		this.company_id = company_id;
		this.codes = codes;
	}

	@Override
	protected String doInBackground(Void... params) {		
		return ServerCall.updateCodes(company_id, codes);
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result == null || result.isEmpty()){
			updateCodesInterface.error();
		}else{
			updateCodesInterface.success(result);
		}
	}

}
