package xyz.n7mn.dev.quiz;

import xyz.n7mn.dev.Database;

import java.sql.Connection;

public class QuizSystem {

    private Connection con;

    public QuizSystem(Database database){
        con = database.getConnect();
    }

}
