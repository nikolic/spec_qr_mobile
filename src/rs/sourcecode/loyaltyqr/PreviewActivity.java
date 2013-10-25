package rs.sourcecode.loyaltyqr;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import rs.sourcecode.loyaltyqr.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

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
		
		image = (ImageView) findViewById(R.id.imageView1);
		ok = (Button) findViewById(R.id.button1);
		cancel = (Button) findViewById(R.id.button2);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

		
		new LoadImage().execute("http://api.source-code.rs/silex/web/temp/526aa4bd1d8b4.png");

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			
			break;
		case R.id.button2:
			
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
