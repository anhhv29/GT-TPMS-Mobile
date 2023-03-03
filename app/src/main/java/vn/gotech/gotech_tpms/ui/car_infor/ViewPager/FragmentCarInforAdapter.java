package vn.gotech.gotech_tpms.ui.car_infor.ViewPager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.List;

import vn.gotech.gotech_tpms.base.response.CheckDetails;

public class FragmentCarInforAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {
    public FragmentCarInforAdapter(@NonNull FragmentManager fm, List<CheckDetails> checkDetailsList) {
        super(fm);
        this.checkDetailsList = checkDetailsList;
    }

    List<CheckDetails> checkDetailsList;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("carInfor", new Gson().toJson(checkDetailsList.get(position)));
        FragmentCarInfor fragmentCarInfor = new FragmentCarInfor();
        fragmentCarInfor.setArguments(bundle);
        return fragmentCarInfor;
    }

    @Override
    public int getCount() {
        return checkDetailsList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}