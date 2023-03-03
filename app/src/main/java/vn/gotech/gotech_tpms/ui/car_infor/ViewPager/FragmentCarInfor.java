package vn.gotech.gotech_tpms.ui.car_infor.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gotech_tpms.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CategoryCarNameResponse;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryCarName;

public class FragmentCarInfor extends Fragment {

    TextView tvTenxe;
    TextView tvBienso;
    TextView tvMaASL;
    TextView tvDoixe;
    ApiService mService;
    CheckDetails checkDetails;
    SharedPreferences sharedPreferences;
    String mbh;
    List<CategoryCarName> listCarName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        tvTenxe = view.findViewById(R.id.tv_ten_xe);
        tvBienso = view.findViewById(R.id.tv_bien_so);
        tvMaASL = view.findViewById(R.id.tv_ma_asl);
        tvDoixe = view.findViewById(R.id.tv_doi_xe);
        mService = RetrofitService.getAPIService();

        checkDetails = new Gson().fromJson(getArguments().getString("carInfor"), CheckDetails.class);

        tvMaASL.setText(checkDetails.getMabaohanh());
        tvBienso.setText(checkDetails.getBiensoxe());
        int hangxe = checkDetails.getHangxe();
        tvDoixe.setText(checkDetails.getDoixe());

        sharedPreferences = view.getContext().getSharedPreferences("mbh", Context.MODE_PRIVATE);
        mbh = sharedPreferences.getString("mbh", "");

        mService.getVehicles(hangxe).enqueue(new Callback<CategoryCarNameResponse>() {
            @Override
            public void onResponse(Call<CategoryCarNameResponse> call, Response<CategoryCarNameResponse> response) {
                listCarName = response.body().getData();
                Object o = response.body();
                Log.d("check data", new Gson().toJson(o));
                if (listCarName != null) {
                    for (int i = 0; i < listCarName.size(); i++) {
                        if (checkDetails.getDongxe() == listCarName.get(i).getId()) {
                            tvTenxe.setText(listCarName.get(i).getCarName());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryCarNameResponse> call, Throwable t) {

            }
        });

        return view;
    }
}
