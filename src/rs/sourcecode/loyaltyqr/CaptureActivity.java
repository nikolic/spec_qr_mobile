package rs.sourcecode.loyaltyqr;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import rs.sourcecode.loyaltyqr.camera.CameraManager;
import rs.sourcecode.loyaltyqr.camera.CaptureHandler;
import rs.sourcecode.loyaltyqr.camera.PreviewCallback;
import rs.sourcecode.loyaltyqr.camera.view.BoundingView;
import rs.sourcecode.loyaltyqr.camera.view.CameraPreviewView;

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
    protected void onPause() {
        super.onPause();
        cameraManager.release();
    }

    private class OnDecoded implements CaptureHandler.OnDecodedCallback {
        @Override
        public void onDecoded(String decodedData) {
            Toast.makeText(CaptureActivity.this, decodedData, Toast.LENGTH_SHORT);
        }
    }
}
