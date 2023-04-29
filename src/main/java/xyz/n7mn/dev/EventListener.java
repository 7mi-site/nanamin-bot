package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.api.ver;
import xyz.n7mn.dev.music.MusicBot;
import xyz.n7mn.dev.music.MusicQueue;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventListener extends ListenerAdapter {

    private final MusicBot musicCommand;

    private SlashCommandData vote = Commands.slash("vote", "投票する");
    private SlashCommandData music = Commands.slash("music", "音楽/動画 再生");
    private SlashCommandData help = Commands.slash("help", "ヘルプ");
    private SlashCommandData ver_c = Commands.slash("nanami-version", "バージョン情報");
    private SlashCommandData setting = Commands.slash("nanami-setting", "ななみちゃんbot 設定画面");
    private SlashCommandData game = Commands.slash("game", "ミニゲーム");

    private List<MusicQueue> musicQueueList = new ArrayList<>();

    public EventListener() {
        musicCommand = new MusicBot(musicQueueList);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        JDA jda = event.getJDA();
        List<Guild> guildList = jda.getGuilds();

        jda.getPresence().setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()+" 現在 "+guildList.size()+"サーバーに導入されているらしい。"));

        Guild guild = event.getGuild();
        System.out.println("test1 : " + guild.getId() + " / " + guild.getName());
        guild.updateCommands().addCommands(vote, help, ver_c, music).queue();
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        JDA jda = event.getJDA();
        List<Guild> guildList = jda.getGuilds();
        jda.getPresence().setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()+" 現在 "+guildList.size()+"サーバーに導入されているらしい。"));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        if (event instanceof ReadyEvent){
            JDA jda = event.getJDA();

            List<Guild> guildList = jda.getGuilds();
            jda.getPresence().setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()+" 現在 "+guildList.size()+"サーバーに導入されているらしい。"));

            // コマンド
            vote.addOption(OptionType.STRING, "タイトル","例「なにを食べますか？」", true, false);
            vote.addOption(OptionType.STRING, "投票終了日時","例 「2022-12-31 23:59:59」", true, false);
            vote.addOption(OptionType.STRING, "選択肢1","投票の選択肢 例「きのこの山」", true, false);
            vote.addOption(OptionType.STRING, "選択肢2","投票の選択肢 例「たけのこの里」", true, false);
            vote.addOption(OptionType.STRING, "選択肢3","投票の選択肢 例「アルフォート」", false, false);
            vote.addOption(OptionType.STRING, "選択肢4","投票の選択肢 例「食べない」", false, false);
            vote.addOption(OptionType.STRING, "選択肢5","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢6","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢7","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢8","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢9","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢10","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢11","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢12","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢13","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢14","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢15","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢16","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢17","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢18","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢19","投票の選択肢", false, false);
            vote.addOption(OptionType.STRING, "選択肢20","投票の選択肢", false, false);

            help.addOption(OptionType.STRING, "送信方式","DMに送りたい場合は「d」、自分以外にも見えるようにする場合は「a」", false, false);
            music.addOption(OptionType.STRING, "url","URL", true, false);
            music.addOption(OptionType.STRING, "音量","0～100、デフォルトは20です！", false, false);

/*
            SlashCommandData setting = Commands.slash("nanami-setting", "ななみちゃんbot 設定画面");

            SlashCommandData game = Commands.slash("game", "ミニゲーム");
            game.addOption(OptionType.STRING, "ゲームの種類","種類についてはヘルプを見てね！", true, false);

 */

            for (Guild guild : guildList){

                guild.updateCommands().addCommands(vote, help, ver_c, music).queue();

            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        Message message = event.getMessage();
        MessageChannelUnion channel = event.getChannel();

        if (message.getMember() != null){
            if (message.getMember().getUser().getId().equals(jda.getSelfUser().getId())){
                return;
            }
        } else {
            if (event.getAuthor().getId().equals(jda.getSelfUser().getId())){
                return;
            }
        }

        if (channel.getType() == ChannelType.PRIVATE){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.PINK);
            builder.setTitle("ななみちゃんbot");
            builder.setDescription("" +
                    "Ver "+ver.get()+"\n" +
                    "サポート(質問・バグ報告・要望)サーバー : https://discord.gg/FnjCMzP7d4\n" +
                    "ソースコード : https://github.com/n7mn-xyz/nanamin-bot"
            );

            channel.sendMessage("なにも起きませんです。送らないでくださいです。\n").addEmbeds(builder.build()).queue();

            // ログ記録
            if (!new File("./log").exists()){
                new File("./log").mkdir();
            }

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");

            if (!new File("./log/"+sdf.format(date)).exists()){
                new File("./log/log"+sdf.format(date)).mkdir();
            }

            File file = new File("./log/log" + sdf.format(date) + "/" + event.getAuthor().getId() + "-" + message.getId() + "-private.txt");

            StringBuffer sb = new StringBuffer();
            sb.append(message.getJumpUrl()); sb.append("\r\n");
            sb.append(event.getAuthor().getAsTag()); sb.append("\r\n");
            sb.append(event.getAuthor().getId()); sb.append("\r\n");
            sb.append(message.getContentRaw()); sb.append("\r\n");
            List<Message.Attachment> list = message.getAttachments();

            if (list.size() > 0){
                sb.append("--- 添付ファイル ---\r\n");
            }
            for (Message.Attachment attachment : list){
                sb.append(attachment.getFileName());
                sb.append(" ");
                sb.append(attachment.getUrl());
                sb.append("\r\n");
            }

            try {
                PrintWriter writer = new PrintWriter(file);
                writer.print(sb);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return;
        }

        // 旧コマンド呼ばれたら誘導する
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("ななみちゃんbot 実行エラー");
        builder.setDescription("そのコマンドはもう古いですっ！\n/helpを使って新しいコマンドを探して打ってね！！");
        if (
                message.getContentRaw().startsWith("n.vote") || message.getContentRaw().startsWith("n.role") || message.getContentRaw().equals("n.ui")
                || message.getContentRaw().equals("n.nullpo") || message.getContentRaw().equals("n.dice") || message.getContentRaw().equals("n.burn") || message.getContentRaw().equals("n.burst") || message.getContentRaw().equals("n.aisatu") || message.getContentRaw().equals("n.あいさつ") || message.getContentRaw().startsWith("n.random")
                || message.getContentRaw().startsWith("n.play") || message.getContentRaw().equals("n.stop") || message.getContentRaw().equals("n.repeat") || message.getContentRaw().equals("n.nowPlay") || message.getContentRaw().toLowerCase().equals("n.nowplay") || message.getContentRaw().startsWith("n.musicVolume")
                || message.getContentRaw().startsWith("n.volume") || message.getContentRaw().equals("n.musicSkip") || message.getContentRaw().equals("n.skip")
                || message.getContentRaw().equals("n.game") || message.getContentRaw().startsWith("n.money") || message.getContentRaw().equals("n.bank") || message.getContentRaw().equals("n.slot")
                || message.getContentRaw().equals("n.rank") || message.getContentRaw().equals("n.yosogame") || message.getContentRaw().equals("n.fx") || message.getContentRaw().equals("n.omikuji") || message.getContentRaw().equals("n.nomoney") || message.getContentRaw().equals("n.nomoney2")
        ){
            message.replyEmbeds(builder.build()).queue();

            new Thread(()->{
                // ログ記録
                if (!new File("./log").exists()){
                    new File("./log").mkdir();
                }

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");

                if (!new File("./log/"+sdf.format(date)).exists()){
                    new File("./log/log"+sdf.format(date)).mkdir();
                }

                File file = new File("./log/log" + sdf.format(date) + "/" + event.getMember().getId() + "-" + event.getMessageId() + "-messageCommand.txt");

                StringBuffer sb = new StringBuffer();

                sb.append(event.getMember().getUser().getAsTag()); sb.append("\r\n");
                sb.append(event.getMember().getUser().getId()); sb.append("\r\n");
                sb.append(event.getJumpUrl()); sb.append("\r\n");
                List<Message.Attachment> list = message.getAttachments();
                if (list.size() > 0){
                    sb.append("--- 添付ファイル ---\r\n");
                }
                for (Message.Attachment attachment : list){
                    sb.append(attachment.getFileName());
                    sb.append(" ");
                    sb.append(attachment.getUrl());
                    sb.append("\r\n");
                }

                try {
                    PrintWriter writer = new PrintWriter(file);
                    writer.print(sb);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){

        MessageChannelUnion channel = event.getChannel();
        Member member = event.getMember();
        JDA jda = event.getJDA();

        new Thread(()->{
            // ログ記録
            if (!new File("./log").exists()){
                new File("./log").mkdir();
            }

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");

            if (!new File("./log/"+sdf.format(date)).exists()){
                new File("./log/log"+sdf.format(date)).mkdir();
            }

            File file = new File("./log/log" + sdf.format(date) + "/" + member.getId() + "-" + event.getId() + "-command.txt");

            StringBuffer sb = new StringBuffer();

            sb.append(member.getUser().getAsTag()); sb.append("\r\n");
            sb.append(member.getUser().getId()); sb.append("\r\n");
            sb.append(event.getFullCommandName()); sb.append("\r\n");
            for (OptionMapping option : event.getOptions()){
                sb.append(option.getName());
                sb.append(" : ");
                sb.append(option.getAsString());
                sb.append("\r\n");
            }

            try {
                PrintWriter writer = new PrintWriter(file);
                writer.print(sb);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        if (event.getFullCommandName().equals("nanami-version")){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.PINK);
            builder.setTitle("ななみちゃんbot");
            builder.setDescription("" +
                    "Ver "+ver.get()+"\n" +
                    "サポート(質問・バグ報告・要望)サーバー : https://discord.gg/FnjCMzP7d4\n" +
                    "ソースコード : https://github.com/n7mn-xyz/nanamin-bot"
            );

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

            return;
        }

        if (event.getFullCommandName().equals("help")){

            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.PINK);
            builder.setTitle("ななみちゃんbot ヘルプ");
            builder.setDescription("" +
                    "質問や要望、バグ報告は https://discord.gg/FnjCMzP7d4 までお願いします。\n" +
                    (ver.get().matches(".*dev.*") ? "**このBotはベータ版です！予告なしで停止する場合があります！！*" : "")
            );

            builder.addField("/help", "いまあなたが見ているやつですっ！！", false);
            // builder.addField("/vote", "投票機能\n\n例\n/vote 朝ごはん食べる？ 2024-12-01 00:00:00 食べる 食べない なにそれ？ ご飯を食べられない人だっているんですよ！！！ それってあなたの感想ですよね？", false);
            // builder.addField("/game", "ミニゲーム機能\n\n実装し直し中です。しばらく待っててね！", false);
            builder.addField("/music", "音楽再生機能", false);
            builder.addField("/nanami-version", "バージョン情報を出す。\nバグ報告に役にたつのでそのときはお願いしますっ", false);
            //builder.addField("/nanami-setting", "ななみちゃんbotの設定ができるコマンドです。 \n昔のnanami_settingチャンネルの代わりですっ", false);

            OptionMapping option = event.getOption("送信方式");

            if (option != null && option.getAsString().equals("d")){
                jda.getUserById(member.getId()).openPrivateChannel().queue((s->{
                    s.sendMessageEmbeds(builder.build()).queue();
                    event.reply("ヘルプをDMに送信しましたっ！").setEphemeral(true).queue();
                }));

                return;
            }

            if (option != null && option.getAsString().equals("a")){
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        }

        if (event.getFullCommandName().equals("music")){
            OptionMapping option1 = event.getOption("url");
            OptionMapping option2 = event.getOption("音量");
            musicCommand.run(event, option1, option2);
        }

    }


}
