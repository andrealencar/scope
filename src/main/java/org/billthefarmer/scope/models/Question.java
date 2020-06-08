package org.billthefarmer.scope.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "active")
    public Boolean active;

    @ColumnInfo(name = "remote_id")
    public int id;

    @ColumnInfo(name = "question_title")
    public String title;

    @ColumnInfo(name = "question_alternatives")
    public List alternatives;

    public Question(Integer uid, Integer id, String title, String letra, List alternatives) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.alternatives = alternatives;
    }
}

