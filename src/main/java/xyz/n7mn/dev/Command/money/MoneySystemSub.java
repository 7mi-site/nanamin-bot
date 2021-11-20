package xyz.n7mn.dev.Command.money;

import xyz.n7mn.dev.EventListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoneySystemSub {

    private static List<Money> moneyList = Collections.synchronizedList(new ArrayList<>());

    public static List<Money> getMoneyList() {

        List<Money> tempList = new ArrayList<>();

        Connection con = EventListener.getDatabase().getConnect();
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM MoneyList");
                ResultSet set = statement.executeQuery();
                while (set.next()){
                    tempList.add(new Money(set.getString("UserID").replaceAll(" ", ""), set.getInt("Money")));
                }
                set.close();
                statement.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        synchronized (moneyList){
            moneyList.clear();
            moneyList.addAll(tempList);
        }
        return tempList;
    }

    public static boolean isFoundData(String userId){

        List<Money> tempList = new ArrayList<>();

        synchronized (moneyList){
            tempList.addAll(moneyList);
        }

        for (Money money : tempList){

            if (money.getUserID().equals(userId)){
                return true;
            }

        }

        return false;
    }

    public static void create(String userId){

        synchronized (moneyList){
            moneyList.add(new Money(userId, 100));
        }

    }

    public static void update(Money money){

        List<Money> tempList = new ArrayList<>();
        synchronized (moneyList){
            tempList.addAll(moneyList);
        }

        int i = 0;
        for (Money money1 : tempList){

            if (money1.getUserID().equals(money.getUserID())){
                synchronized (moneyList){
                    moneyList.remove(i);
                }
            }
            i++;
        }

        synchronized (moneyList){
            moneyList.add(money);
        }
    }

    public static Money getMoneyData(String userId){

        List<Money> tempList = new ArrayList<>();
        synchronized (moneyList){
            tempList.addAll(moneyList);
        }

        for (Money money : tempList){
            if (money.getUserID().equals(userId)){
                return money;
            }
        }

        return null;
    }

}
