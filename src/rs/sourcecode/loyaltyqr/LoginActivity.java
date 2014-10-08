package rs.sourcecode.loyaltyqr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.sourcecode.loyaltyqr.util.Gift;
import rs.sourcecode.loyaltyqr.util.GiftsSingleton;
import rs.sourcecode.loyaltyqr.util.LoginAsyncTask;
import rs.sourcecode.loyaltyqr.util.LoginInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	EditText email, password;
	Button login;
	
	LoginInterface loginInterface = new LoginInterface() {
		
		@Override
		public void success(String response) {
			checkResponse(response);
		}
		
		@Override
		public void error() {
			Toast.makeText(LoginActivity.this, "Login nije prosao.", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		login = (Button) findViewById(R.id.buttonLogin);
				
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
					Toast.makeText(LoginActivity.this, "Email i/ili password su prazni.", Toast.LENGTH_SHORT).show();
				}else{
					LoginAsyncTask asyncTask = new LoginAsyncTask();
					asyncTask.setLoginInterface(loginInterface);
					asyncTask.execute(email.getText().toString(), password.getText().toString());
				}
				
			}
		});
	}
	
	public void checkResponse(String response){
		try {
			JSONObject obj = new JSONObject(response);
			if(obj.getBoolean("success")){
				GiftsSingleton.getInstance().company_id = obj.getInt("company_id");
				ArrayList<Gift> gifts = new ArrayList<Gift>();
				JSONArray l = obj.getJSONArray("gifts");
				for(int i=0; i<l.length(); i++){
					JSONObject o = l.getJSONObject(i);
					Gift g = new Gift();
					g.id = o.getInt("id");
					g.name = o.getString("name");
					g.price = o.getInt("price");
					gifts.add(g);
				}
				GiftsSingleton.getInstance().listOfGifts = gifts;
				Intent i = new Intent(LoginActivity.this, ActivityGifts.class);
				startActivity(i);
				LoginActivity.this.finish();
			}else{
				Toast.makeText(LoginActivity.this, "Login nije prosao. Pokusajte ponovo.", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
