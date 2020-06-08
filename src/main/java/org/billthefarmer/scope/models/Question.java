package org.billthefarmer.scope.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "active")
    public Boolean active;

    @ColumnInfo(name = "question_title")
    public String title;

//    @ColumnInfo(name = "question_alternatives")
//    public List<Alternative> alternatives;

}

