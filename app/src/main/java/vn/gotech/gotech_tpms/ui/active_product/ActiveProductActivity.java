package vn.gotech.gotech_tpms.ui.active_product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gotech_tpms.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.gotech.gotech_tpms.base.response.ActiveProduct;
import vn.gotech.gotech_tpms.base.response.CategoryCarModelResponse;
import vn.gotech.gotech_tpms.base.response.CategoryCarNameResponse;
import vn.gotech.gotech_tpms.base.retrofit.ApiService;
import vn.gotech.gotech_tpms.base.retrofit.RetrofitService;
import vn.gotech.gotech_tpms.ui.otp.OtpActivity;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel.CategoryAdapterCarModel;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel.CategoryCarModel;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryAdapterCarName;
import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryCarName;

public class ActiveProductActivity extends AppCompatActivity {

    ApiService mService;
    Spinner spnCategoryCarModel;
    CategoryAdapterCarModel categoryAdapterCarModel;
    Spinner spnCategoryCarName;
    CategoryAdapterCarName categoryAdapterCarName;
    TextView tvMaASL;
    EditText edtTenKH;
    EditText edtSDT;
    EditText edtDoiXe;
    EditText edtBienSo;
    Button btnConfirm;
    LinearLayout lnBack;
    int carModelId;
    int carNameId;
    String mbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_product);

        Intent intent = getIntent();
        mbh = intent.getStringExtra("mbh");

        mService = RetrofitService.getAPIService();
        spnCategoryCarModel = findViewById(R.id.spinner_car_model);
        spnCategoryCarName = findViewById(R.id.spinner_car_name);
        tvMaASL = findViewById(R.id.tv_ma_asl);
        edtTenKH = findViewById(R.id.edt_ten_kh);
        edtSDT = findViewById(R.id.edt_sdt);
        edtBienSo = findViewById(R.id.edt_bien_so);
        edtDoiXe = findViewById(R.id.edt_doi_xe);
        btnConfirm = findViewById(R.id.btn_confirm);
        lnBack = findViewById(R.id.ln_back);

        tvMaASL.setText(mbh);

        lnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mService.getBrand().enqueue(new Callback<CategoryCarModelResponse>() {
            @Override
            public void onResponse(Call<CategoryCarModelResponse> call, Response<CategoryCarModelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<CategoryCarModel> listCarModel = response.body().getData();
                    CategoryCarModel categoryCarModel = new CategoryCarModel();
                    categoryCarModel.setCarmodel("Hãng xe (*)");
                    categoryCarModel.setId(0);
                    listCarModel.add(0, categoryCarModel);
                    categoryAdapterCarModel = new CategoryAdapterCarModel(ActiveProductActivity.this, 0, listCarModel);
                    spnCategoryCarModel.setAdapter(categoryAdapterCarModel);
                    spnCategoryCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int carId = ((CategoryCarModel) spnCategoryCarModel.getAdapter().getItem(position)).getId();
                            carModelId = carId;
                            carNameId = 0;
                            getCarName(carId);
//                            Toast.makeText(UpdateProfileActivity.this, carId+"", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CategoryCarModelResponse> call, Throwable t) {
                Toast.makeText(ActiveProductActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCarName(int id) {
        mService.getVehicles(id).enqueue(new Callback<CategoryCarNameResponse>() {
            @Override
            public void onResponse(Call<CategoryCarNameResponse> call, Response<CategoryCarNameResponse> response) {
                List<CategoryCarName> listCarName = response.body().getData();
                CategoryCarName categoryCarName = new CategoryCarName();
                categoryCarName.setCarName("Tên xe (*)");
                categoryCarName.setId(0);
                listCarName.add(0, categoryCarName);
                categoryAdapterCarName = new CategoryAdapterCarName(ActiveProductActivity.this, 0, listCarName);
                spnCategoryCarName.setAdapter(categoryAdapterCarName);
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
                Toast.makeText(ActiveProductActivity.this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkhachhang = edtTenKH.getText().toString().trim();
                String sodienthoai = edtSDT.getText().toString().trim();
                String biensoxe = edtBienSo.getText().toString().trim();
                String doixe = edtDoiXe.getText().toString().trim();

                if (edtTenKH.length() != 0) {
                    if (edtSDT.length() > 8 && edtSDT.length() < 15) {
                        if (carModelId != 0) {
                            if (carNameId != 0) {
                                if (edtDoiXe.length() != 0) {
                                    if (edtBienSo.length() > 6 && edtBienSo.length() < 12) {
                                        MultipartBody builder = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("mabaohanh", mbh)
                                                .addFormDataPart("tenkhachhang", tenkhachhang)
                                                .addFormDataPart("sodienthoai", sodienthoai)
                                                .addFormDataPart("dongxe", carNameId + "")
                                                .addFormDataPart("hangxe", carModelId + "")
                                                .addFormDataPart("doixe", doixe)
                                                .addFormDataPart("biensoxe", biensoxe)
                                                .addFormDataPart("imei", "")
                                                .build();
                                        mService.activeProduct("eWncAaLetuqI2VEn7Q5WKEgCMy09HmUngt", builder).enqueue(new Callback<ActiveProduct>() {
                                            @Override
                                            public void onResponse(Call<ActiveProduct> call, Response<ActiveProduct> response) {
                                                if (response.body() != null && response.body().getMessage() != null) {
                                                    if (response.body().getMessage().toLowerCase().equals("Gửi mã OTP thành công".toLowerCase())) {
                                                        Object object = response.body();
                                                        Log.e("check gửi mã OTP", new Gson().toJson(object));
                                                        Intent intent = new Intent(ActiveProductActivity.this, OtpActivity.class);
                                                        intent.putExtra("mabaohanh", mbh);
                                                        intent.putExtra("sodienthoai", sodienthoai);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(ActiveProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ActiveProductActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ActiveProduct> call, Throwable t) {
                                                Toast.makeText(ActiveProductActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ActiveProductActivity.this, "Vui lòng nhập lại biển số xe", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ActiveProductActivity.this, "Vui lòng nhập lại đời xe", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ActiveProductActivity.this, "Vui lòng chọn tên xe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ActiveProductActivity.this, "Vui lòng chọn hãng xe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActiveProductActivity.this, "Vui lòng nhập lại số điện thoại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActiveProductActivity.this, "Vui lòng nhập lại tên", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

