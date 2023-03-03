package vn.gotech.gotech_tpms.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.gotech_tpms.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CheckActiveResponse;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.active_product.ActiveProductActivity;
import vn.gotech.gotech_tpms.ui.car_infor.CarInforActivity;
import vn.gotech.gotech_tpms.ui.scan_qr_code.ScanQrCodeActivity;
import vn.gotech.gotech_tpms.ui.update_profile.UpdateProfileActivity;

public class LoginActivity extends AppCompatActivity {

    ApiService mService;
    EditText edtLoginNhapMa;
    AppCompatButton btnLogin;
    SharedPreferences sharedPreferences;
    LinearLayout lnWarning;
    ImageView imageViewScanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mService = RetrofitService.getAPIService();
        edtLoginNhapMa = findViewById(R.id.edt_login_NhapMaAsl);
        btnLogin = findViewById(R.id.btn_login);
        lnWarning = findViewById(R.id.ln_warning_filled);
        imageViewScanQR = findViewById(R.id.imageView_icon_qr);
        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);

        imageViewScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ScanQrCodeActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mbh = edtLoginNhapMa.getText().toString().trim();
                if (!mbh.isEmpty()) {
                    mService.checkActive("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckActiveResponse>() {
                        @Override
                        public void onResponse(Call<CheckActiveResponse> call, Response<CheckActiveResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().getProduct()) {
                                    if (response.body().getData().getActive()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("mbh", mbh);
                                        editor.apply();
                                        Toast.makeText(LoginActivity.this, "Sản phẩm đã được kích hoạt", Toast.LENGTH_SHORT).show();
                                        //check details
                                        checkDetails(mbh);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sản phẩm chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, ActiveProductActivity.class);
                                        intent.putExtra("mbh", mbh);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Sản phẩm này không tồn tại", Toast.LENGTH_SHORT).show();
                                    lnWarning.setVisibility(View.VISIBLE);
                                    warning();
                                }
                            } else
                                Toast.makeText(LoginActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<CheckActiveResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                            Log.e("CheckActive", t.getMessage());
                        }
                    });
                } else
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập mã bảo hành", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void warning() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lnWarning.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private void checkDetails(String mbh) {
        //Check details
        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(Call<CheckDetails> call, Response<CheckDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    startActivity(new Intent(LoginActivity.this, CarInforActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lòng cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UpdateProfileActivity.class));
                }
            }

            @Override
            public void onFailure(Call<CheckDetails> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
