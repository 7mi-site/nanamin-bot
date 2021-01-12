package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.i.System;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommandSystem {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void run(TextChannel textChannel, Message message){

        System system = null;
        String messageText = message.getContentRaw();

        if (messageText.startsWith("https://discord.com")){
            system = new URLChecker(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.help")){
            system = new Help(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.ping")){
            system = new Ping(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.vote") && !messageText.toLowerCase().startsWith("n.votestop") && !messageText.toLowerCase().startsWith("n.votecheck")){
            system = new Vote(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.votestop")){
            system = new VoteStop(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.ぬるぽ") || messageText.toLowerCase().equals("n.nullpo")){
            system = new Nullpo(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.dice")){
            system = new Dice(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.random")){
            system = new Random(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.burn")){
            system = new Burn(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.burst")){
            system = new Burst(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.role") || messageText.toLowerCase().equals("n.ui")){
            system = new Role(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.music")){
            system = new Music(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.play")){
            system = new MusicPlay(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.stop")){
            system = new MusicStop(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.nowplay")){
            system = new MusicNowPlay(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.musicvolume") || messageText.toLowerCase().startsWith("n.volume")){
            system = new MusicVolume(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.game")){
            system = new Game(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.money")){
            system = new MoneyCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.rank")){
            system = new GameRank(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.slot")){
            system = new GameSlot(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.yosogame")){
            system = new GameYoso(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.fx")){
            system = new GameFx(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.omikuji")){
            system = new GameOmikuji(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.nomoney")){
            system = new GameNoMoney(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.gold") || (!messageText.toLowerCase().equals(".gold") && messageText.toLowerCase().equals("z.gold") && messageText.toLowerCase().endsWith(".gold"))) {
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.renyoko") || messageText.toLowerCase().equals("n.rennyoko") || messageText.toLowerCase().equals("n.れにょこ") || messageText.toLowerCase().equals("n.レニョコ") || messageText.toLowerCase().equals("n.ﾚﾆｮｺ")){
            system = new YululiCommand(textChannel, message);
        }


        if (messageText.toLowerCase().equals("n.sand")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.sc")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.hentai")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.kouta")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.n3m_") || messageText.toLowerCase().equals("n.7mi_chan")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.baka")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.pan")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.yululi")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.poti")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.kuretiki")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.ys")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.yululi2")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.sebunku")){
            system = new SebunkuCommand(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.playsebunku")){
            system = new SebunkuCommand(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.send")){
            system = new DMSend(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.msg")){
            system = new Msg(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.check")){
            system = new CheckCommand(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.votecheck")){
            system = new VoteCheck(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.nomoney2")){
            system = new GameNomoney2(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.fuck")){
            system = new YululiCommand(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.ban")){
            system = new Ban(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.oha") || messageText.toLowerCase().startsWith("n.oya") || messageText.toLowerCase().startsWith("n.おは") || messageText.toLowerCase().startsWith("n.おや") || messageText.toLowerCase().equals("n.aisatu") || messageText.toLowerCase().equals("n.あいさつ")){
            system = new Aisatu(textChannel, message);
        }

        if (messageText.toLowerCase().equals("n.lost") || messageText.toLowerCase().equals("n.hoopless") || messageText.toLowerCase().equals("n.acrylicstyle") || messageText.toLowerCase().equals("n.acrylic_style")) {
            system = new YululiCommand(textChannel, message);
        }

        Matcher matcher = Pattern.compile("(.*)あけおめ(.*)").matcher(messageText);
        Matcher matcher2 = Pattern.compile("(.*)誕生日(.*)").matcher(messageText);
        Matcher matcher3 = Pattern.compile("(.*)Happy(.*)New(.*)Year(.*)").matcher(messageText);
        Matcher matcher4 = Pattern.compile("(.*)あけましておめでとう(.*)").matcher(messageText);

        if (matcher.find() || matcher2.find() || matcher3.find() || matcher4.find()){
            system = new Akeome(textChannel, message);
        }

        if (messageText.toLowerCase().startsWith("n.") || system != null){
            User user = textChannel.getJDA().getUserById("529463370089234466");
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("実行ログ").setFooter(sdf.format(new Date())).setColor(Color.PINK);

            builder.addField("サーバー名", textChannel.getGuild().getName()+"\n("+textChannel.getGuild().getId()+")", false);
            builder.addField("テキストチャンネル名", textChannel.getName()+"\n("+textChannel.getId()+")", false);
            builder.addField("実行者", message.getAuthor().getAsTag()+"\n("+message.getAuthor().getId()+")", false);
            builder.addField("内容", messageText, false);
            builder.addField("メッセージリンクURL", message.getJumpUrl(), false);

            user.openPrivateChannel().complete().sendMessage(builder.build()).queue();
        }

        if (system != null){
            system.run();
        }
    }

}
