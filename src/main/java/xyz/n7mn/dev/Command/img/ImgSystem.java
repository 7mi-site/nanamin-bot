package xyz.n7mn.dev.Command.img;

import xyz.n7mn.dev.Database;
import xyz.n7mn.dev.EventListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImgSystem {

    public static List<ImageData> getImageDataList(String id){

        Database database = EventListener.getDatabase();
        Connection con = database.getConnect();
        List<ImageData> dataList = new ArrayList<>();

        if (con != null){
            try {
                PreparedStatement preparedStatement;
                if (id.equals("ys")){
                    preparedStatement = con.prepareStatement("SELECT * FROM ImageList");
                }else{
                    preparedStatement = con.prepareStatement("SELECT * FROM ImageList WHERE Command = ?");
                    preparedStatement.setString(1, id);
                }

                ResultSet set = preparedStatement.executeQuery();
                while (set.next()){

                    ImageData data = new ImageData(
                            "https://n.7mi.site/nana-bot/"+set.getString("Command").replaceAll("gold","kouta").replaceAll("yululi2","yululi")+"/"+set.getString("FileName"),
                            set.getString("Comment")
                    );

                    dataList.add(data);

                }
                set.close();
                preparedStatement.close();

                con.close();

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return dataList;
    }

}
