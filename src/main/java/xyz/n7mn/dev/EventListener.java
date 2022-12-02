package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.api.ver;

import java.util.List;

public class EventListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        JDA jda = event.getJDA();
        List<Guild> guildList = jda.getGuilds();

        jda.getPresence().setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()+" 現在 "+guildList.size()+"サーバーに導入されているらしい。"));

        // コマンド
        SlashCommandData vote = Commands.slash("vote", "投票する");
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

        SlashCommandData help = Commands.slash("help", "ヘルプ");
        help.addOption(OptionType.STRING, "送信方式","DMに送りたい場合は「d」、自分だけのメッセージにしたい場合は「m」、みんなに共有する場合は「a」", true, false);

        SlashCommandData ver = Commands.slash("nanami-version", "バージョン情報");

        SlashCommandData setting = Commands.slash("nanami-setting", "ななみちゃんbot 設定画面");

        SlashCommandData game = Commands.slash("game", "ミニゲーム");
        game.addOption(OptionType.STRING, "ゲームの種類","種類についてはヘルプを見てね！", true, false);

        Guild guild = event.getGuild();
        guild.updateCommands().addCommands(vote, help, ver, setting, game).queue();
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
        super.onGuildMemberRemove(event);
    }

    @Override
    public void onGatewayPing(GatewayPingEvent event) {
        super.onGatewayPing(event);
    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        if (event instanceof ReadyEvent){
            JDA jda = event.getJDA();

            List<Guild> guildList = jda.getGuilds();
            jda.getPresence().setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()+" 現在 "+guildList.size()+"サーバーに導入されているらしい。"));

            // コマンド
            SlashCommandData vote = Commands.slash("vote", "投票する");
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

            SlashCommandData help = Commands.slash("help", "ヘルプ");
            help.addOption(OptionType.STRING, "送信方式","DMに送りたい場合は「d」、自分だけのメッセージにしたい場合は「m」、みんなに共有する場合は「a」", true, false);

            SlashCommandData ver = Commands.slash("nanami-version", "バージョン情報");

            SlashCommandData setting = Commands.slash("nanami-setting", "ななみちゃんbot 設定画面");

            SlashCommandData game = Commands.slash("game", "ミニゲーム");
            game.addOption(OptionType.STRING, "ゲームの種類","種類についてはヘルプを見てね！", true, false);

            for (Guild guild : guildList){

                guild.updateCommands().addCommands(vote, help, ver, setting, game).queue();

            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        Message message = event.getMessage();
        System.out.println(message.getType());

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){


        MessageChannelUnion channel = event.getChannel();
        Member member = event.getMember();

        if (event.getFullCommandName().equals("nanami-version")){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("ななみちゃんbot");
            builder.setDescription("" +
                    "Ver "+ver.get()+"\n" +
                    "サポート(質問・バグ報告・要望)サーバー : https://discord.gg/FnjCMzP7d4\n" +
                    "ソースコード : https://github.com/n7mn-xyz/nanamin-bot"
            );

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

            return;
        }


    }
}
