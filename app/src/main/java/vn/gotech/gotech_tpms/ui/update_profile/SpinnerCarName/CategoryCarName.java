package vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName;

import com.google.gson.annotations.SerializedName;

public class CategoryCarName {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String carName;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
