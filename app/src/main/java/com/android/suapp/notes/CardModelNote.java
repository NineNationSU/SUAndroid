package com.android.suapp.notes;

import com.android.suapp.suapp.server.notes.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CardModelNote {

    private String noteBody;
    private String noteData;
    private String noteDeadline;

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }


    public String getNoteData() {
        return noteData;
    }

    public void setNoteData(String noteData) {
        this.noteData = noteData;
    }

    public String getNoteDeadline() {
        return noteDeadline;
    }

    public void setNoteDeadline(String noteDeadline) {
        this.noteDeadline = noteDeadline;
    }


    public static List<CardModelNote> getObjectList(List<Note> notes) {

        List<CardModelNote> noteList = new ArrayList<>();

        for (int i = notes.size() - 1; i > -1; i--) {
            System.out.println(notes.get(i));
            CardModelNote nature = new CardModelNote();
            nature.noteBody = notes.get(i).getText();
            nature.noteData = notes.get(i).getStart();
            nature.noteDeadline = notes.get(i).getDeadline();
            noteList.add(nature);
        }
        return noteList;
    }
}
