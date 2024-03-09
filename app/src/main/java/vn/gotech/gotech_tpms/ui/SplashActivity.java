package vn.gotech.gotech_tpms.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotech_tpms.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.car_infor.CarInforActivity;
import vn.gotech.gotech_tpms.ui.login.LoginActivity;
import vn.gotech.gotech_tpms.ui.update_profile.UpdateProfileActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ApiService mService;
    String mbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);
        mbh = sharedPreferences.getString("mbh", "");

        mService = RetrofitService.getAPIService();

        //Check sản phẩm kích hoạt
/*
        if (!mbh.isEmpty()) {
            mService.checkActive("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckActiveResponse>() {
                @Override
                public void onResponse(@NonNull Call<CheckActiveResponse> call, @NonNull Response<CheckActiveResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().getProduct()) {
                            if (response.body().getData().getActive()) {
                                Toast.makeText(SplashActivity.this, "Sản phẩm đã được kích hoạt", Toast.LENGTH_SHORT).show();
                                checkDetails(mbh);
                            } else {
                                Toast.makeText(SplashActivity.this, "Sản phẩm chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                                gotoLogin();
                            }
                        } else {
                            Toast.makeText(SplashActivity.this, "Sản phẩm này không tồn tại", Toast.LENGTH_SHORT).show();
                            gotoLogin();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CheckActiveResponse> call, @NonNull Throwable t) {
                    Toast.makeText(SplashActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                    Log.e("CheckActive", Objects.requireNonNull(t.getMessage()));
                }
            });
        } else {
            gotoLogin();
        }
*/
        gotoLogin();
    }

    private void gotoLogin() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 1000);
    }

    private void checkDetails(String mbh) {
        //Check details
        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(@NonNull Call<CheckDetails> call, @NonNull Response<CheckDetails> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    startActivity(new Intent(SplashActivity.this, CarInforActivity.class));
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, "Vui lòng cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, UpdateProfileActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckDetails> call, @NonNull Throwable t) {
                Toast.makeText(SplashActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}