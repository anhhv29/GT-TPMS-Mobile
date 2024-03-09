package vn.gotech.gotech_tpms.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotech_tpms.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.response.GetPhoneResponse;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvName1;
    Button btnLogout;
    LinearLayout lnBack;
    ApiService mService;
    SharedPreferences sharedPreferences;
    RecyclerView rvWarrantyInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.textView_name);
        tvName1 = findViewById(R.id.textView_name1);

        btnLogout = findViewById(R.id.btn_logout);
        lnBack = findViewById(R.id.ln_back);
        rvWarrantyInfor = findViewById(R.id.rv_warranty_infor);

        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);
        String mbh = sharedPreferences.getString("mbh", "");
        mService = RetrofitService.getAPIService();
        rvWarrantyInfor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
/*

        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(@NonNull Call<CheckDetails> call, @NonNull Response<CheckDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String sodienthoai = response.body().getSodienthoai();
                    getPhone(sodienthoai);
                } else {
                    Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckDetails> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
*/

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mbh","");
            editor.apply();
            finish();
            finishAffinity();
        });

        lnBack.setOnClickListener(v -> finish());
    }

    private void getPhone(String sodienthoai) {
        mService.getPhone("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", sodienthoai).enqueue(new Callback<GetPhoneResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetPhoneResponse> call, @NonNull Response<GetPhoneResponse> response) {
                assert response.body() != null;
                ArrayList<CheckDetails> data = response.body().getData();
                if (!data.isEmpty()) {
                    tvName.setText(data.get(0).getTenkhachhang());
                    tvName1.setText(data.get(0).getTenkhachhang());
//                    CheckDetails checkDetails = new CheckDetails();
//                    checkDetails.setHanbaohanh("12345678");
//                    checkDetails.setImei("12345678");
//                    data.add(checkDetails);
//                    data.add(checkDetails);
//                    data.add(checkDetails);
//                    data.add(checkDetails);
//                    data.add(checkDetails);
//                    data.add(checkDetails);
                    WarrantyInforAdapter warrantyInforAdapter = new WarrantyInforAdapter(ProfileActivity.this, data);
                    rvWarrantyInfor.setAdapter(warrantyInforAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetPhoneResponse> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
