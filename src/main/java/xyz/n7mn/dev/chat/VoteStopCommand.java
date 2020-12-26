package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.data.VoteReactionList;

import java.util.List;

public class VoteStopCommand extends VoteCommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public VoteStopCommand(VoteReactionList voteReactionList, TextChannel textChannel, Message message) {
        super(voteReactionList, textChannel, message);
    }

    @Override
    public void run() {
        String[] split = getMessageText().split(" ", -1);

        if (split.length == 1){
            getMessage().reply("準備中...").queue();
            return;
        }

        if (split.length == 2) {
            String[] url = split[1].split("/", -1);

            Guild guild = getJda().getGuildById(url[4]);
            if (guild == null) {
                getMessage().reply("見つからないよぉ").queue();
                return;
            }

            TextChannel textChannelById = guild.getTextChannelById(url[5]);
            if (textChannelById == null) {
                getMessage().reply("見つからないよぉ").queue();
                return;
            }

            textChannelById.retrieveMessageById(url[6]).queue(message1 -> {
                String raw = message1.getContentRaw();
                List<Member> members = guild.getMembers();

                boolean b = false;
                for (Member member : members) {
                    User user = getJda().getUserById(member.getId());
                    if (user != null) {
                        if (raw.contains(user.getName())) {
                            if (getUser().getName().equals(user.getName())) {
                                b = true;
                                break;
                            }
                        }
                    }

                }

                if (!message1.getAuthor().getId().equals("781323086624456735") && !message1.getAuthor().getId().equals("785322639295905792")) {
                    getMessage().reply("それはななみちゃんのメッセージじゃないよぉ").queue();
                    return;
                }

                if (!b) {
                    getMessage().reply("❗投票開始した人以外は投票終了できません！").queue();
                    return;
                }

                List<MessageReaction> reactions = message1.getReactions();
                if (reactions.size() <= 1) {
                    if (reactions.size() == 1 && reactions.get(0).getReactionEmote().getEmoji().equals("✅")) {
                        getMessage().reply("それは結果発表済みみたい...").queue();
                        return;
                    }
                    getMessage().reply("？").queue();
                    return;
                }

                String title = "";
                String[] string = message1.getContentRaw().split("\n", -1);
                if (string[1].startsWith("投票タイトル")) {
                    title = string[1].replaceAll("投票タイトル：", "");
                }

                StopVote(message1, title);
            });
        }
    }
}
