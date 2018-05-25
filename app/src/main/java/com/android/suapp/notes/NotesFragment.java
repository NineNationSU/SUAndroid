package com.android.suapp.notes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.suapp.R;
import com.android.suapp.suapp.sdk.SUAppServer;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.utility.NoteListWrapper;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_STUDENT_DATA;


/**
 * Created by fokin on 26.04.2018.
 */

public class NotesFragment extends Fragment{

    public static List<String> list;
    public final static String NOTES_PREFRENCES = "notes_pref";
    public final static String LIST_DISCIPLINE_PREFRENCES = "list_subjects";
    private String buffer;
    private Student student;
    private SharedPreferences sp;
    final Handler h = new Handler();
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("StaticFieldLeak")
    private static NotesFragment instance;
    public static NotesFragment newInstance() {
        if (instance == null){
            instance = new NotesFragment();
        }
        return instance;
    }


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDisciplines() {

        try{
            sp = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            String studentJSONString = sp.getString(APP_PREFERENCES_STUDENT_DATA, "null");
            if(studentJSONString.equals("null")){
                throw new Exception();
            }
            student =new Gson().fromJson(studentJSONString, Student.class);
            buffer = sp.getString(LIST_DISCIPLINE_PREFRENCES, null);
            if (buffer == null){
                buffer = SUAppServer.getLessons(student.getToken());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(LIST_DISCIPLINE_PREFRENCES, buffer);
                editor.apply();
            }
            list = Arrays.asList(buffer.split(";"));
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            });

        }
        catch(Exception e){
            h.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Не удалось загрузить данные предметов", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_1);
        swipeRefreshLayout.setRefreshing(false);

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view_all_subjects);
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                getDisciplines();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(new RecyclerViewAdapter(NotesFragment.this.getContext(), CardModelDisciplines.getObjectList(list)));
                            swipeRefreshLayout.setRefreshing(false);
                        }catch (NullPointerException e){}
                    }
                }, 100);
            }
        }).start();



        return view;
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<NotesFragment.RecyclerViewAdapter.MyViewHolder>{

        private List<CardModelDisciplines> objectList;
        private LayoutInflater inflater;


        public RecyclerViewAdapter(Context context, List<CardModelDisciplines> objectList) {
            try {
                inflater = LayoutInflater.from(context);
                this.objectList = objectList;
            }catch(Exception ignored){}
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.activity_cardview_disciplines, parent, false);
            final MyViewHolder holder = new MyViewHolder(view);
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lessonName = holder.subjectName.getText().toString();
                    Toast.makeText(inflater.getContext(), lessonName, Toast.LENGTH_SHORT).show();
                    Bundle args = new Bundle();
                    args.putString("name", lessonName);
                    NotesFragment.this.setArguments(args);

                    Intent intent = new Intent(getActivity(), MainActivityNotes.class);
                    intent.putExtra("name",lessonName);
                    startActivity(intent);

/*
                    BodyNotesFragment bodyNotesFragment = BodyNotesFragment.newInstance();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_2, bodyNotesFragment)
                            .addToBackStack(null)
                            .commit();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.note_frame, TableFragment.newInstance());
                    transaction.commit();*/
                }
            });


            return holder;
        }


        @Override
        public int getItemCount() {
            return objectList.size();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CardModelDisciplines current = objectList.get(position);
            holder.setData(current, position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            private CardView mCardView;
            private TextView subjectName;
            private int position;
            private CardModelDisciplines currentObject;




            public MyViewHolder(View itemView) {
                super(itemView);
                mCardView = itemView.findViewById(R.id.card_view_list_disciplines);
                subjectName = itemView.findViewById(R.id.text_subject_name);
            }

            public void setData(CardModelDisciplines currentObject, int position) {
                this.subjectName.setText(currentObject.getDisciplineName());
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

        public void addItem(int position, CardModelDisciplines currentObject) {
            objectList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, objectList.size());
//		notifyDataSetChanged();
        }
    }





}
