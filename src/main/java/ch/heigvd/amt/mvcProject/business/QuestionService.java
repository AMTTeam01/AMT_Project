package ch.heigvd.amt.mvcProject.business;

import ch.heigvd.amt.mvcProject.model.Question;
import ch.heigvd.amt.mvcProject.model.User;

import java.util.HashMap;

public class QuestionService {
    // User - Question
    private final HashMap<User, Question> database;

    private static class Instance{
        static final QuestionService instance = new QuestionService();
    }

    private QuestionService(){
        database = new HashMap<>();
    }

    public void storeQuestion(User author, Question question){
        database.put(author, question);
    }

    public static QuestionService getInstance(){
        return Instance.instance;
    }


}
