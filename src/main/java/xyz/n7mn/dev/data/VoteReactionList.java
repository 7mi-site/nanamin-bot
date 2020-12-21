package xyz.n7mn.dev.data;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class VoteReactionList {

    private List<VoteReaction> reactionList = Collections.synchronizedList(new ArrayList<>());
    private Connection con;

    public VoteReactionList(JDA jda){

        Gson gson = new Gson();
        String[] json = gson.fromJson(fileRead("./mysql.json"), String[].class);

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + json[0] + ":"+json[1]+"/" + json[2] + json[3], json[4], json[5]);
            con.setAutoCommit(true);

            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM EmoteList");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                Guild guild = jda.getGuildById(resultSet.getString("GuildID"));
                TextChannel textChannel = jda.getTextChannelById(resultSet.getString("TextChannnelID"));
                Message message = null;
                try {
                    message = textChannel.retrieveMessageById(resultSet.getString("MessageID")).complete();
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                Member member = null;
                try {
                    member = guild.getMemberById(resultSet.getString("DiscordMemberID"));
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                Emote emote = null;
                try {
                    emote = jda.getEmoteById(resultSet.getString("EmoteID"));
                } catch (Exception e) {
                    // e.printStackTrace();
                }

                synchronized (reactionList) {
                    try {
                        reactionList.add(new VoteReaction(uuid, guild, textChannel, member, message.getId(), emote));
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        TimerTask task = new TimerTask() {
            public void run() {
                new Thread(() -> {
                    try {
                        PreparedStatement statement = con.prepareStatement("SELECT * FROM EmoteList");
                        statement.execute();
                        statement.close();
                    } catch (SQLException e) {

                        try {
                            con = DriverManager.getConnection("jdbc:mysql://" + json[0] + ":" + json[1] + "/" + json[2] + json[3], json[4], json[5]);
                            con.setAutoCommit(true);
                        } catch (SQLException e1) {
                            // e1.printStackTrace();
                        }

                    }
                }).start();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, (1000 * 60));

    }

    public void addList(VoteReaction voteReaction){

        synchronized (reactionList){
            reactionList.add(voteReaction);
        }

        new Thread(()->{
            try {
                PreparedStatement statement = con.prepareStatement("INSERT INTO `EmoteList` (`UUID`, `GuildID`, `TextChannelID`, `MessageID`, `DiscordMemberID`, `EmoteID`) VALUES (?, ?, ?, ?, ?, ?) ");
                statement.setString(1, voteReaction.getUuid().toString());
                statement.setString(2, voteReaction.getGuild().getId());
                statement.setString(3, voteReaction.getChannel().getId());
                statement.setString(4, voteReaction.getMessageId());
                statement.setString(5, voteReaction.getMember().getId());
                statement.setString(6, voteReaction.getEmote().getId());
                statement.execute();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    public void deleteList(VoteReaction voteReaction){

        synchronized (reactionList){
            for (VoteReaction voteReaction1 : reactionList){
                if (voteReaction1.getEmote().getId().equals(voteReaction.getEmote().getId()) && voteReaction1.getMessageId().equals(voteReaction.getMessageId())){
                    reactionList.remove(voteReaction1);
                    return;
                }
            }
        }

        new Thread(()->{
            try {
                PreparedStatement statement = con.prepareStatement("DELETE FROM `EmoteList` WHERE UUID = ?");
                statement.setString(1, voteReaction.getUuid().toString());
                statement.execute();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();

    }

    public List<VoteReaction> getList(){

        List<VoteReaction> voteReaction = new ArrayList<>();
        synchronized (reactionList){
            voteReaction.addAll(reactionList);
        }

        return voteReaction;
    }

    public VoteReaction getData(UUID uuid){
        List<VoteReaction> list = getList();
        for (VoteReaction voteReaction : list){

            if (voteReaction.getUuid().equals(uuid)){
                return voteReaction;
            }

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
