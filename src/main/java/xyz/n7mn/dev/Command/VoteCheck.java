package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.Command.vote.VoteData;
import xyz.n7mn.dev.Command.vote.VoteSystem;
import xyz.n7mn.dev.Command.votecommand.VoteResultData;
import xyz.n7mn.dev.NanamiFunction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.util.ArrayList;
import java.util.List;

public class VoteCheck extends Chat {
    public VoteCheck(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return Help.getHelpData(1017);
    }

    @Override
    public void run() {

        String[] textSplit = getMessageText().split(" ", -1);

        if (textSplit.length == 1 && getMessageText().toLowerCase().equals("n.votecheck")){
            getMessage().reply("準備中ですっ！").queue();
            return;
        }

        if (textSplit.length != 2){
            getMessage().reply("えらーです！\n`n.voteCheck <メッセージURL>`でお願いしますっ！！").queue();
            return;
        }

        String[] url = textSplit[1].split("/", -1);
        Guild guild = getGuild().getJDA().getGuildById(url[4]);
        if (guild == null) {
            getMessage().reply("見つからないよぉ").queue();
            return;
        }

        TextChannel textChannelById = guild.getTextChannelById(url[5]);
        if (textChannelById == null) {
            getMessage().reply("見つからないよぉ").queue();
            return;
        }

        try {
            textChannelById.retrieveMessageById(url[6]).queue(message -> {
                List<VoteData> list = VoteSystem.getVoteDataList(message);

                EmbedBuilder builder = new EmbedBuilder();

                builder.setTitle("結果データ");
                builder.setDescription("全体票：" + list.size() + "票");

                List<VoteResultData> resultDataList = new ArrayList<>();
                String[] regionalList = NanamiFunction.getRegionalList();
                for (int i = 0; i < message.getReactions().size(); i++){
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

                StringBuffer sb = new StringBuffer();
                for (VoteResultData resultData : resultDataList){
                    sb.append(resultData.getEmoji());
                    sb.append(" : ");
                    sb.append(resultData.getCount());
                    sb.append("票\n");
                }

                builder.setTitle("途中結果", getMessage().getJumpUrl());
                builder.setDescription(sb.toString());

                PrivateChannel sendUserDM = getUser().openPrivateChannel().complete();
                sendUserDM.sendMessage(builder.build()).queue();
                getMessage().reply("途中結果をお送りしましたっ").queue();
            });
        } catch (Exception e){
            getMessage().reply("見つからないよぉ").queue();
        }
    }
}
