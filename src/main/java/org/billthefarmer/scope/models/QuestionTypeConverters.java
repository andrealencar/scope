package org.billthefarmer.scope.models;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.billthefarmer.scope.models.Alternative;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class QuestionTypeConverters {

    Gson gson = new Gson();

    @TypeConverter
    public  List<Alternative> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Alternative>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public  String someObjectListToString(List<Alternative> someObjects) {
        return gson.toJson(someObjects);
    }

}
