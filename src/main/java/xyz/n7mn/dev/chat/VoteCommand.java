package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

import java.text.SimpleDateFormat;
import java.util.*;

public class VoteCommand extends VoteCommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public VoteCommand(VoteReactionList voteReactionList, TextChannel textChannel, Message message) {
        super(voteReactionList, textChannel, message);
    }

    @Override
    public void run() {


        // 前身のn7mn-VoteBot対策
        List<Member> members = getTextChannel().getMembers();
        for (Member member : members){

            if (member.getUser().getId().equals("781130665906274317")){
                return;
            }

        }

        if (getMessageText().toLowerCase().equals("n.vote") || getMessageText().toLowerCase().equals("n.votent")){

            getMessage().delete().queue();

            String msg = getMessage().getAuthor().getName()+"さんっ！\n" +
                    "n.voteのヘルプですっ！\n" +
                    "コマンドの書き方は\n" +
                    "`n.vote <時間> <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢19>` または\n" +
                    "`n.vote\n<時間>\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢19>`\nですっ！\n" +
                    "時間の指定は以下の通りでできます！ (`t:`は`time:`に置き換えてもできます！)\n" +
                    "`t:(時間)s または t:(時間) --- 秒\n" +
                    "t:(時間)m --- 分\n" +
                    "t:(時間)h --- 時\n" +
                    "t:(時間)d --- 日`\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！\n" +
                    "(投票を終了するには「n.voteStop <メッセージリンクのURL>」と入力してください)";

            getTextChannel().sendMessage(msg).queue();
            return;

        }

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

        String[] string;

        if (getMessageText().split("\n",-1).length >= 3){
            string = getMessageText().split("\n", -1);
        } else {
            String text1 = getMessageText().replaceAll("　"," ");
            string = text1.split(" ", -1);
        }

        if (string.length <= 3 && (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:"))){
            getMessage().reply("えらーですっ！選択肢が見つかりませんっ！").queue();
            return;
        }

        List<String> vote;
        final String title;
        final String time;
        if (!getMessageText().toLowerCase().startsWith("n.votent")){
            if (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(3, string.length));
                title = string[2];
                time = string[1];
            } else if (string[string.length - 1].toLowerCase().startsWith("t:") || string[string.length - 1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(2, string.length - 1));
                title = string[1];
                time = string[string.length - 1];
            } else {
                int i = 0;
                vote = new ArrayList<>();
                String time2 = null;
                String title2 = "";

                for (String t : string){
                    if (i == 0) {
                        i++;
                        continue;
                    }

                    if (i == 1){
                        title2 = t;
                        i++;
                        continue;
                    }

                    if (time2 == null && t.toLowerCase().startsWith("t:") || t.toLowerCase().startsWith("time:")){
                        time2 = t;
                    } else {
                        vote.add(t);
                    }

                    i++;
                }

                time = time2;
                title = title2;
            }
        } else {
            title = "";
            if (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(2, string.length));
                time = string[1];
            } else if (string[string.length - 1].toLowerCase().startsWith("t:") || string[string.length - 1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(1, string.length - 1));
                time = string[string.length - 1];
            } else {
                int i = 0;
                vote = new ArrayList<>();
                String time2 = null;

                for (String t : string){

                    if (i == 0) {
                        i++;
                        continue;
                    }

                    if (time2 == null && t.toLowerCase().startsWith("t:") || t.toLowerCase().startsWith("time:")){
                        time2 = t;
                    } else {
                        vote.add(t);
                    }

                    i++;
                }

                time = time2;
            }
        }

        if (vote.size() == 0){
            getMessage().reply("えらーですっ！選択肢が見つかりませんっ！").queue();
            return;
        }

        if (vote.size() == 1){
            getMessage().reply("選択肢がひとつしか見つからない...").queue();
            return;
        }

        if (vote.size() > regional.length){
            getMessage().reply("えらーですっ！選択肢が多すぎます！！").queue();
            return;
        }

        getMessage().delete().queue();

        StringBuffer sb = new StringBuffer();
        if (getMessageText().toLowerCase().startsWith("n.votent")){
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---");
        } else {
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---\n投票タイトル：");
            sb.append(title);
        }

        sb.append("\n\n");


        for (int i = 0; i < vote.size(); i++){

            sb.append(regional[i]);
            sb.append(" : ");
            sb.append(vote.get(i));
            sb.append("\n");

        }
        sb.append("\n(");
        sb.append(getUser().getName());
        sb.append(" さんが投票を開始しました");

        if (time != null && getMs(time) != -1){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = new Date();
            long time1 = date.getTime() + getMs(time);

            sb.append(" ");
            sb.append(simpleDateFormat.format(time1));
            sb.append("まで投票受付中です。");

        }
        sb.append(")");
        sb.append("\n**リアクション数が1ではない場合はしばらく待ってから投票してください！**");

        getTextChannel().sendMessage(sb.toString()).queue(message -> {

            for (int i = 0; i < vote.size(); i++){
                message.addReaction(regional[i]).queue();
            }

            if (time != null){

                long ms = getMs(time);

                if (ms != -1){

                    TimerTask task = new TimerTask() {
                        public void run() {
                            StopVote(message, title);
                        }
                    };

                    Timer timer = new Timer();
                    timer.schedule(task, ms);
                }
            }

        });

    }
}
