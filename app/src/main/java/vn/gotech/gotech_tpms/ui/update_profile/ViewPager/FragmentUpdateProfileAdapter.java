package vn.gotech.gotech_tpms.ui.update_profile.ViewPager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.List;

import vn.gotech.gotech_tpms.base.response.CheckDetails;

public class FragmentUpdateProfileAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {
    public FragmentUpdateProfileAdapter(@NonNull FragmentManager fm, List<CheckDetails> checkDetailsList) {
        super(fm);
        this.checkDetailsList = checkDetailsList;
    }

    List<CheckDetails> checkDetailsList;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("updateProfile", new Gson().toJson(checkDetailsList.get(position)));
        FragmentUpdateProfile fragmentUpdateProfile = new FragmentUpdateProfile();
        fragmentUpdateProfile.setArguments(bundle);
        return fragmentUpdateProfile;
    }

    @Override
    public int getCount() {
        return checkDetailsList.size();
    }
}
