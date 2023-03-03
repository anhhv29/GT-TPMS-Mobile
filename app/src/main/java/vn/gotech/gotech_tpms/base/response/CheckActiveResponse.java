package vn.gotech.gotech_tpms.base.response;

import com.google.gson.annotations.SerializedName;

public class CheckActiveResponse {
    @SerializedName("data")
    CheckActive data;

    public CheckActive getData() {
        return data;
    }

    public void setData(CheckActive data) {
        this.data = data;
    }
}
