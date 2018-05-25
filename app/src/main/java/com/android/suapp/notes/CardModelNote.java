package com.android.suapp.notes;

import com.android.suapp.suapp.server.notes.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardModelNote {

    private String noteBody;

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }


    public static List<CardModelNote> getObjectList(List<Note> notes) {

        List<CardModelNote> noteList = new ArrayList<>();

        for (int i = notes.size() - 1; i > -1; i--) {
            System.out.println(notes.get(i));
            CardModelNote nature = new CardModelNote();
            nature.noteBody = notes.get(i).getText();
            noteList.add(nature);
        }
        return noteList;
    }

}
