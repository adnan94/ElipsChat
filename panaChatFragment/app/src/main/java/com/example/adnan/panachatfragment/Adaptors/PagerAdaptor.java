package com.example.adnan.panachatfragment.Adaptors;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adnan.panachatfragment.Fragments.CallsFragment;
import com.example.adnan.panachatfragment.Fragments.ConversationFragment;
import com.example.adnan.panachatfragment.Fragments.Friend_Request_Fragment;
import com.example.adnan.panachatfragment.Fragments.FriendsListFragment;
import com.example.adnan.panachatfragment.Fragments.GroupsFragment;

/**
 * Created by Adnan on 12/31/2015.
 */
//public class PagerAdaptor {
//}
public class PagerAdaptor extends FragmentStatePagerAdapter {
    String i[];

    public PagerAdaptor(android.support.v4.app.FragmentManager fm, String tite[]) {
        super(fm);
        this.i = tite;

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = null;
        if (position == 0) {
            fragment = new ConversationFragment();
        }
        if (position == 1) {
            fragment = new FriendsListFragment();
        }
        if (position == 2) {
            fragment = new GroupsFragment();
        }
        if (position == 3) {
            fragment = new CallsFragment();
        }
//        if(position == 3){
//            fragment = new Friend_Request_Fragment();
//
//        }

        return fragment;
    }

    @Override
    public int getCount() {

        return i.length;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Chats";
                break;
            case 1:
                title = "Contacts";
                break;
            case 2:
                title = "Groups";
                break;
            case 3:
                title = "Calls";
                break;

        }

        return title;
    }
}
