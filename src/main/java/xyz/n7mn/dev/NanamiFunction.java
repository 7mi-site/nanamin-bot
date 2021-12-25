package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.Command.votecommand.VoteComparator;
import xyz.n7mn.dev.Command.vote.VoteData;
import xyz.n7mn.dev.Command.votecommand.VoteResultData;
import xyz.n7mn.dev.Command.vote.VoteSystem;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NanamiFunction {

    private static String ver = "1.4.7-fix1";

    public static String getVersion(){
        return ver;
    }

    public static String fileRead(String pass){
        File file = new File(pass);
        if (!file.isFile()){
            System.out.println("存在しないファイル");
            return "{}";
        }

        BufferedReader buffer = null;
        String json;
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

            json = sb.toString();

        } catch (FileNotFoundException e) {
            json = "{}";
        } catch (IOException e) {
            e.printStackTrace();
            json = "{}";
        } finally {
            try {
                if (buffer != null){
                    buffer.close();
                }
            } catch (IOException e) {
                json = "{}";
            }
        }

        return json;
    }

    public static String[] getRegionalList() {
        String[] regional = new String[]{
                "\uD83C\uDDE6",
                "\uD83C\uDDE7",
                "\uD83C\uDDE8",
                "\uD83C\uDDE9",
                "\uD83C\uDDEA",
                "\uD83C\uDDEB",
                "\uD83C\uDDEC",
                "\uD83C\uDDED",
                "\uD83C\uDDEE",
                "\uD83C\uDDEF",
                "\uD83C\uDDF0",
                "\uD83C\uDDF1",
                "\uD83C\uDDF2",
                "\uD83C\uDDF3",
                "\uD83C\uDDF4",
                "\uD83C\uDDF5",
                "\uD83C\uDDF6",
                "\uD83C\uDDF7",
                "\uD83C\uDDF8",
                "\uD83C\uDDF9"
        };

        return regional;
    }


    public static long getMs(String time){

        long ms = -1;
        Matcher matcher = Pattern.compile("(\\d+)").matcher(time);
        if (matcher.find()){
            // System.out.println(matcher.group(1));
        }

        try {
            // 秒
            if (time.toLowerCase().endsWith("s")){
                ms = Long.parseLong(matcher.group(1)) * 1000;
            } else if (time.toLowerCase().endsWith("m")){
                ms = (Long.parseLong(matcher.group(1)) * 60) * 1000;
            } else if (time.toLowerCase().endsWith("h")){
                ms = ((Long.parseLong(matcher.group(1)) * 60) * 60) * 1000;
            } else if (time.toLowerCase().endsWith("d")){
                ms = (((Long.parseLong(matcher.group(1)) * 60) * 60) * 24) * 1000;
            } else {
                ms = Long.parseLong(matcher.group(1)) * 1000;
            }

        } catch (Exception e){
            ms = -1;
        }

        return ms;

    }

    public static void VoteStop(Message message){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        message.getTextChannel().retrieveMessageById(message.getId()).queue(message1 -> {
            List<MessageReaction> reactions = message1.getReactions();
            int reactionCount = reactions.size();
            message.clearReactions().queue();

            // System.out.println(reactionCount);
            EmbedBuilder builder = new EmbedBuilder();

            VoteSystem system = new VoteSystem();

            if (!system.isVote(message)){
                message.reply("えらー！\nこの投票はすでに終了している可能性がありますっ！！").queue();
                return;
            }

            List<VoteData> list = VoteSystem.getVoteDataList(message);
            List<VoteResultData> resultDataList = new ArrayList<>();
            String[] regionalList = NanamiFunction.getRegionalList();
            for (int i = 0; i < reactionCount; i++){
                resultDataList.add(new VoteResultData(regionalList[i], 0));
            }

            for (VoteData data : list){
                for (VoteResultData resultData : resultDataList){
                    if (resultData.getEmoji().equals(data.getEmoji())){
                        resultData.setCount(resultData.getCount() + 1);
                        break;
                    }
                }
            }

            resultDataList.sort(new VoteComparator());

            List<MessageEmbed.Field> fields = message1.getEmbeds().get(0).getFields();


            StringBuffer sb = new StringBuffer("**投票結果**\n");
            sb.append("投票タイトル : `");
            sb.append(message1.getEmbeds().get(0).getTitle());
            sb.append("`\n");
            for (VoteResultData data : resultDataList){
                for (MessageEmbed.Field field : fields){
                    //System.out.println(field.getName());
                    if (field.getName() != null && field.getName().equals("選択肢")){
                        String value = field.getValue();
                        //System.out.println(value);
                        String[] valueList = value.split("\n", -1);
                        for (String v : valueList){
                            String[] str = v.split(" ", -1);
                            if (str[0].equals(data.getEmoji())){
                                sb.append(str[0]);
                                sb.append(" ");
                                sb.append(str[2]);
                                sb.append(" : ");
                                sb.append(data.getCount());
                                sb.append("票\n");
                            }
                        }

                    }
                }

            }

            builder.setTitle("結果データ");
            builder.setDescription("全体票：" + list.size() + "票");
            for (VoteResultData result : resultDataList){
                StringBuffer nameList = new StringBuffer();
                for (VoteData data : list){
                    if (result.getEmoji().equals(data.getEmoji())){
                        JDA jda = message.getJDA();
                        Guild guild = jda.getGuildById(message.getGuild().getId());
                        Member member = guild.getMemberById(data.getUserId());
                        if (member != null){
                            if (member.getNickname() != null){
                                nameList.append(member.getNickname());
                                nameList.append("さん\n");
                            } else {
                                nameList.append(member.getUser().getName());
                                nameList.append("さん\n");
                            }
                        }

                    }
                }

                String substring;
                if (nameList.length() >= 2){
                    substring = nameList.substring(0, nameList.length() - 1);
                } else {
                    substring = nameList.toString();
                }
                if (result.getCount() != 0){
                    builder.addField(result.getEmoji() + " ("+result.getCount()+"票)",substring,false);
                } else {
                    builder.addField(result.getEmoji() + " ("+result.getCount()+"票)","",false);
                }


            }

            builder.setColor(Color.GREEN);
            message1.editMessage(sb.toString()).embed(builder.build()).queue(message2 -> {
                message2.addReaction("\u2705").queue();
            });

            system.voteStop(message);
        });

    }

}
