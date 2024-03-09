package vn.gotech.gotech_tpms.ui.car_infor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import vn.gotech.gotech_tpms.ui.car_infor.ViewPager.FragmentCarInforAdapter;
import vn.gotech.gotech_tpms.ui.profile.ProfileActivity;
import vn.gotech.gotech_tpms.ui.update_profile.UpdateProfileActivity;

public class CarInforActivity extends AppCompatActivity {
    ImageView imgUpdateProfile;
    ImageView imgProfile;
    ViewPager viewPagerCar;
    String mbh;
    ApiService mService;
    FragmentManager fm;
    WormDotsIndicator wormDotsIndicator;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_infor);

        sharedPreferences = getSharedPreferences("mbh", MODE_PRIVATE);
        mbh = sharedPreferences.getString("mbh", "");
        imgUpdateProfile = findViewById(R.id.imageView_update_profile);
        imgProfile = findViewById(R.id.img_profile);
        mService = RetrofitService.getAPIService();
        viewPagerCar = findViewById(R.id.viewpager);
        fm = getSupportFragmentManager();
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);

        imgProfile.setOnClickListener(v -> startActivity(new Intent(CarInforActivity.this, ProfileActivity.class)));

        imgUpdateProfile.setOnClickListener(v -> startActivity(new Intent(CarInforActivity.this, UpdateProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        mService.checkDetails("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", mbh).enqueue(new Callback<CheckDetails>() {
            @Override
            public void onResponse(@NonNull Call<CheckDetails> call, @NonNull Response<CheckDetails> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    String sodienthoai = response.body().getSodienthoai();
                    getPhone(sodienthoai);
                } else {
                    Toast.makeText(CarInforActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckDetails> call, @NonNull Throwable t) {
                Toast.makeText(CarInforActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private void getPhone(String sodienthoai) {
        mService.getPhone("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", sodienthoai).enqueue(new Callback<GetPhoneResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetPhoneResponse> call, @NonNull Response<GetPhoneResponse> response) {
                if ((response.isSuccessful() && response.body() != null)) {
                    Object object = response.body();
                    Log.e("check getPhone", new Gson().toJson(object));
                    ArrayList<CheckDetails> list = new ArrayList<>(response.body().getData());
//                    list.add(response.body().getData().get(0));
                    viewPagerCar.setOffscreenPageLimit(list.size());
                    viewPagerCar.setAdapter(new FragmentCarInforAdapter(fm, list));
                    wormDotsIndicator.attachTo(viewPagerCar);
                    if (list.size() > 1) {
                        wormDotsIndicator.setVisibility(View.VISIBLE);
                    } else {
                        wormDotsIndicator.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(CarInforActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetPhoneResponse> call, @NonNull Throwable t) {
                Toast.makeText(CarInforActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}