package vn.gotech.gotech_tpms.ui.scan_qr_code;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotech_tpms.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import vn.gotech.gotech_tpms.ui.confirm_qr_code.ConfirmQrCodeActivity;

public class ScanQrCodeActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;
    LinearLayout lnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        barcodeScannerView.decodeContinuous(callback);
        barcodeScannerView.setStatusText("");

        lnBack = findViewById(R.id.ln_back_scan_qr);

        lnBack.setOnClickListener(v -> finish());
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(final BarcodeResult result) {
            Toast.makeText(ScanQrCodeActivity.this, result.getResult().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ScanQrCodeActivity.this, ConfirmQrCodeActivity.class);
            intent.putExtra("mbh", result.getResult().toString());
            startActivity(intent);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeScannerView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
