package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.votecommand.VoteRegionalData;
import xyz.n7mn.dev.Command.vote.VoteSystem;
import xyz.n7mn.dev.NanamiFunction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Vote extends Chat {


    private String[] regionalList = NanamiFunction.getRegionalList();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Vote(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1000);
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder().setColor(Color.ORANGE);

        if (getMessageText().toLowerCase().equals("n.vote") || getMessageText().toLowerCase().equals("n.votent")){

            HelpData message = getHelpMessage();
            builder.addField(message.getHelpTitle(), message.getHelpMessage(), false).setColor(Color.CYAN);

            getMessage().reply(builder.build()).queue();
            return;
        }


        List<VoteRegionalData> voteRegionalDataList = new ArrayList<>();
        String time = "";
        String title = "(タイトルなし)";

        if (getMessageText().toLowerCase().startsWith("n.vote") && !getMessageText().toLowerCase().startsWith("n.votent")) {

            String text = getMessageText().replaceAll("n.vote ", "");
            String[] temp1 = text.split("\n", -1);
            String[] temp2 = text.replaceAll("　", " ").split(" ", -1);

            int i = 0;
            int c = 0;
            if (temp1.length > 1) {
                for (String temp : temp1) {
                    if (c == 0){
                        c++;
                        continue;
                    }

                    if (c == 1 && !temp.startsWith("t:") && !temp.startsWith("time:")){
                        title = temp;
                        c++;
                        continue;
                    }

                    if (c == 2 && !time.equals("")){
                        title = temp;
                        c++;
                        continue;
                    }

                    if (temp.toLowerCase().startsWith("t:") || temp.toLowerCase().startsWith("time:")){
                        time = temp;
                        i++;
                        continue;
                    }

                    voteRegionalDataList.add(new VoteRegionalData(temp, regionalList[i]));
                    i++;
                    c++;
                }
            } else if (temp2.length > 1) {
                for (String temp : temp2) {
                    if (c == 0 && !temp.startsWith("t:") && !temp.startsWith("time:")){
                        title = temp;
                        c++;
                        continue;
                    }
                    if (c == 1 && !time.equals("")){
                        title = temp;
                        c++;
                        continue;
                    }
                    if (temp.toLowerCase().startsWith("t:") || temp.toLowerCase().startsWith("time:")){
                        time = temp;
                        c++;
                        continue;
                    }

                    voteRegionalDataList.add(new VoteRegionalData(temp, regionalList[i]));
                    i++;
                    c++;
                }
            }

        } else if (getMessageText().toLowerCase().startsWith("n.votent")){

            String text = getMessageText();
            String[] temp1 = text.split("\n", -1);
            String[] temp2 = text.replaceAll("　", " ").split(" ", -1);

            int i = 0;
            int c = 0;
            if (temp1.length > 1) {
                for (String temp : temp1) {
                    if (c == 0){
                        c++;
                        continue;
                    }
                    if (temp.toLowerCase().startsWith("t:") || temp.toLowerCase().startsWith("time:")){
                        time = temp;
                        c++;
                        continue;
                    }

                    voteRegionalDataList.add(new VoteRegionalData(temp, regionalList[i]));
                    i++;
                    c++;
                }
            } else if (temp2.length > 1) {
                for (String temp : temp2) {
                    if (c == 0){
                        c++;
                        continue;
                    }
                    if (temp.toLowerCase().startsWith("t:") || temp.toLowerCase().startsWith("time:")){
                        time = temp;
                        c++;
                        continue;
                    }

                    voteRegionalDataList.add(new VoteRegionalData(temp, regionalList[i]));
                    i++;
                    c++;
                }
            }
        }

        if (voteRegionalDataList.size() == 0){
            getMessage().reply("えらーですっ\n選択肢が見つかりませんでしたっ！！").queue(message -> {
                message.addReaction("\u26A0").queue();
            });
            return;
        }

        if (voteRegionalDataList.size() == 1){
            getMessage().reply("えらーですっ\n選択肢が1つしか見つかりませんでしたっ！！").queue(message -> {
                message.addReaction("\u26A0").queue();
            });
            return;
        }

        if (voteRegionalDataList.size() > 20){
            getMessage().reply("えらーですっ\n選択肢が多すぎます！20個以内でお願いしますっ！！").queue(message -> {
                message.addReaction("\u26A0").queue();
            });
            return;
        }


        builder.setTitle(title);
        final String name;
        if (getMember().getNickname() != null){
            name = getMember().getNickname();
        } else {
            name = getUser().getName();
        }

        builder.setDescription(name+"さんが投票を開始しました。\n以下の選択肢から投票をしてください。");

        long ms = NanamiFunction.getMs(time);
        if (!time.equals("")){
            builder.setFooter(sdf.format(new Date(new Date().getTime() + ms)) + "ごろ まで投票受付中です。");
        } else {
            builder.setFooter("開始した人が終了するまで投票受付中です。");
        }

        StringBuffer sb = new StringBuffer();
        for (VoteRegionalData voteRegionalData : voteRegionalDataList){
            sb.append(voteRegionalData.getEmoji());
            sb.append(" : ");
            sb.append(voteRegionalData.getName());
            sb.append("\n");
        }

        builder.addField("選択肢", sb.toString(), false);

        getMessage().delete().queue();
        getTextChannel().sendMessage(builder.build()).queue(message -> {
            for (VoteRegionalData voteData : voteRegionalDataList){
                message.addReaction(voteData.getEmoji()).queue();
            }

            VoteSystem system = new VoteSystem();
            system.voteStart(message);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    NanamiFunction.VoteStop(message);
                }
            };

            if (ms != -1){
                timer.schedule(task, ms);
            }
        });
    }
}
