package xyz.n7mn.dev;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;
import java.util.Random;

class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        String text = event.getMessage().getContentRaw();

        if (author.isBot()){
            return;
        }


        if (text.toLowerCase().startsWith("n.")){
            System.out.println("---- Debug ----\n" + author.getAsTag() + "\n" + text + "\n----- Debug -----");
        }


        if (text.toLowerCase().equals("n.dice")){

            int i = new Random().nextInt(5) + 1;
            event.getMessage().getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();

        }

        if (text.toLowerCase().equals("n.vote")){

            TextChannel textChannel = event.getTextChannel();
            List<Member> members = textChannel.getMembers();
            for (Member member : members){

                if (member.getUser().getAsTag().equals("n7mn-VoteBot#6771")){
                    return;
                }

            }

            Message message = event.getMessage();
            message.delete().queue();

            String msg = message.getAuthor().getAsTag()+"さんっ！\n" +
                    "n.voteのヘルプですっ！\n" +
                    "コマンドの書き方は\n" +
                    "`n.vote <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢20>` または\n" +
                    "`n.vote\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢20>`\nですっ！\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！";

            event.getMessage().getTextChannel().sendMessage(msg).queue();


        }

        if (text.toLowerCase().startsWith("n.vote")){

            TextChannel textChannel = event.getTextChannel();
            List<Member> members = textChannel.getMembers();
            for (Member member : members){

                if (member.getUser().getAsTag().equals("n7mn-VoteBot#6771")){
                    return;
                }

            }

        }
    }

}
