package vn.gotech.gotech_tpms.base.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import vn.gotech.gotech_tpms.base.response.ActiveProduct;
import vn.gotech.gotech_tpms.base.response.CategoryCarModelResponse;
import vn.gotech.gotech_tpms.base.response.CategoryCarNameResponse;
import vn.gotech.gotech_tpms.base.response.CheckActiveResponse;
import vn.gotech.gotech_tpms.base.response.CheckDetails;
import vn.gotech.gotech_tpms.base.response.ConfirmOTP;
import vn.gotech.gotech_tpms.base.response.GetPhoneResponse;
import vn.gotech.gotech_tpms.base.response.ResendOTP;
import vn.gotech.gotech_tpms.base.response.UpdateProfile;

public interface ApiService {
    @GET("api/get-carbrand")
    Call<CategoryCarModelResponse> getBrand();

    @GET("api/get-vehicles")
    Call<CategoryCarNameResponse> getVehicles(@Query("hangxe") int hangxe);

    @GET("api/check-active")
    Call<CheckActiveResponse> checkActive(@Query("token") String token, @Query("mabaohanh") String mabaohanh);

    @GET("api/get-details")
    Call<CheckDetails> checkDetails(@Query("token") String token, @Query("mabaohanh") String mabaohanh);

    @GET("api/get-phone")
    Call<GetPhoneResponse> getPhone(@Query("token") String token, @Query("sodienthoai") String sodienthoai);

    @POST("api/active-product")
    Call<ActiveProduct> activeProduct(@Query("token") String token, @Body RequestBody body);

    @PUT("api/update-active")
    Call<UpdateProfile> updateActive(@Query("token") String token, @Query("mabaohanh") String mabaohanh,
                                     @Query("tenkhachhang") String tenkhachhang,
                                     @Query("dongxe") int dongxe,
                                     @Query("hangxe") int hangxe,
                                     @Query("doixe") String doixe,
                                     @Query("biensoxe") String biensoxe);

    @GET("api/confirm-otp")
    Call<ConfirmOTP> confirmOTP(@Query("token") String token, @Query("mabaohanh") String mabaohanh, @Query("sodienthoai") String sodienthoai, @Query("otp") String otp);

    @GET("api/send-otp")
    Call<ResendOTP> resendOTP(@Query("token") String token, @Query("mabaohanh") String mabaohanh, @Query("sodienthoai") String sodienthoai);
}
