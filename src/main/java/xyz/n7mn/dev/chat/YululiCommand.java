package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.game.GoldImg;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private String[] pan_url = new String[]{
            "https://yululi.n7mn.xyz/nana/PANmaster.png",
            "https://yululi.n7mn.xyz/nana/PanMasterNETA01COMP.png",
            "https://yululi.n7mn.xyz/nana/puyCRzt9_400x400.jpg"
    };

    private String[] poti_url = new String[]{
            "https://yululi.n7mn.xyz/nana/poti/2020-12-26_22.49.08.png",
            "https://yululi.n7mn.xyz/nana/poti/2020-12-26_22.49.57.png"
    };

    private String[] kuretiki_url = new String[]{
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.23.00.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.20.32.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.20.30.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.20.18.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.19.24.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.18.37.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.18.14.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.18.10.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.17.39.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.13.39.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.13.18.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.11.51.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.10.53.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.09.28.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.08.55.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.06.53.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_14.01.00.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.59.53.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.52.16.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.51.06.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.50.11.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.42.05.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.41.15.png",
            "https://yululi.n7mn.xyz/nana/kuretiki/2020-12-27_13.39.19.png"
    };

    private List<GoldImg> goldImgList = new ArrayList<>();
    private String[] kouta_url = null;


    public YululiCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);

        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/kouta2.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-10_01.31.15.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-10_01.30.43.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-07_22.29.57.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/kouta.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-19_16.47.28.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-14_21.27.04.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-02_20.51.22.png","Kouta1212 in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-02_21.26.10.png","Kouta1212 with Sand in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-30_23.59.11.png","Kouta1212 in VillagerDefence"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-30_23.56.15.png","Kouta1212 in VillagerDefence"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-02_18.39.38.png",""));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-25_22.07.25.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-21_18.18.57.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-14_21.32.08.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-15_14.58.35.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-03-14_134452.png",""));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-03-14_13.46.16.png",""));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-15_14.58.35.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-03-19_15.09.45.png","osu__player in 逃走中"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-03-05_21.48.47.png","Tokyo_2020 in ???"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-03_18.24.41.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-02-02_21.21.57.png","Kouta1212 with dropped gold armor in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-26_19.39.30.png","甲羅を持ったKouta1212"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-25_22.05.11_2.png","パンを持ったKouta1212"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-19_16.47.20.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-05-06_18.03.36.png","TNTを持ってるKouta1212"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-01-20_20.45.27.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-05-06_18.03.36.png","Kouta1212 in RTM"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-05-06_21.17.04.png","レールを持っているKouta1212"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-05-06_21.17.18.png","レールを持っているKouta1212 on SEUS"));
        goldImgList.add(new GoldImg("https://yululi.n7mn.xyz/nana/kouta/2020-05-06_21.23.50.png","Tokyo_2020 in RTM"));

        kouta_url = new String[goldImgList.size()];
        int i = 0;
        for (GoldImg goldImg : goldImgList){
            kouta_url[i] = goldImg.getURL();
            i++;
        }
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
            return;
        }

        if (getMessageText().toLowerCase().equals("n.pan")){
            pan();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.yululi")){
            yululi();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.poti")){
            poti();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.kuretiki")){
            kuretiki();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.ys")){
            image();
        }
    }

    private void gold(){
/*
        getTextChannel().sendMessage("https://goldarmor-is.best/").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
            message1.suppressEmbeds(true).queue();
        });
*/
        shuffle(goldImgList);
        int i = new SecureRandom().nextInt(goldImgList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("こうたちゃんの金装備は神！");
        builder.setDescription(goldImgList.get(i).getContent());
        builder.setColor(Color.ORANGE);
        builder.setThumbnail("https://yululi.n7mn.xyz/nana/kouta/body.png");
        builder.setImage(goldImgList.get(i).getURL());

        getTextChannel().sendMessage(goldImgList.get(i).getURL()).embed(builder.build()).queue();

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

    private void pan(){

        List<String> list = Arrays.asList(pan_url);
        Collections.shuffle(list);

        SecureRandom secureRandom = new SecureRandom();
        int c = secureRandom.nextInt(list.size() - 1);
        getTextChannel().sendMessage(list.get(c)).queue();

    }

    private void yululi(){

        EmbedBuilder builder = new EmbedBuilder();

        String[] url = new String[]{"https://twitter.com/Yululi_ch","https://www.youtube.com/channel/UC4UktYq8vt-N0ZDgnC5U6bQ","https://www.youtube.com/channel/UCG20JIY3L5hcAFBZk3XZGTQ"};
        List<String> urlList = new ArrayList<>(Arrays.asList(url));
        shuffle(urlList);

        builder.setThumbnail("https://yululi.n7mn.xyz/nana/VjYrK6z9_400x400.jpg");
        int i = new SecureRandom().nextInt(urlList.size() - 1);
        builder.setTitle("Yululi-ゆるり-", urlList.get(i));
        builder.setDescription(urlList.get(i));
        if (urlList.get(i).startsWith("https://twitter.com")){
            builder.setColor(Color.CYAN);
        } else {
            builder.setColor(Color.RED);
        }

        getTextChannel().sendMessage(builder.build()).queue();
    }

    private void burn(){

        List<Emote> emote = getGuild().getEmotes();
        int i = new SecureRandom().nextInt(emote.size() - 1);

    }

    private void poti(){

        List<String> urlList = new ArrayList<>(Arrays.asList(poti_url));
        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("poti_336");
        builder.setThumbnail("https://yululi.n7mn.xyz/nana/poti/body.png");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }

    private void kuretiki(){

        List<String> urlList = new ArrayList<>(Arrays.asList(kuretiki_url));
        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("vd23z12");
        builder.setThumbnail("https://yululi.n7mn.xyz/nana/kuretiki/body.png");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }

    private void image(){

        List<String> urlList = new ArrayList<>();
        urlList.addAll(Arrays.asList(kouta_url));
        urlList.addAll(Arrays.asList(pan_url));
        urlList.addAll(Arrays.asList(poti_url));
        urlList.addAll(Arrays.asList(kuretiki_url));

        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Yululi Server");
        builder.setThumbnail("https://yululi.n7mn.xyz/nana/VjYrK6z9_400x400.jpg");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }
}
