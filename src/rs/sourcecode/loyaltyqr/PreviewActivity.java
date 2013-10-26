package rs.sourcecode.loyaltyqr;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rs.sourcecode.loyaltyqr.util.SystemUiHider;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class PreviewActivity extends Activity implements OnClickListener{
		
	private ImageView image;
	private Button ok,cancel;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_preview);
		
		// find all controls
		image = (ImageView) findViewById(R.id.imageView1);
		ok = (Button) findViewById(R.id.button1);
		cancel = (Button) findViewById(R.id.button2);
		// set listener
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		// get date from previous activity
		Intent intent = getIntent();
		String qrcode = intent.getStringExtra("qrcode");
		
		new LoadImage().execute("http://api.source-code.rs/silex/web/temp/526aa4bd1d8b4.png");
		
//		Toast.makeText(getApplicationContext(), qrcode, 
//				   Toast.LENGTH_LONG).show();
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ok button
		case R.id.button1:
			
			break;
		// cancel button
		case R.id.button2:
			Intent intent = new Intent(PreviewActivity.this, CaptureActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	public class LoadImage extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String address = params[0];
			try {
				URL url = new URL(address);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.connect();
				InputStream is = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			image.setImageBitmap(bitmap);
		}
		
	}
}
