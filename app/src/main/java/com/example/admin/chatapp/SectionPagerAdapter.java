package com.example.admin.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.chatapp.Fragments.ChatFragment;
import com.example.admin.chatapp.Fragments.RequestFragment;


public class SectionPagerAdapter extends FragmentPagerAdapter {

    myInterface mInterface;

    public SectionPagerAdapter(FragmentManager fm,myInterface mInterface) {
        super(fm);
        this.mInterface=mInterface;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setMyinterfaced(this.mInterface);
                return chatFragment;

            case 1:
                RequestFragment statusFragment = new RequestFragment();
                statusFragment.setMyinterfaced(this.mInterface);
                return statusFragment;


        }
        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }


    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chat";
            case 1:
                return "Requests";
            default:
                return null;
        }

    }
}