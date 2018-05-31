package com.android.suapp.notes;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.suapp.R;
import com.android.suapp.SignUpActivity;
import com.android.suapp.messages.CardModel;
import com.android.suapp.messages.NotificationsFragment;
import com.android.suapp.suapp.sdk.SUAppServer;
import com.android.suapp.suapp.server.database.objects.Message;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.database.objects.StudyGroup;
import com.android.suapp.suapp.server.notes.Note;
import com.android.suapp.suapp.server.responses.ServerResponse;
import com.android.suapp.suapp.server.utility.MessagesListWrapper;
import com.android.suapp.suapp.server.utility.NoteListWrapper;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.android.suapp.LoginActivity.APP_PREFERENCES;
import static com.android.suapp.LoginActivity.APP_PREFERENCES_STUDENT_DATA;
import static com.android.suapp.LoginActivity.APP_PROFESSION;
import static com.android.suapp.notes.NotesFragment.list;

public class BodyNotesFragment extends Fragment {

    public static List<Note> list;
    public static int noteCount;
    private ImageButton newNote;
    private SharedPreferences sp;
    private String sourceData;
    private TextView noteDeadline;
    private Context mContext;
    private Student student;
    private String discipline;
    private static String dateDeadLine;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;





    Integer  myYear = null;
    Integer myMonth = null;
    Integer myDay = null;

    final Handler h = new Handler();
    Calendar dateline = Calendar.getInstance();

    private static BodyNotesFragment instance;
    public static BodyNotesFragment newInstance() {
        instance = new BodyNotesFragment();
        return instance;
    }

    private void getNotes(){
        try{
            String listOfNote = SUAppServer.getNote(discipline ,student.getToken());
            NoteListWrapper listWrapper = new Gson().fromJson(listOfNote, NoteListWrapper.class);
            noteCount = listWrapper.getList().size();
            list = listWrapper.getList();
            System.out.println(noteCount);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(discipline, listWrapper.toString());
            editor.apply();
        }catch (ConnectException e){
            h.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mContext, "Оффлайн-заметки", Toast.LENGTH_SHORT).show();
                }
            });
            String listOfNote = sp.getString(discipline, null);
            if (listOfNote == null){
                list = new ArrayList<>();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BodyNotesFragment.this.getContext(), "Заметок нет", Toast.LENGTH_SHORT).show();

                    }
                });
                return;
            }
            NoteListWrapper listWrapper = new Gson().fromJson(listOfNote, NoteListWrapper.class);
            noteCount = listWrapper.getList().size();
            list = listWrapper.getList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_note, container, false);
        mContext = getActivity();
        discipline = MainActivityNotes.discipline;
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_2);
        swipeRefreshLayout.setRefreshing(true);
        sp = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        try {
            sourceData = sp.getString(APP_PREFERENCES_STUDENT_DATA, null);
            student =new Gson().fromJson(sourceData, Student.class);
        }
        catch (Exception e){
            Toast.makeText(getContext(), "Не удалось загрузить данные о пользователе", Toast.LENGTH_SHORT).show();
        }

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view_all_notes);
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                getNotes();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(new RecyclerViewAdapter(BodyNotesFragment.this.getContext(), CardModelNote.getObjectList(list)));
                            swipeRefreshLayout.setRefreshing(false);
                        }catch (NullPointerException e){}
                    }
                }, 100);
            }
        }).start();


        newNote = view.findViewById(R.id.new_note);
        final SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler h = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getNotes();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new RecyclerViewAdapter(BodyNotesFragment.this.getContext(), CardModelNote.getObjectList(list)));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 100);
                    }
                }).start();
            }
        };
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_note, null);
                final String noteData;
                noteDeadline = dialogView.findViewById(R.id.note_text_data_end);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                noteData = simpleDateFormat.format(new Date());
                noteDeadline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDate(v);
                    }
                });
                setInitialDateTime();
                builder.setView(dialogView)
                        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final EditText noteBody = dialogView.findViewById(R.id.edit_text_note);
                                        final Handler h = new Handler();
                                        swipeRefreshLayout.setRefreshing(true);
                                        myYear = dateline.get(Calendar.YEAR);
                                        myMonth = dateline.get(Calendar.MONTH)+1;
                                        myDay = dateline.get(Calendar.DAY_OF_MONTH);
                                        String mstr = "";
                                        String dstr = "";
                                        if(myMonth < 10){
                                            mstr = "0";
                                        }
                                        if(myDay < 10){
                                            dstr = "0";
                                        }
                                        dateDeadLine = dstr + myDay + "/" + mstr + myMonth + "/"  + myYear;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    final String message = SUAppServer.addNote(
                                                            new Note()
                                                                    .setDeadline(dateDeadLine)
                                                                    .setStart(noteData)
                                                                    .setText(noteBody.getText().toString())
                                                                    .setLesson(discipline),
                                                            student.getToken());
                                                    final ServerResponse r = new Gson().fromJson(message, ServerResponse.class);
                                                    h.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(), r.getResponse(), Toast.LENGTH_SHORT).show();
                                                            listener.onRefresh();
                                                        }
                                                    });
                                                }catch(ConnectException e){
                                                    h.post(new Runnable() {
                                                        @Override
                                                        public void run() {Toast.makeText(getContext(), "Сервер не отвечает", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } catch (Exception ignored) {
                                                }finally {
                                                    h.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            swipeRefreshLayout.setRefreshing(false);
                                                        }
                                                    });
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
                }
        });

        swipeRefreshLayout.setOnRefreshListener(listener);

        return view;

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<BodyNotesFragment.RecyclerViewAdapter.MyViewHolder>{

        private List<CardModelNote> objectList;
        private LayoutInflater inflater;

        public RecyclerViewAdapter(Context context, List<CardModelNote> objectList) {
            try {
                inflater = LayoutInflater.from(context);
                this.objectList = objectList;
            }catch(Exception ignored){}
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.activity_cardview_note, parent, false);
            RecyclerViewAdapter.MyViewHolder holder = new RecyclerViewAdapter.MyViewHolder(view);
            return holder;
        }


        @Override
        public int getItemCount() {
            return objectList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
            CardModelNote current = objectList.get(position);
            holder.setData(current, position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private CardView mCardView;
            private TextView noteBody;
            private TextView noteData;
            private TextView noteDeadline;
            private int position;
            private CardModelNote currentObject;

            public MyViewHolder(View itemView) {
                super(itemView);
                mCardView = itemView.findViewById(R.id.card_view_note);
                noteBody = itemView.findViewById(R.id.text_note);
                noteData = itemView.findViewById(R.id.note_data);
                noteDeadline = itemView.findViewById(R.id.note_data_end);
            }

            public void setData(CardModelNote currentObject, int position) {
                this.noteBody.setText(currentObject.getNoteBody());
                this.noteData.setText(currentObject.getNoteData());
                this.noteDeadline.setText(currentObject.getNoteDeadline());
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

        public void addItem(int position, CardModelNote currentObject) {
            objectList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, objectList.size());
//		notifyDataSetChanged();
        }
    }


    private void setInitialDateTime() {

        noteDeadline.setText(DateUtils.formatDateTime(getContext(),
                dateline.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateline.set(Calendar.YEAR, year);
            dateline.set(Calendar.MONTH, monthOfYear);
            dateline.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    public void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                dateline.get(Calendar.YEAR),
                dateline.get(Calendar.MONTH),
                dateline.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}


