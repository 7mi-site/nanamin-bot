package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.img.ImageData;
import xyz.n7mn.dev.Command.img.ImgSystem;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class YululiCommand extends Chat {
    public YululiCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder();

        if (getMessageText().toLowerCase().startsWith("n.gold") || (!getMessageText().toLowerCase().equals(".gold") && getMessageText().toLowerCase().equals("z.gold") && getMessageText().toLowerCase().endsWith(".gold"))) {
            List<ImageData> imageDataList = ImgSystem.getImageDataList("gold");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("こうたちゃんの金装備は神！");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/kouta/body.png");
            builder.setImage(imageDataList.get(i).getImageURL());
            builder.setColor(Color.YELLOW);

            getTextChannel().sendMessage(imageDataList.get(i).getImageURL()).embed(builder.build()).queue(message -> {
                message.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }

        if (getMessageText().toLowerCase().equals("n.renyoko") || getMessageText().toLowerCase().equals("n.rennyoko") || getMessageText().toLowerCase().equals("n.れにょこ") || getMessageText().toLowerCase().equals("n.レニョコ") || getMessageText().toLowerCase().equals("n.ﾚﾆｮｺ")){
            getTextChannel().sendMessage("れにょこの虫特攻はいい！").queue(message -> {
                message.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }


        if (getMessageText().toLowerCase().equals("n.sand")){
            getTextChannel().sendMessage(getGuild().getEmotesByName("Crousand",true).get(0).getAsMention()).queue(message1 -> {
                message1.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }

        if (getMessageText().toLowerCase().equals("n.sc")){
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
            return;
        }

        if (getMessageText().toLowerCase().equals("n.hentai")){
            List<String> a = new ArrayList<>();
            a.add("しおぽぽ");
            a.add("ふーぷれす");
            a.add("ゆるり");
            a.add("みやゆ");
            a.add(getUser().getName());
            shuffle(a);

            SecureRandom secureRandom = new SecureRandom();
            int c = secureRandom.nextInt(a.size() - 1);

            getTextChannel().sendMessage("変態は「"+a.get(c)+"さん」です！").queue(message1 -> {
                message1.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }

        if (getMessageText().toLowerCase().equals("n.kouta")){
            String text = "金装備の Kouta1212 ちゃん　は　神\n金装備の Kouta1212 ちゃん　は　かわいい";
            getTextChannel().sendMessage(text).queue(message1 -> {
                message1.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }

        if (getMessageText().toLowerCase().equals("n.n3m_") || getMessageText().toLowerCase().equals("n.7mi_chan")){
            getTextChannel().sendMessage(getGuild().getEmotesByName("n3m_",true).get(0).getAsMention()+"\n:fire:").queue(message1 -> {
                message1.addReaction("\uD83D\uDCAF").queue();
            });
            return;
        }

        if (getMessageText().toLowerCase().equals("n.baka")){
            List<Member> members = getTextChannel().getMembers();
            // Collections.shuffle(members);

            SecureRandom secureRandom = new SecureRandom();
            Member me = members.get(secureRandom.nextInt(members.size() - 1));

            if (me.getNickname() != null){
                getTextChannel().sendMessage(me.getNickname()+"さんが馬鹿に選ばれましたっ！").queue();
            } else {
                getTextChannel().sendMessage(me.getUser().getName()+"さんが馬鹿に選ばれましたっ！").queue();
            }
            return;
        }

        if (getMessageText().toLowerCase().equals("n.pan")){
            List<ImageData> imageDataList = ImgSystem.getImageDataList("pan");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/pan/food_francepan.png");
            builder.setImage(imageDataList.get(i).getImageURL());
            builder.setColor(Color.ORANGE);

            getTextChannel().sendMessage(builder.build()).queue();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.yululi")){
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
            return;
        }

        if (getMessageText().toLowerCase().equals("n.poti")){
            List<ImageData> imageDataList = ImgSystem.getImageDataList("poti");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("poti_336","https://ja.namemc.com/profile/poti_336.1");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/poti/body.png");
            builder.setImage(imageDataList.get(i).getImageURL());

            getTextChannel().sendMessage(builder.build()).queue();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.kuretiki")){
            List<ImageData> imageDataList = ImgSystem.getImageDataList("kuretiki");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("vd23z12","https://ja.namemc.com/profile/vd23z12.1");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/poti/body.png");
            builder.setImage(imageDataList.get(i).getImageURL());

            getTextChannel().sendMessage(builder.build()).queue();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.ys")){

            List<ImageData> imageDataList = ImgSystem.getImageDataList("ys");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("Yululi Server");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/VjYrK6z9_400x400.jpg");
            builder.setImage(imageDataList.get(i).getImageURL());

            getTextChannel().sendMessage(builder.build()).queue();

        }

        if (getMessageText().toLowerCase().equals("n.yululi2")){
            List<ImageData> imageDataList = ImgSystem.getImageDataList("yululi2");
            int i = new SecureRandom().nextInt(imageDataList.size());

            builder.setTitle("Yululi Server");
            builder.setDescription(imageDataList.get(i).getDescription());
            builder.setThumbnail("https://nana-bot.n7mn.xyz/VjYrK6z9_400x400.jpg");
            builder.setImage(imageDataList.get(i).getImageURL());

            getTextChannel().sendMessage(builder.build()).queue();
        }

    }
}
