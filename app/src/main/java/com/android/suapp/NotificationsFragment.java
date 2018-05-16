package com.android.suapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fokin on 10.04.2018.
 */

public class NotificationsFragment extends Fragment {

    private ImageButton newMessage;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    public NotificationsFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificate, container, false);
        List<String> nameList = new ArrayList<>();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_all_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter());

        newMessage = view.findViewById(R.id.new_message);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 2500);
            }
        });

        return view;
    }

    private class RecycleViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        private TextView textIcon;
        private TextView textUserName;
        private TextView textMessage;
        public RecycleViewHolder(View itemView){
            super(itemView);
        }

        public RecycleViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.activity_cardview, container, false));

            mCardView = itemView.findViewById(R.id.card_view);
            textIcon = itemView.findViewById(R.id.text_view_user_alphabet);
            textUserName = itemView.findViewById(R.id.text_view_username);
            textMessage = itemView.findViewById(R.id.text_message);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>{

        private List<String> messageList;

        @Override
        public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new RecycleViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(RecycleViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
