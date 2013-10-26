package rs.sourcecode.loyaltyqr;

import java.net.URI;

import rs.sourcecode.loyaltyqr.camera.CameraManager;
import rs.sourcecode.loyaltyqr.camera.CaptureHandler;
import rs.sourcecode.loyaltyqr.camera.PreviewCallback;
import rs.sourcecode.loyaltyqr.camera.view.BoundingView;
import rs.sourcecode.loyaltyqr.camera.view.CameraPreviewView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import data.Fetcher;

/**
 * Capture activity (camera barcode activity)
 */
public class CaptureActivity extends Activity {
    /**
     * Camera preview view
     */
    private CameraPreviewView cameraPreview;
    /**
     * Camera manager
     */
    private CameraManager cameraManager;
    /**
     * Capture handler
     */
    private Handler captureHandler;
    
    private static String current_text = null;
    
    private BoundingView bView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        
     // Create an instance of Camera
    	cameraManager = new CameraManager();
        captureHandler = new CaptureHandler(cameraManager, this, new OnDecoded());
        //requesting next frame for decoding
        cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));

        // Create our Preview view and set it as the content of our activity.
        cameraPreview = (CameraPreviewView) findViewById(R.id.camera_preview);
        cameraPreview.setCameraManager(cameraManager);
        
        ((BoundingView) findViewById(R.id.bounding_view)).setCameraManager(cameraManager);
                
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraManager.release();
    }
    
    private void createResultDialog(String qrcode){
    	
  	  AlertDialog alertDialog = new AlertDialog.Builder(CaptureActivity.this).create(); //Read Update
      alertDialog.setTitle("Success");
      alertDialog.setMessage(qrcode);
      CaptureActivity.current_text = qrcode;

      alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Continue..", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
        	 Log.v("Current text", CaptureActivity.current_text);
        	 
        	
        	// new Fetcher().execute(Fetcher.PrepareURL("http://www.api.source-code.rs", "silex/web/index.php/generate_qrcode/", CaptureActivity.current_text ));
        	 
        	//new Fetcher().execute("http://www.api.source-code.rs/silex/web/index.php/generate_qrcode/" + CaptureActivity.current_text);
        	
        	Intent intent = new Intent(CaptureActivity.this, PreviewActivity.class);
        	intent.putExtra("qrcode", CaptureActivity.current_text);
        	startActivity(intent);
        	
         }
      });
      
      alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Read again", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  // TO DO without recreating activity
//        	  reload();
        	  
        	  dialog.cancel();
        	  cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));
          }
       });

      alertDialog.show();
    	
    }
    
    public void reload() {
	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();

	    overridePendingTransition(0, 0);
	    startActivity(intent);
    }   

    private class OnDecoded implements CaptureHandler.OnDecodedCallback {
        @Override
        public void onDecoded(String decodedData) {
        	createResultDialog(decodedData.toString());   	
        }
    }   
    
}
