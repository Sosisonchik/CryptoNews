package com.company.sosison.cryptonews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.company.sosison.cryptonews.fragment.BitCoin;
import com.company.sosison.cryptonews.fragment.Ephyr;


public class MyPagerAdapter extends FragmentPagerAdapter {
    int nOFrag=2;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BitCoin bitCoin = new BitCoin();
                return bitCoin;
            case 1:
                Ephyr ephyr = new Ephyr();
                return ephyr;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return nOFrag;
    }
}
