package vn.gotech.gotech_tpms.ui.update_profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.gotech_tpms.R;
import com.google.gson.Gson;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.response.GetPhoneResponse;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.update_profile.ViewPager.FragmentUpdateProfileAdapter;

public class UpdateProfileActivity extends AppCompatActivity {

    LinearLayout lnBack;
    ViewPager viewPager;
    FragmentManager fm;
    ApiService mService;
    SharedPreferences sharedPreferences;
    String mbh;
    WormDotsIndicator wormDotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);
        mbh = sharedPreferences.getString("mbh", "");

        mService = RetrofitService.getAPIService();
        viewPager = findViewById(R.id.viewpager);
        fm = getSupportFragmentManager();
        lnBack = findViewById(R.id.ln_back);
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        checkDetails();

        lnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkDetails() {
        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(Call<CheckDetails> call, Response<CheckDetails> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    String sodienthoai = response.body().getSodienthoai();
                    getPhone(sodienthoai);
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckDetails> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhone(String sodienthoai) {
        mService.getPhone("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", sodienthoai).enqueue(new Callback<GetPhoneResponse>() {
            @Override
            public void onResponse(Call<GetPhoneResponse> call, Response<GetPhoneResponse> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    Object object = response.body();
                    Log.e("check getPhone", new Gson().toJson(object));
                    ArrayList<CheckDetails> list = new ArrayList<>(response.body().getData());
//                    list.add(response.body().getData().get(0));
//                    list.add(response.body().getData().get(0));
                    viewPager.setAdapter(new FragmentUpdateProfileAdapter(fm, list));
                    wormDotsIndicator.attachTo(viewPager);
                    if (list.size() > 1) {
                        wormDotsIndicator.setVisibility(View.VISIBLE);
                    } else {
                        wormDotsIndicator.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetPhoneResponse> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
