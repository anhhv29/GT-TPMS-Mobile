package vn.gotech.gotech_tpms.base.response;

import vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName.CategoryCarName;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryCarNameResponse {
    @SerializedName("data")
    List<CategoryCarName> data;

    public List<CategoryCarName> getData() {
        return data;
    }

    public void setData(List<CategoryCarName> data) {
        this.data = data;
    }
}
