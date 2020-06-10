package org.billthefarmer.scope.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class QuestionTypeConverters {

    @TypeConverter
    public static ArrayList<Alternative> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Alternative>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Alternative> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
