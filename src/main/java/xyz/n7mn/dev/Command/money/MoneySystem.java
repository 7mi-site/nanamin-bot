package xyz.n7mn.dev.Command.money;

import net.dv8tion.jda.api.entities.Guild;
import xyz.n7mn.dev.EventListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MoneySystem {

    private static Connection con = null;
    public static final String Currency = "ななみコイン";
    private static int count = 0;

    public static Money getData(String discordUserID){

        int i = 0;
        while (count > 0){
            //System.out.println("count1 : " + count);
            i++;
        }
        con = EventListener.getDatabase().getConnect();
        if (con != null){
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM MoneyList WHERE UserID = ?");
                statement.setString(1, discordUserID);
                ResultSet set = statement.executeQuery();

                if (set.next()){
                    Money money = new Money(set.getString("UserID").replaceAll(" ", ""), set.getInt("Money"));
                    set.close();
                    statement.close();
                    return money;
                }

                set.close();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Money getDefaultData(String discordUserID){

        return new Money(discordUserID, 100);

    }

    public static void createData(String discordUserID){

        con = EventListener.getDatabase().getConnect();
        if (con != null){
            count++;
            new Thread(()->{
                try {

                    PreparedStatement statement = con.prepareStatement("INSERT INTO `MoneyList`(`UserID`, `Money`) VALUES (?, ?)");
                    statement.setString(1, discordUserID);
                    statement.setInt(2, 100);
                    statement.execute();
                    statement.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }).start();
            count--;
        }

    }

    public static void updateData(Money money){

        con = EventListener.getDatabase().getConnect();
        if (con != null){
            count++;
            new Thread(()->{
                try {
                    PreparedStatement statement = con.prepareStatement("UPDATE `MoneyList` SET `Money`= ? WHERE `UserID` = ?");
                    statement.setInt(1, money.getMoney());
                    statement.setString(2, money.getUserID());
                    statement.execute();
                    statement.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }).start();
            count--;
        }

    }

    public static String getCurrency(){

        return Currency;

    }

    public static List<Money> getMoneyList(){
        int i = 0;
        while (count > 0){
            //System.out.println("count2 : " + count);
            i++;
        }
        count++;

        List<Money> moneyList = new ArrayList<>();
        con = EventListener.getDatabase().getConnect();
        if (con != null) {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM MoneyList");
                ResultSet set = statement.executeQuery();
                while (set.next()){
                    moneyList.add(new Money(set.getString("UserID").replaceAll(" ", ""), set.getInt("Money")));
                }
                set.close();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        count--;
        return moneyList;
    }

    public static List<Money> getMoneyList(Guild guild){

        List<Money> moneyList = getMoneyList();
        List<Money> moneyResultList = new ArrayList<>();

        for (Money money : moneyList){
            if (guild.getMemberById(money.getUserID()) != null){
                moneyResultList.add(money);
            }
        }

        return moneyResultList;
    }

    public static int getCount(){
        return count;
    }
}
