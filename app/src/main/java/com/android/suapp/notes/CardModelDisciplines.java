package com.android.suapp.notes;

import com.android.suapp.TableFragment;
import com.android.suapp.suapp.server.notes.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardModelDisciplines {

    private String disciplineName;

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }


    public static List<CardModelDisciplines> getObjectList(List<String> disciplines) {

        List<CardModelDisciplines> disciplinesList = new ArrayList<>();

        for (int i = disciplines.size() - 1; i > -1; i--) {
            System.out.println(disciplines.get(i));
            CardModelDisciplines nature = new CardModelDisciplines();
            nature.disciplineName = disciplines.get(i);
            disciplinesList.add(nature);
        }
        return disciplinesList;
    }


}
