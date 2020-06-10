package org.billthefarmer.scope.ViewModel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.billthefarmer.scope.models.Question;
import java.util.List;

public class BaseViewModel extends AndroidViewModel {

    private MutableLiveData<List<Question>> questions;
    private MutableLiveData<Question> current_question;
    private MutableLiveData<Integer> question_done;
    private MutableLiveData<Integer> question_wrong;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        questions = new MutableLiveData<List<Question>>();
        current_question  = new MutableLiveData<Question>();
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }
    public LiveData<Question> getCurrentQuestion() {
        return current_question;
    }
    public LiveData<Integer> getQuestionDone() {
        return question_done;
    }
    public LiveData<Integer> getQuestionWrong() {
        return question_wrong;
    }

    public void SetQuestionDone(Integer q) {
        question_done.setValue(q);
    }

    public void SetQuestionWrong(Integer q) {
        question_wrong.setValue(q);
    }

    public void SetQuestions(List<Question> q) {
        questions.setValue(q);
    }

    public void SetCurrentQuestions(Question q) {
        current_question.setValue(q);
    }
}
