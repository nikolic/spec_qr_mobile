package rs.sourcecode.loyaltyqr;

import rs.sourcecode.loyaltyqr.util.Gift;
import rs.sourcecode.loyaltyqr.util.GiftsAdapter;
import rs.sourcecode.loyaltyqr.util.GiftsSingleton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityGifts extends Activity{
	
	ListView list;
	SharedPreferences preferences;
	GiftsAdapter adapter;
	Button logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gifts);		
		logout = (Button) findViewById(R.id.buttonLogout);
		list = (ListView) findViewById(R.id.listView);
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Gift gift = (Gift) parent.getItemAtPosition(position);
				GiftsSingleton.getInstance().gift = gift;
				Intent i = new Intent(ActivityGifts.this, CaptureActivity.class);
				startActivity(i);
			}
		});
		
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GiftsSingleton.getInstance().company_id = 0;
				GiftsSingleton.getInstance().gift = null;
				GiftsSingleton.getInstance().listOfGifts.clear();
				Intent i = new Intent(ActivityGifts.this, LoginActivity.class);
				startActivity(i);
				ActivityGifts.this.finish();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter = new GiftsAdapter(this, GiftsSingleton.getInstance().listOfGifts);
		list.setAdapter(adapter);
	}

}
