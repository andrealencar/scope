package org.billthefarmer.scope.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity
public class Alternative {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "correct")
    public Boolean correct;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "remote_id")
    public int id;

    @ColumnInfo(name = "alternative_title")
    public String title;

    @ColumnInfo(name = "alternative_letra")
    public String letra;

    public Alternative(Integer uid, Integer id, String title, String question, String letra, Boolean correct) {
        this.uid = uid;
        this.id = id;
        this.correct = correct;
        this.title = title;
        this.question = question;
        this.letra = letra;
    }

}
