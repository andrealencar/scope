package org.billthefarmer.scope.ViewModel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.billthefarmer.scope.AppExecutors;
import org.billthefarmer.scope.database.AppDatabase;
import org.billthefarmer.scope.models.Question;

import java.util.List;

public class BaseViewModel extends AndroidViewModel {

    private List<Question> questions_dao;
    private MutableLiveData<List<Question>> questions;
    private MutableLiveData<Question> current_question;
    private MutableLiveData<Integer> question_done;
    private MutableLiveData<Integer> question_wrong;
    AppDatabase mDb;

    public BaseViewModel(@NonNull Application application) {
        super(application);

        mDb = AppDatabase.getInstance(application);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                questions_dao = mDb.questionDao().getAll();
            }
        });

        questions = new MutableLiveData<List<Question>>();
        current_question  = new MutableLiveData<Question>();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                questions.setValue(questions_dao);
            }
        }, 500);
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
