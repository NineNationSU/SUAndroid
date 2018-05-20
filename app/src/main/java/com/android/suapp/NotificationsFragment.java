package com.android.suapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.suapp.suapp.sdk.MessageUtility;
import com.android.suapp.suapp.server.database.objects.Message;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.database.objects.StudyGroup;
import com.android.suapp.suapp.server.utility.MessagesListWrapper;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_STUDENT_DATA;
import static com.android.suapp.SignUpActivity.APP_PROFFESION;

/**
 * Created by fokin on 10.04.2018.
 */

public class NotificationsFragment extends Fragment {

    private ImageButton newMessage;
    private SharedPreferences proffesion;
    private int select;
    private Student student;
    private String sourceData;
    public static int messageCount;
    public static List<Message> list;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    private void getMessages(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String listOfMessage = MessageUtility.getMessages(student,student.getToken());
                    MessagesListWrapper listWrapper = new Gson().fromJson(listOfMessage, MessagesListWrapper.class);
                    messageCount = listWrapper.getList().size();
                    list = listWrapper.getList();
                    System.out.println(messageCount);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public NotificationsFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificate, container, false);
        List<String> nameList = new ArrayList<>();
        proffesion = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);
        try {
            sourceData = proffesion.getString(APP_PREFERENCES_STUDENT_DATA, null);
            student =new Gson().fromJson(sourceData, Student.class);
        }
                catch (NullPointerException e){
            Toast.makeText(getContext(), "Не удалось загрузить данные о пользователе", Toast.LENGTH_SHORT).show();
        }
        getMessages();
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view_all_messages);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new RecyclerViewAdapter(NotificationsFragment.this.getContext(), CardModel.getObjectList(list)));
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);




        newMessage = view.findViewById(R.id.new_message);
        final SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler h = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMessages();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new RecyclerViewAdapter(NotificationsFragment.this.getContext(), CardModel.getObjectList(list)));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1500);
                    }
                }).start();
            }
        };
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select = proffesion.getInt(APP_PROFFESION, -1);
                switch (select){
                    case -1:
                        Toast.makeText(getContext(), "Не удалось загрузить данные о пользователе", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(getContext(), "Вашему типу пользователя недоступна отправка сообщений", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                    case 2:
                    case 3:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_send_message, null);
                        builder.setView(dialogView)
                                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final EditText messageBody = dialogView.findViewById(R.id.edittext_chat);
                                        final Handler h = new Handler();
                                        swipeRefreshLayout.setRefreshing(true);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    final String message = MessageUtility.sendMessage(
                                                            new Message().setSenderId(student.getId()).setBody(messageBody.getText().toString()),
                                                            new StudyGroup().setNumber(student.getGroup()),
                                                            student.getToken());
                                                    h.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                                            listener.onRefresh();
                                                        }
                                                    });
                                                } catch (URISyntaxException | IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;

                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(listener);

        return view;
    }
/*
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

        public RecycleViewHolder(LayoutInflater inflater, ViewGroup container, CardView mCardView,
                                 TextView textIcon, TextView textUserName, TextView textMessage){
            super(inflater.inflate(R.layout.activity_cardview, container, false));
            this.mCardView = mCardView;
            this.textIcon = textIcon;
            this.textUserName = textUserName;
            this.textMessage = textMessage;

        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>{

        private List<CardModel> objectList;
        private LayoutInflater inflater;

        public RecyclerViewAdapter(Context context, List<CardModel> objectList) {
            inflater = LayoutInflater.from(context);
            this.objectList = objectList;
        }

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
            return 5;//NotificationsFragment.messageCount;
        }
    }
*/

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

        private List<CardModel> objectList;
        private LayoutInflater inflater;

        public RecyclerViewAdapter(Context context, List<CardModel> objectList) {
            try {
                inflater = LayoutInflater.from(context);
                this.objectList = objectList;
            }catch(Exception ignored){}
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.activity_cardview, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CardModel current = objectList.get(position);
            holder.setData(current, position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private CardView mCardView;
            private TextView textIcon;
            private TextView textUserName;
            private TextView textMessage;
            private int position;
            private CardModel currentObject;

            public MyViewHolder(View itemView) {
                super(itemView);
                mCardView = itemView.findViewById(R.id.card_view);
                textIcon = itemView.findViewById(R.id.text_view_user_alphabet);
                textUserName = itemView.findViewById(R.id.text_view_username);
                textMessage = itemView.findViewById(R.id.text_message);
            }

            public void setData(CardModel currentObject, int position) {
                this.textIcon.setText(currentObject.getIcon());
                this.textMessage.setText(currentObject.getMessageBody());
                this.textUserName.setText(currentObject.getUserName());
                this.position = position;
                this.currentObject = currentObject;
            }
        }

        public void removeItem(int position) {
            objectList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, objectList.size());
//		notifyDataSetChanged();
        }

        public void addItem(int position, CardModel currentObject) {
            objectList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, objectList.size());
//		notifyDataSetChanged();
        }
    }


}
