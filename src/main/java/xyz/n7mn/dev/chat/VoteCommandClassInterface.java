package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.data.Vote;
import xyz.n7mn.dev.data.VoteComparator;
import xyz.n7mn.dev.data.VoteReaction;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

import java.util.ArrayList;
import java.util.List;

public abstract class VoteCommandClassInterface implements Chat {

    private final JDA jda;
    private final Guild guild;
    private final TextChannel textChannel;
    private final Member member;
    private final User user;
    private final Message message;
    private final String MessageText;

    private final VoteReactionList voteReactionList;

    public VoteCommandClassInterface(VoteReactionList voteReactionList, TextChannel textChannel, Message message) {

        this.jda = textChannel.getJDA();
        this.guild = textChannel.getGuild();
        this.textChannel = textChannel;
        this.member = message.getMember();
        this.user = message.getAuthor();
        this.message = message;
        this.MessageText = message.getContentRaw();

        this.voteReactionList = voteReactionList;

    }

    @Override
    public JDA getJda() {
        return jda;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public TextChannel getTextChannel(){
        return textChannel;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getMessageText() {
        return MessageText;
    }

    @Override
    public VoteReactionList getVoteReactionList() {
        return voteReactionList;
    }

    @Override
    public MoneySystem getMoneySystem() {
        return null;
    }

    public long getMs(String time){

        long ms = -1;

        try {
            // 秒
            if (time.toLowerCase().endsWith("s")){
                ms = Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("s","")) * 1000;
            }
            // 分
            if (time.toLowerCase().endsWith("m")){
                ms = (Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("m","")) * 60) * 1000;
            }
            // 時
            if (time.toLowerCase().endsWith("h")){
                ms = ((Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("h","")) * 60) * 60) * 1000;
            }
            // 日
            if (time.toLowerCase().endsWith("d")){
                ms = (((Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("h","")) * 60) * 60) * 24) * 1000;
            } else {
                ms = Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("s","")) * 1000;
            }

        } catch (Exception e){
            ms = -1;
        }

        return ms;

    }

    public void StopVote(Message message, String title){

        StringBuffer sb = new StringBuffer(message.getContentRaw());
        message.getChannel().retrieveMessageById(message.getId()).queue(message1 -> {

            List<MessageReaction> reactions = message1.getReactions();
            message1.clearReactions().queue();

            List<Vote> voteResultList = new ArrayList<>();
            List<VoteReaction> list = voteReactionList.getList();

            String[] raw = message.getContentRaw().split("\n");

            int i = 3;
            if (title.length() == 0){
                i = 2;
            }

            for (MessageReaction reaction : reactions){
                List<String> nlist = new ArrayList<>();
                for (VoteReaction voteReaction : list){
                    voteReactionList.deleteList(voteReaction);
                    if (voteReaction.getMessageId().equals(message.getId()) && voteReaction.getEmoji().equals(reaction.getReactionEmote().getEmoji())){
                        if (voteReaction.getMember().getNickname() != null){
                            nlist.add(voteReaction.getMember().getNickname());
                        } else {
                            nlist.add(voteReaction.getMember().getUser().getName());
                        }
                        // System.out.println("a");
                    }
                }
                // System.out.println(nlist.size());
                voteResultList.add(new Vote(reaction.getReactionEmote().getEmoji(), raw[i], nlist.size(), nlist));
                i++;
            }

            voteResultList.sort(new VoteComparator());


            sb.append("\n\n---- 投票結果 ----\n");
            for (Vote vote : voteResultList){
                sb.append(vote.getTitle());
                sb.append(" ");
                sb.append(vote.getNameList().size());
                sb.append("票");
                if (vote.getNameList().size() != 0){
                    sb.append(" (");
                    for (String name : vote.getNameList()){
                        sb.append(name);
                        sb.append("さん,");
                    }
                    sb.append(")\n");
                } else {
                    sb.append("\n");
                }
            }


            message1.editMessage(sb.toString().replaceAll(",\\)",")").replaceAll("まで投票受付中です。","まで投票受付しました。")).queue(message2 -> {
                message2.addReaction("\u2705").queue();
            });

        });

    }

}
