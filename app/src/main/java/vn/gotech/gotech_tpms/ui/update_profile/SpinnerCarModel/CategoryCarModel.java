package vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel;

import com.google.gson.annotations.SerializedName;

public class CategoryCarModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String carmodel;

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
