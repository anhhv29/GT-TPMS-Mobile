package vn.gotech.gotech_tpms.ui.update_profile.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.gotech_tpms.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.CategoryCarModelResponse;
import vn.gotech.gotech_tpms.base.response.CategoryCarNameResponse;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.response.UpdateProfile;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel.CategoryAdapterCarModel;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel.CategoryCarModel;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryAdapterCarName;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryCarName;

public class FragmentUpdateProfile extends androidx.fragment.app.Fragment {

    ApiService mService;
    Spinner spnCategoryCarModel;
    CategoryAdapterCarModel categoryAdapterCarModel;
    Spinner spnCategoryCarName;
    CategoryAdapterCarName categoryAdapterCarName;
    TextView tvMaASL;
    EditText edtTenKH;
    TextView tvSDT;
    EditText edtDoiXe;
    EditText edtBienSo;
    Button btnConfirm;
    LinearLayout lnWarning;
    int carModelId;
    int carNameId;
    CheckDetails checkDetails;
    List<CategoryCarModel> listCarModel;
    List<CategoryCarName> listCarName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        checkDetails = new Gson().fromJson(getArguments().getString("updateProfile"), CheckDetails.class);

        mService = RetrofitService.getAPIService();
        spnCategoryCarModel = view.findViewById(R.id.spinner_car_model);
        spnCategoryCarName = view.findViewById(R.id.spinner_car_name);
        tvMaASL = view.findViewById(R.id.tv_ma_asl);
        edtTenKH = view.findViewById(R.id.edt_ten_kh);
        tvSDT = view.findViewById(R.id.tv_SDT);
        edtDoiXe = view.findViewById(R.id.edt_doi_xe);
        edtBienSo = view.findViewById(R.id.edt_bien_so);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        lnWarning = view.findViewById(R.id.ln_warning_filled);

        tvMaASL.setText(checkDetails.getMabaohanh());
        edtTenKH.setText(checkDetails.getTenkhachhang());
        tvSDT.setText(checkDetails.getSodienthoai());
        edtBienSo.setText(checkDetails.getBiensoxe());
        edtDoiXe.setText(checkDetails.getDoixe());

        mService.getBrand().enqueue(new Callback<CategoryCarModelResponse>() {
            @Override
            public void onResponse(Call<CategoryCarModelResponse> call, Response<CategoryCarModelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    listCarModel = response.body().getData();
                    CategoryCarModel categoryCarModel = new CategoryCarModel();
                    categoryCarModel.setCarmodel("Hãng xe (*)");
                    categoryCarModel.setId(0);
                    listCarModel.add(0, categoryCarModel);
                    categoryAdapterCarModel = new CategoryAdapterCarModel(view.getContext(), 0, listCarModel);
                    spnCategoryCarModel.setAdapter(categoryAdapterCarModel);

                    spnCategoryCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int carId = ((CategoryCarModel) spnCategoryCarModel.getAdapter().getItem(position)).getId();
                            carModelId = carId;
                            carNameId = 0;
                            getCarName(carId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if (listCarModel != null) {
                    for (int i = 0; i < listCarModel.size(); i++) {
                        if (checkDetails.getHangxe() == listCarModel.get(i).getId()) {
                            spnCategoryCarModel.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryCarModelResponse> call, Throwable t) {

            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkhachhang = edtTenKH.getText().toString().trim();
                String biensoxe = edtBienSo.getText().toString().trim();
                String doixe = edtDoiXe.getText().toString().trim();

                if (edtTenKH.length() != 0) {
                    if (carModelId != 0) {
                        if (carNameId != 0) {
                            if (edtDoiXe.length() != 0) {
                                if (edtBienSo.length() > 6 && edtBienSo.length() < 12) {
                                    mService.updateActive("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", checkDetails.getMabaohanh(), tenkhachhang, carNameId, carModelId, doixe, biensoxe).enqueue(new Callback<UpdateProfile>() {
                                        @Override
                                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                                            Log.d("check data", call.request().url().toString());
                                            if (response.body() != null && response.body().getMessage() != null) {
                                                if (response.body().getMessage().toLowerCase().equals("Cập nhật dữ liệu thành công".toLowerCase())) {
                                                    Object object = response.body();
                                                    Log.d("check update profile", new Gson().toJson(object));
                                                    lnWarning.setVisibility(View.VISIBLE);
                                                    warning();
                                                } else {
                                                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(view.getContext(), "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UpdateProfile> call, Throwable t) {
                                            Toast.makeText(view.getContext(), "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                            Log.d("check data", call.request().url().toString());
                                        }
                                    });
                                } else {
                                    Toast.makeText(view.getContext(), "Vui lòng nhập lại biển số xe", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(view.getContext(), "Vui lòng nhập lại đời xe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Vui lòng chọn tên xe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Vui lòng chọn hãng xe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Vui lòng nhập lại tên", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void getCarName(int carId) {
        mService.getVehicles(carId).enqueue(new Callback<CategoryCarNameResponse>() {
            @Override
            public void onResponse(Call<CategoryCarNameResponse> call, Response<CategoryCarNameResponse> response) {
                listCarName = response.body().getData();
                CategoryCarName categoryCarName = new CategoryCarName();
                categoryCarName.setCarName("Tên xe (*)");
                categoryCarName.setId(0);
                listCarName.add(0, categoryCarName);
                categoryAdapterCarName = new CategoryAdapterCarName(spnCategoryCarModel.getContext(), 0, listCarName);
                spnCategoryCarName.setAdapter(categoryAdapterCarName);
                if (listCarName != null) {
                    for (int i = 0; i < listCarName.size(); i++) {
                        if (checkDetails.getDongxe() == listCarName.get(i).getId()) {
                            spnCategoryCarName.setSelection(i);
                            break;
                        }
                    }
                }
                spnCategoryCarName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int carId = ((CategoryCarName) spnCategoryCarName.getAdapter().getItem(position)).getId();
                        carNameId = carId;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<CategoryCarNameResponse> call, Throwable t) {

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
        }, 1000);
    }
}
