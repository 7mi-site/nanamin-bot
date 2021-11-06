package xyz.n7mn.dev;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Gson gson = new Gson();
    private String[] json = gson.fromJson(fileRead("./mysql.json"), String[].class);

    public Connection getConnect() {

        Connection con;

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + json[0] + ":"+json[1]+"/" + json[2] + json[3], json[4], json[5]);
            con.setAutoCommit(true);
            return con;
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return null;
    }


    private String fileRead(String pass){

        if (System.getProperty("os.name").toLowerCase().startsWith("windows")){
            pass = pass.replaceAll("/", "\\\\");
        }

        File file = new File(pass);
        BufferedReader buffer = null;
        try {
            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input, StandardCharsets.UTF_8);
            buffer = new BufferedReader(stream);
            StringBuffer sb = new StringBuffer();

            int ch = buffer.read();
            while (ch != -1){
                sb.append((char) ch);
                ch = buffer.read();
            }

            return sb.toString();
        } catch (FileNotFoundException e) {
            try{
                file.createNewFile();
            } catch (IOException ioException) {
                // e.printStackTrace();
            }
            return "{}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        } finally {
            try {
                if (buffer != null){
                    buffer.close();
                }
            } catch (IOException e) {
                // return "{}";
            }
        }
    }
}
