package ch.heigvd.amt.mvcProject.business;

import java.util.HashMap;

public class UserService {
    private final HashMap<String, String> database;

    private static class Instance{
        static final UserService instance = new UserService();
    }

    private UserService(){
        database = new HashMap<>();
    }

    public void storeUser(String username, String password){
        database.put(username, password);
    }

    public boolean doesExistUser(String username, String password){
        if(database.containsKey(username)){
            return database.get(username).equals(password);
        }
        return false;
    }

    public static UserService getInstance(){
        return Instance.instance;
    }


}
