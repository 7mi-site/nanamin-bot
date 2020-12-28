package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.PlayerManager;

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
            "https://nana-bot.n7mn.xyz/PANmaster.png",
            "https://nana-bot.n7mn.xyz/PanMasterNETA01COMP.png",
            "https://nana-bot.n7mn.xyz/puyCRzt9_400x400.jpg"
    };

    private String[] poti_url = new String[]{
            "https://nana-bot.n7mn.xyz/poti/2020-12-26_22.49.08.png",
            "https://nana-bot.n7mn.xyz/poti/2020-12-26_22.49.57.png",
            "https://nana-bot.n7mn.xyz/poti/2020-12-27_18.31.34.png",
            "https://nana-bot.n7mn.xyz/poti/2020-12-27_18.30.54.png",
            "https://nana-bot.n7mn.xyz/poti/2020-12-27_18.29.21.png",
            "https://nana-bot.n7mn.xyz/poti/2020-12-27_17.52.24.png"
    };

    private String[] kuretiki_url = new String[]{
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.23.00.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.20.32.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.20.30.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.20.18.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.19.24.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.18.37.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.18.14.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.18.10.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.17.39.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.13.39.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.13.18.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.11.51.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.10.53.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.09.28.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.08.55.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.06.53.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_14.01.00.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.59.53.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.52.16.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.51.06.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.50.11.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.42.05.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.41.15.png",
            "https://nana-bot.n7mn.xyz/kuretiki/2020-12-27_13.39.19.png"
    };

    private List<GoldImg> goldImgList = new ArrayList<>();
    private String[] kouta_url = null;

    private String[] sebunku_url = new String[]{
            "https://nana-bot.n7mn.xyz/sebunku/image0_1.png",
            "https://nana-bot.n7mn.xyz/sebunku/Desktop_Screenshot_2020.12.26_-_19.57.00.16.png",
            "https://nana-bot.n7mn.xyz/sebunku/Desktop_Screenshot_2020.04.06_-_13.09.44.32.png",
            "https://nana-bot.n7mn.xyz/sebunku/1389.png",
            "https://nana-bot.n7mn.xyz/sebunku/1387.png",
            "https://nana-bot.n7mn.xyz/sebunku/1321.png",
            "https://nana-bot.n7mn.xyz/sebunku/1013.png",
            "https://nana-bot.n7mn.xyz/sebunku/215af548349a7f50.jpg",
            "https://nana-bot.n7mn.xyz/sebunku/2.png",
            "https://nana-bot.n7mn.xyz/sebunku/1_2.png",
            "https://youtu.be/lwOnJUqBQhY"
    };

    private String[] yululi_url = new String[]{
            "https://nana-bot.n7mn.xyz/kouta/Desktop_Screenshot_2020.12.27_-_16.31.18.84.png",
            "https://nana-bot.n7mn.xyz/yululi/freeeeee.png",
            "https://nana-bot.n7mn.xyz/yululi/e451eabd993592bffd6ffe78ebe669af.png",
            "https://nana-bot.n7mn.xyz/yululi/98db7496472626488b2ab74ad8c43117.png",
            "https://nana-bot.n7mn.xyz/yululi/9ba6a53c0f688eafda0b7f927fa691d5.png",
            "https://nana-bot.n7mn.xyz/yululi/9.png",
            "https://nana-bot.n7mn.xyz/yululi/2_2.png"
    };


    public YululiCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);

        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/kouta2.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-10_01.31.15.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-10_01.30.43.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-07_22.29.57.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/kouta.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-19_16.47.28.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-14_21.27.04.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-02_20.51.22.png","Kouta1212 in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-02_21.26.10.png","Kouta1212 with Sand in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-30_23.59.11.png","Kouta1212 in VillagerDefence"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-30_23.56.15.png","Kouta1212 in VillagerDefence"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-02_18.39.38.png",""));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-25_22.07.25.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-21_18.18.57.png","Kouta1212 in サバイバルサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-14_21.32.08.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-15_14.58.35.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-03-14_134452.png",""));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-03-14_13.46.16.png",""));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-15_14.58.35.png","Kouta1212 in CoreBreakingPVP"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-03-19_15.09.45.png","osu__player in 逃走中"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-03-05_21.48.47.png","Tokyo_2020 in ???"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-03_18.24.41.png","Kouta1212 in Rush"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-02-02_21.21.57.png","Kouta1212 with dropped gold armor in Mapperサーバー"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-26_19.39.30.png","甲羅を持ったKouta1212"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-25_22.05.11_2.png","パンを持ったKouta1212"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-19_16.47.20.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-05-06_18.03.36.png","TNTを持ってるKouta1212"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-01-20_20.45.27.png","Kouta1212 in KartRace"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-05-06_18.03.36.png","Kouta1212 in RTM"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-05-06_21.17.04.png","レールを持っているKouta1212"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-05-06_21.17.18.png","レールを持っているKouta1212 on SEUS"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/2020-05-06_21.23.50.png","Tokyo_2020 in RTM"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/Desktop_Screenshot_2020.12.27_-_16.31.18.84.png", "yululi in CastleStage"));
        goldImgList.add(new GoldImg("https://nana-bot.n7mn.xyz/kouta/8.png", ""));

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
            return;
        }

        if (getMessageText().toLowerCase().equals("n.sebunku") || getMessageText().toLowerCase().equals("n.playsebunku")){
            sebunku();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.yululi2")){
            yululi2();
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
        builder.setThumbnail("https://nana-bot.n7mn.xyz/kouta/body.png");
        builder.setImage(goldImgList.get(i).getURL());

        getTextChannel().sendMessage(goldImgList.get(i).getURL()).embed(builder.build()).queue(message -> {
            message.addReaction("\uD83D\uDCAF").queue();
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

        builder.setThumbnail("https://nana-bot.n7mn.xyz/VjYrK6z9_400x400.jpg");
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

    private void sebunku(){
        if (getMessageText().toLowerCase().startsWith("n.sebunku")){

            List<String> urlList = new ArrayList<>(Arrays.asList(sebunku_url));
            shuffle(urlList);
            int i = new SecureRandom().nextInt(urlList.size() - 1);

            EmbedBuilder builder = new EmbedBuilder();
            if (!urlList.get(i).startsWith("https://youtu.be")){
                builder.setTitle("sebunku");
                builder.setThumbnail("https://nana-bot.n7mn.xyz/sebunku/body.png");
                builder.setImage(urlList.get(i));
                builder.setColor(Color.PINK);
                getTextChannel().sendMessage(builder.build()).queue();
                return;
            }

            getTextChannel().sendMessage(urlList.get(i)).queue();

            return;
        }

        if (getMessageText().toLowerCase().startsWith("n.playsebunku")){

            AudioManager audioManager = getGuild().getAudioManager();
            if (!audioManager.isConnected()){
                List<VoiceChannel> voiceChannels = getGuild().getVoiceChannels();
                // voiceChannel = voiceChannels.get(0);

                VoiceChannel voiceChannel = null;
                for (VoiceChannel vc : voiceChannels){
                    try {
                        // System.out.println(vc.getName() + " : " + vc.getMembers().size());

                        if (vc.getMembers().size() != 0){
                            List<Member> members = vc.getMembers();

                            for (Member member : members){

                                if (getMember().getId().equals(member.getId())){
                                    voiceChannel = vc;
                                }

                            }

                        }
                    } catch (Exception e){
                        // e.printStackTrace();
                    }

                }

                if (voiceChannel != null){
                    audioManager.openAudioConnection(voiceChannel);
                }
            }

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.loadAndPlay(getTextChannel(), "https://www.youtube.com/watch?v=lwOnJUqBQhY");

        }
    }

    private void poti(){

        List<String> urlList = new ArrayList<>(Arrays.asList(poti_url));
        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("poti_336");
        builder.setThumbnail("https://nana-bot.n7mn.xyz/poti/body.png");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }

    private void kuretiki(){

        List<String> urlList = new ArrayList<>(Arrays.asList(kuretiki_url));
        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("vd23z12");
        builder.setThumbnail("https://nana-bot.n7mn.xyz/kuretiki/body.png");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }

    private void image(){

        List<String> urlList = new ArrayList<>();
        urlList.addAll(Arrays.asList(kouta_url));
        urlList.addAll(Arrays.asList(pan_url));
        urlList.addAll(Arrays.asList(poti_url));
        urlList.addAll(Arrays.asList(kuretiki_url));
        urlList.addAll(Arrays.asList(sebunku_url));
        urlList.addAll(Arrays.asList(yululi_url));

        String[] add = new String[]{
                "https://nana-bot.n7mn.xyz/y/Desktop_Screenshot_2020.12.26_-_19.57.00.16.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.20.28.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.20.12.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.08.50.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.04.35.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.03.05.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.01.47.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.00.10.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_18.00.02.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.59.52.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.59.35.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.58.44.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.58.40.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.58.06.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.56.18.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.54.49_2.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.52.24.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.51.55.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.51.29.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.51.19.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.51.05.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.50.56.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.50.43.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.50.40.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.50.15.png",
                "https://nana-bot.n7mn.xyz/y/2020-12-27_17.48.18.png",
                "https://nana-bot.n7mn.xyz/y/1155.png",
                "https://nana-bot.n7mn.xyz/y/1043.png",
                "https://nana-bot.n7mn.xyz/y/1027.png",
                "https://nana-bot.n7mn.xyz/y/1013.png",
                "https://nana-bot.n7mn.xyz/y/221.png"
        };

        urlList.addAll(Arrays.asList(add));

        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Yululi Server");
        builder.setThumbnail("https://nana-bot.n7mn.xyz/VjYrK6z9_400x400.jpg");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();

    }

    private void yululi2(){
        List<String> urlList = new ArrayList<>(Arrays.asList(yululi_url));
        shuffle(urlList);
        int i = new SecureRandom().nextInt(urlList.size() - 1);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("yululi");
        builder.setThumbnail("https://nana-bot.n7mn.xyz/yululi/body.png");
        builder.setImage(urlList.get(i));

        getTextChannel().sendMessage(builder.build()).queue();
    }
}
