package vn.gotech.gotech_tpms.ui.confirm_qr_code;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotech_tpms.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CheckActiveResponse;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.active_product.ActiveProductActivity;
import vn.gotech.gotech_tpms.ui.car_infor.CarInforActivity;
import vn.gotech.gotech_tpms.ui.update_profile.UpdateProfileActivity;

public class ConfirmQrCodeActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvRefuse;
    ApiService mService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_qr_code);

        btnLogin = findViewById(R.id.btn_logout);
        tvRefuse = findViewById(R.id.tv_refuse);
        mService = RetrofitService.getAPIService();
        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            final Intent intent = getIntent();
            final String mbh = intent.getStringExtra("mbh");

            @Override
            public void onClick(View v) {
                if (mbh != null && !mbh.isEmpty()) {
                    mService.checkActive("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckActiveResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CheckActiveResponse> call, @NonNull Response<CheckActiveResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().getProduct()) {
                                    if (response.body().getData().getActive()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("mbh", mbh);
                                        editor.apply();
                                        Toast.makeText(ConfirmQrCodeActivity.this, "Sản phẩm đã được kích hoạt", Toast.LENGTH_SHORT).show();
                                        checkDetails(mbh);
                                    } else {
                                        Toast.makeText(ConfirmQrCodeActivity.this, "Sản phẩm chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmQrCodeActivity.this, ActiveProductActivity.class);
                                        intent.putExtra("mbh", mbh);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(ConfirmQrCodeActivity.this, "Sản phẩm này không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ConfirmQrCodeActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CheckActiveResponse> call, @NonNull Throwable t) {
                            Toast.makeText(ConfirmQrCodeActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                            Log.e("CheckActive", Objects.requireNonNull(t.getMessage()));
                        }
                    });
                }
            }
        });

        tvRefuse.setOnClickListener(v -> finish());
    }

    private void checkDetails(String mbh) {
        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(@NonNull Call<CheckDetails> call, @NonNull Response<CheckDetails> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    startActivity(new Intent(ConfirmQrCodeActivity.this, CarInforActivity.class));
                    finish();
                    finishAffinity();
                } else {
                    Toast.makeText(ConfirmQrCodeActivity.this, "Vui lòng cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConfirmQrCodeActivity.this, UpdateProfileActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckDetails> call, @NonNull Throwable t) {
                Toast.makeText(ConfirmQrCodeActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
