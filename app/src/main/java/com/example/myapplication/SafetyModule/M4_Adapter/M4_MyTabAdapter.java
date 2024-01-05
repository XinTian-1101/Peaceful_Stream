package com.example.myapplication.SafetyModule.M4_Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.SafetyModule.M4_Fragment.M4_FileViewerFragment;
import com.example.myapplication.SafetyModule.M4_Fragment.M4_RecordFragment;

public class M4_MyTabAdapter extends FragmentPagerAdapter {


    String[]titles={"Record","Saved Recording"};

    public M4_MyTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new M4_RecordFragment();
            case 1:
                return new M4_FileViewerFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
