package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.NanamiFunction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;


public class VoteStop extends Chat {

    private final EmbedBuilder builder = new EmbedBuilder();

    public VoteStop(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1000);
    }

    @Override
    public void run() {

        String[] textSplit = getMessageText().split(" ", -1);

        if (textSplit.length == 1 && !getMessageText().equalsIgnoreCase("n.stopvote")){
            return;
        }

        if (textSplit.length == 1){
            getMessage().reply("準備中です...").queue();
            return;
        }

        if (textSplit.length == 2){

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

            textChannelById.retrieveMessageById(url[6]).queue(message1 -> {
                String desc = message1.getEmbeds().get(0).getDescription();
                // System.out.println(desc);

                boolean isCreateUser = false;
                if (desc != null){
                    for (Member member : message1.getTextChannel().getMembers()){
                        if (member.getNickname() != null && desc.startsWith(member.getNickname())){
                            isCreateUser = true;
                            break;
                        }

                        if (desc.startsWith(member.getUser().getName())){
                            isCreateUser = true;
                            break;
                        }

                    }
                }

                if (!isCreateUser){
                    getMessage().reply("えらー！\n投票した人しか終了できませんっ！").queue();
                    return;
                }

                NanamiFunction.VoteStop(message1);
                getMessage().delete().queue();
                getTextChannel().sendMessage("投票を終了しましたっ\n"+message1.getJumpUrl()).queue();
            });

            return;
        }

        getMessage().reply("えらーですっ！\n`n.stopVote`または `n.stopVote <URL>`と入力してくださいっ").queue();
    }
}
