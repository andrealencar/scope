package org.billthefarmer.scope.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alternative {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "correct")
    public Boolean correct;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "alternative_title")
    public String title;

    @ColumnInfo(name = "alternative_letra")
    public String letra;


}
