package vn.gotech.gotech_tpms.base.response;

import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel.CategoryCarModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryCarModelResponse {
    @SerializedName("data")
    List<CategoryCarModel> data;

    public List<CategoryCarModel> getData() {
        return data;
    }

    public void setData(List<CategoryCarModel> data) {
        this.data = data;
    }
}
