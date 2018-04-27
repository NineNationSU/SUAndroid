package com.android.suapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

/**
 * Created by fokin on 10.04.2018.
 */

public class NotificateFragment extends Fragment {

    public static NotificateFragment newInstance() {
        NotificateFragment fragment = new NotificateFragment();
        return fragment;
    }

    public NotificateFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificate, container, false);
    }
}
