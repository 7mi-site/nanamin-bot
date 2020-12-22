package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class YululiCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public YululiCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        if (getMessageText().toLowerCase().startsWith("n.gold") || (!getMessageText().toLowerCase().equals(".gold") && getMessageText().toLowerCase().equals("z.gold") && getMessageText().toLowerCase().endsWith(".gold"))){
            gold();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.renyoko") || getMessageText().toLowerCase().equals("n.rennyoko") || getMessageText().toLowerCase().equals("n.れにょこ") || getMessageText().toLowerCase().equals("n.レニョコ") || getMessageText().toLowerCase().equals("n.ﾚﾆｮｺ")){
            renyoko();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.sand")){
            sand();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.sc")){
            superChat();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.hentai")){
            hentai();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.kouta")){
            kouta();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.n3m_")){
            n3m();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.baka")){
            baka();
        }
    }

    private void gold(){

        getTextChannel().sendMessage("https://goldarmor-is.best/").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
            message1.suppressEmbeds(true).queue();
        });

    }

    private void renyoko(){

        getTextChannel().sendMessage("れにょこの虫特攻はいい！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void sand(){

        getTextChannel().sendMessage(getGuild().getEmotesByName("Crousand",true).get(0).getAsMention()).queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void superChat(){

        int f = (new SecureRandom().nextInt(15) & 5) + 1;
        int count = (new SecureRandom().nextInt(16) & 4) + 1;


        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(f);

        for (int i = 0; i < count; i++){
            stringBuffer.append("0");
        }

        getMessage().reply("ゆるりさんに "+stringBuffer.toString() + "円スパチャしました！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void hentai(){

        List<String> a = new ArrayList<>();
        a.add("しおぽぽ");
        a.add("ふーぷれす");
        a.add("ゆるり");
        a.add("みやゆ");
        a.add(getUser().getName());
        shuffle(a);

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new SecureRandom().nextInt(Integer.MAX_VALUE));
        int c = secureRandom.nextInt(a.size() - 1);

        getTextChannel().sendMessage("変態は「"+a.get(c)+"さん」です！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void kouta(){

        String text = "金装備の Kouta1212 ちゃん　は　神\n金装備の Kouta1212 ちゃん　は　かわいい";
        getTextChannel().sendMessage(text).queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void n3m(){

        getTextChannel().sendMessage(getGuild().getEmotesByName("n3m_",true).get(0).getAsMention()+"\n:fire:").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void baka(){

        List<Member> members = getGuild().getMembers();
        // Collections.shuffle(members);

        SecureRandom secureRandom = new SecureRandom();
        Member me = members.get(secureRandom.nextInt(members.size() - 1));

        if (me.getNickname() != null){
            getTextChannel().sendMessage(me.getNickname()+"さんが馬鹿に選ばれましたっ！").queue();
        } else {
            getTextChannel().sendMessage(me.getUser().getName()+"さんが馬鹿に選ばれましたっ！").queue();
        }
    }
}
