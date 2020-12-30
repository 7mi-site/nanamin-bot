package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoteCheck {

    public VoteCheck(JDA jda, Database database){
        Connection con = database.getConnect();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM VoteList");
            ResultSet set = statement.executeQuery();

            while (set.next()){
                String id = set.getString("MessageID").replaceAll(" ", "");
                List<Guild> guilds = jda.getGuilds();

                for (Guild guild : guilds){
                    List<TextChannel> channels = guild.getTextChannels();
                    for (TextChannel channel : channels){
                        try {
                            Message message = channel.retrieveMessageById(id).complete();
                            if (message != null){
                                if (message.getEmbeds().size() == 0){
                                    continue;
                                }

                                MessageEmbed embed = message.getEmbeds().get(0);
                                String text = embed.getFooter().getText();
                                if (text == null){
                                    continue;
                                }
                                if (text.toLowerCase().startsWith("開始した人が終了するまで")){
                                    continue;
                                }

                                Matcher matcher = Pattern.compile("(.*)ごろ").matcher(text);
                                if (matcher.find()){
                                    System.out.println(matcher.group(1));

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date messageDate = sdf.parse(matcher.group(1));
                                    Date nowDate = new Date();

                                    if (nowDate.getTime() >= messageDate.getTime()){
                                        NanamiFunction.VoteStop(message);
                                        System.out.println("終了してから起動の間に期限が過ぎたものを終了しました");
                                        return;
                                    }

                                    Timer timer = new Timer();
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            NanamiFunction.VoteStop(message);
                                        }
                                    };
                                    timer.schedule(task, messageDate);
                                    System.out.println(matcher.group(1)+"までのタイマーセットし直ししました");
                                }
                            }
                        } catch (Exception e){
                            // e.printStackTrace();
                        }
                    }
                }

            }


        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
