package vn.gotech.gotech_tpms.ui.otp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.gotech_tpms.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.ConfirmOTP;
import vn.gotech.gotech_tpms.base.response.ResendOTP;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.car_infor.CarInforActivity;

public class OtpActivity extends AppCompatActivity {

    ApiService mService;
    LinearLayout lnBack;
    TextView tvSdt;
    TextView tvMbh;
    PinView pinViewOtp;
    TextView tvResendOtp;
    TextView tvCountTime;
    Button btnConfirm;
    CountDownTimer countDownTimer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        lnBack = findViewById(R.id.ln_back);
        tvSdt = findViewById(R.id.tv_SDT);
        tvMbh = findViewById(R.id.tv_ma_asl);
        pinViewOtp = findViewById(R.id.otpView);
        tvResendOtp = findViewById(R.id.tv_resend_otp);
        tvCountTime = findViewById(R.id.tv_count_time);
        btnConfirm = findViewById(R.id.btn_confirm);
        mService = RetrofitService.getAPIService();
        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvResendOtp.setEnabled(false);
                tvCountTime.setText("(" + millisUntilFinished / 1000 + "s)");
            }

            @Override
            public void onFinish() {
                tvResendOtp.setEnabled(true);
                tvCountTime.setText("");
                countDownTimer.cancel();
            }
        };

        countDownTimer.start();

        lnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String mabaohanh = intent.getStringExtra("mabaohanh");
        String sodienthoai = intent.getStringExtra("sodienthoai");
        tvSdt.setText(sodienthoai);
        tvMbh.setText(mabaohanh);

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                countDownTimer.start();
                mService.resendOTP("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mabaohanh, sodienthoai).enqueue(new Callback<ResendOTP>() {
                    @Override
                    public void onResponse(Call<ResendOTP> call, Response<ResendOTP> response) {
                        if (response.body() != null && response.body().getMessage() != null) {
                            if (response.body().getMessage().toLowerCase().equals("Mã OTP đã được gửi".toLowerCase())) {
                                Object object = response.body();
                                Log.e("check gửi lại mã", new Gson().toJson(object));
                                Log.e("check data resend otp", call.request().url().toString());
                                Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OtpActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OtpActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResendOTP> call, Throwable t) {
                        Toast.makeText(OtpActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mService.confirmOTP("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mabaohanh, sodienthoai, pinViewOtp.getText().toString().trim()).enqueue(new Callback<ConfirmOTP>() {
                    @Override
                    public void onResponse(Call<ConfirmOTP> call, Response<ConfirmOTP> response) {
                        if (response.body() != null && response.body().getMessage() != null) {
                            if (response.body().getMessage().toLowerCase().equals("Kích hoạt sản phẩm thành công".toLowerCase())) {
                                Object object = response.body();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mbh", mabaohanh);
                                editor.apply();
                                Log.e("check kích hoạt", new Gson().toJson(object));
                                Log.e("check dữ liệu kích hoạt", call.request().url().toString());
                                startActivity(new Intent(OtpActivity.this, CarInforActivity.class));
                                finish();
                            } else {
                                Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OtpActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ConfirmOTP> call, Throwable t) {
                        Toast.makeText(OtpActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
