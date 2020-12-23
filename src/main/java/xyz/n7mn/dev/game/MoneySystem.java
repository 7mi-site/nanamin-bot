package xyz.n7mn.dev.game;

import com.google.gson.Gson;
import xyz.n7mn.dev.data.MoneyComparator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class MoneySystem {

    private Connection con = null;

    String coinName = "ななみコイン";
    int defaultCoin = 100;

    List<Money> moneyList = Collections.synchronizedList(new ArrayList<>());


    public MoneySystem(){
        Gson gson = new Gson();
        String[] json = gson.fromJson(fileRead("./mysql.json"), String[].class);

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + json[0] + ":"+json[1]+"/" + json[2] + json[3], json[4], json[5]);
            con.setAutoCommit(true);

            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Money");
            ResultSet resultSet = preparedStatement.executeQuery();
            synchronized (moneyList){
                while (resultSet.next()){
                    moneyList.add(new Money(UUID.fromString(resultSet.getString("UserID")), resultSet.getString("DiscordUserID"), resultSet.getInt("Money")));
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        TimerTask task = new TimerTask() {
            public void run() {
                new Thread(()->{
                    try {
                        PreparedStatement statement = con.prepareStatement("SELECT * FROM Money");
                        statement.execute();
                        statement.close();
                    } catch (Exception e){

                        try {
                            con = DriverManager.getConnection("jdbc:mysql://" + json[0] + ":"+json[1]+"/" + json[2] + json[3], json[4], json[5]);
                            con.setAutoCommit(true);
                        } catch (SQLException e1){
                            // e1.printStackTrace();
                        }
                    }
                }).start();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, (1000 * 60));
    }

    public void Update(){

        new Thread(()->{
            if (con != null){

                List<Money> cList;
                synchronized (moneyList){
                    cList = new ArrayList<>(moneyList);
                }

                try {

                    for (Money money : cList){
                        PreparedStatement statement = con.prepareStatement("UPDATE `Money` SET `Money`= ? WHERE UserID = ?");
                        statement.setInt(1, money.getMoney());
                        statement.setString(2, money.getUserID().toString());
                        statement.execute();
                        statement.close();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public List<Money> getMoneyList(){
        List<Money> moneyCList;

        synchronized (moneyList){
            moneyCList = new ArrayList<>(moneyList);
        }

        moneyCList.sort(new MoneyComparator());
        return moneyCList;
    }

    public Money getMoney(String discordUserID){
        List<Money> moneyCList;

        synchronized (moneyList){
            moneyCList = new ArrayList<>(moneyList);
        }

        for (Money money : moneyCList){
            if (money.getDiscordUserID().equals(discordUserID)){
                moneyCList.clear();
                return money;
            }
        }

        Money money = new Money(discordUserID, defaultCoin);
        new Thread(()->{
            try {
                PreparedStatement statement = con.prepareStatement("INSERT INTO `Money` (`UserID`, `DiscordUserID`, `Money`) VALUES (?, ?, ?) ");
                statement.setString(1, money.getUserID().toString());
                statement.setString(2, money.getDiscordUserID());
                statement.setInt(3, money.getMoney());
                statement.execute();
                statement.close();
                synchronized (moneyList){
                    moneyList.add(money);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }).start();

        moneyCList.clear();

        return money;
    }

    public void setMoney(String discordUserID, int moneyInt){
        List<Money> moneyCList = new ArrayList<>();

        synchronized (moneyList){
            moneyCList.addAll(moneyList);
        }

        for (Money money : moneyCList){
            if (money.getDiscordUserID().equals(discordUserID)){
                Money money1 = new Money(money.getUserID(), money.getDiscordUserID(), moneyInt);
                synchronized (moneyList){
                    moneyList.remove(money);
                    moneyList.add(money1);
                }

            }
        }

        Update();
    }

    public String getCurrency(){
        return coinName;
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
