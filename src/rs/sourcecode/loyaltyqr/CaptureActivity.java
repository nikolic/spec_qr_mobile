package rs.sourcecode.loyaltyqr;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.sourcecode.loyaltyqr.camera.CameraManager;
import rs.sourcecode.loyaltyqr.camera.CaptureHandler;
import rs.sourcecode.loyaltyqr.camera.PreviewCallback;
import rs.sourcecode.loyaltyqr.camera.view.BoundingView;
import rs.sourcecode.loyaltyqr.camera.view.CameraPreviewView;
import rs.sourcecode.loyaltyqr.util.Gift;
import rs.sourcecode.loyaltyqr.util.GiftsSingleton;
import rs.sourcecode.loyaltyqr.util.UpdateCodesAsyncTask;
import rs.sourcecode.loyaltyqr.util.UpdateCodesInterface;
import rs.sourcecode.loyaltyqr.util.ValidateCodeAsyncTask;
import rs.sourcecode.loyaltyqr.util.ValidateCodeInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
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
    
    private int company_id;
    private Gift gift;
    private Set<Integer> scaned = new HashSet<Integer>();
    private Set<Integer> alreadyScaned = new HashSet<Integer>();
    
    ValidateCodeInterface validateCodeInterface = new ValidateCodeInterface() {
		
		@Override
		public void success(String result) {
			try {
				JSONObject obj = new JSONObject(result);
				if(obj.getBoolean("success") && obj.getBoolean("exist")){
					JSONObject l = obj.getJSONObject("loyaltyCode");
					if(scaned.add(l.getInt("id"))){
						Toast.makeText(CaptureActivity.this, "Kod je validiran.", Toast.LENGTH_SHORT).show();
					}
					boolean check = checkIfPriceCool();
					if(check){
						createDialog();
					}else{
						cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));
					}
				}else{
					JSONObject l = obj.getJSONObject("loyaltyCode");
					if(alreadyScaned.add(l.getInt("id"))){
						Toast.makeText(CaptureActivity.this, "Kod nije validan ili je vec iskoriscen. Pokusajte sa drugim.", Toast.LENGTH_SHORT).show();
					}
					cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void error() {
			Toast.makeText(CaptureActivity.this, "Kod nije validan ili je vec iskoriscen. Pokusajte sa drugim.", Toast.LENGTH_SHORT).show();
		}
	};
	
	UpdateCodesInterface updateCodesInterface = new UpdateCodesInterface() {
		
		@Override
		public void success(String result) {
			try {
				JSONObject obj = new JSONObject(result);
				if(obj.getBoolean("success")){
					createUpdateSuccessDialog();
				}else{
					createUpdateErrorDialog();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void error() {
			createUpdateErrorDialog();			
		}
	};

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
        
        gift = GiftsSingleton.getInstance().gift;
        company_id = GiftsSingleton.getInstance().company_id;
        
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
    
    public boolean checkIfPriceCool(){
    	return this.gift.price == this.scaned.size() ? true : false;
    }
    
    
    private void createDialog(){
    	AlertDialog alertDialog = new AlertDialog.Builder(CaptureActivity.this).create(); //Read Update
    	alertDialog.setMessage("Da li zelite da ponistite skenirane loyalty kodove?");

    	alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "DA", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			UpdateCodesAsyncTask asyncTask = new UpdateCodesAsyncTask(Integer.toString(company_id), new ArrayList<Integer>(scaned));
				asyncTask.setUpdateCodesInterface(updateCodesInterface);
				asyncTask.execute();

    		}
    	});

    	alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "NE", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.cancel();
    			CaptureActivity.this.finish();
    		}
    	});

    	alertDialog.show();
    }
    
    private void createUpdateErrorDialog(){
    	AlertDialog alertDialog = new AlertDialog.Builder(CaptureActivity.this).create(); //Read Update
    	alertDialog.setMessage("Validiranje kodova nije uspelo. Pokusajte kasnije.");

    	alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.cancel();
    			CaptureActivity.this.finish();
    		}
    	});
    	alertDialog.show();
    }
    
    private void createUpdateSuccessDialog(){
    	AlertDialog alertDialog = new AlertDialog.Builder(CaptureActivity.this).create(); //Read Update
    	alertDialog.setMessage("Uspesno ste iskoristili skenirane kodove. Mozete preuzetu poklon.");

    	alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.cancel();
    			CaptureActivity.this.finish();
    		}
    	});
    	alertDialog.show();
    }
    
    private void validateCode(String qrcode){
    	ValidateCodeAsyncTask asyncTask = new ValidateCodeAsyncTask();
    	asyncTask.setValidateCodeInterface(validateCodeInterface);
    	asyncTask.execute(Integer.toString(company_id), qrcode);
    	
    	
    	
//  	  AlertDialog alertDialog = new AlertDialog.Builder(CaptureActivity.this).create(); //Read Update
//      alertDialog.setTitle("Success");
//      alertDialog.setMessage(qrcode);
//      CaptureActivity.current_text = qrcode;
//
//      alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Continue..", new DialogInterface.OnClickListener() {
//         public void onClick(DialogInterface dialog, int which) {
//        	 Log.v("Current text", CaptureActivity.current_text);
//        	 
//        	
//        	// new Fetcher().execute(Fetcher.PrepareURL("http://www.api.source-code.rs", "silex/web/index.php/generate_qrcode/", CaptureActivity.current_text ));
//        	 
//        	//new Fetcher().execute("http://www.api.source-code.rs/silex/web/index.php/generate_qrcode/" + CaptureActivity.current_text);
//        	
//        	Intent intent = new Intent(CaptureActivity.this, PreviewActivity.class);
//        	intent.putExtra("qrcode", CaptureActivity.current_text);
//        	startActivity(intent);
//        	
//         }
//      });
//      
//      alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Read again", new DialogInterface.OnClickListener() {
//          public void onClick(DialogInterface dialog, int which) {
//        	  // TO DO without recreating activity
////        	  reload();
//        	  
//        	  dialog.cancel();
//        	  cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));
//          }
//       });
//
//      alertDialog.show();
    	
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
        	validateCode(decodedData.toString());   	
        }
    }   
    
}
