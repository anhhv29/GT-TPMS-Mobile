package vn.gotech.gotech_tpms.base.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPhoneResponse {
    @SerializedName("data")
    ArrayList<CheckDetails> data;

    public ArrayList<CheckDetails> getData() {
        return data;
    }

    public void setData(ArrayList<CheckDetails> data) {
        this.data = data;
    }
}
