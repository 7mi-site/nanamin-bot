package xyz.n7mn.dev;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;
import xyz.n7mn.dev.data.VoteReaction;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.Money;
import xyz.n7mn.dev.game.MoneySystem;

import java.util.*;
import java.util.List;

class EventListener extends ListenerAdapter {

    private VoteReactionList voteReactionList = null;
    private final MoneySystem moneySystem;

    EventListener(){

        moneySystem = new MoneySystem();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isWebhookMessage() && !event.getAuthor().isBot()){
            Money money = moneySystem.getMoney(event.getAuthor().getId());

            if (money.getMoney() < Integer.MAX_VALUE){
                int money1 = money.getMoney() + 1;
                moneySystem.setMoney(event.getAuthor().getId(), money1);
            }
        }

        if (event.getChannel().getType() == ChannelType.PRIVATE){
            new DMMessage(event.getMessage(), event.getAuthor()).run();
            return;
        }

        new ChatMessage(event.getAuthor(), event.getMessage(), voteReactionList, moneySystem).run();

    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        // super.onGuildMessageReactionAdd(event);
        if (event.getUser().isBot()){
            return;
        }

        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        String messageId = event.getMessageId();
        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        Message message = channel.retrieveMessageById(messageId).complete();

        if (!reactionEmote.isEmoji() && message.getContentRaw().startsWith("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---")){
            message.removeReaction(reactionEmote.getEmote(), event.getUser()).queue();
            return;
        }

        if (message.getContentRaw().startsWith("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---")){
            message.removeReaction(reactionEmote.getEmoji(), event.getUser()).queue();

        }

        VoteReaction voteReaction = new VoteReaction(guild, channel, member, messageId, reactionEmote.getEmoji());
        if (message.getContentRaw().startsWith("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---")){

            boolean add_flag = false;

            String[] regionalList = new String[]{
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

            for (String regional : regionalList){
                if (regional.equals(voteReaction.getEmoji())){
                    add_flag = true;
                    break;
                }
            }

            boolean f = false;
            for (VoteReaction voteRe  : voteReactionList.getList()){
                if (voteRe.getMessageId().equals(messageId) && voteRe.getMember().getId().equals(member.getId()) && voteRe.getEmoji().equals(reactionEmote.getEmoji())){
                    f = true;
                    break;
                }
            }

            if (add_flag){
                if (!f){
                    voteReactionList.addList(voteReaction);
                }

                PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
                if (!f){
                    privateChannel.sendMessage(reactionEmote.getAsReactionCode() + "に投票しました！").queue();
                } else {
                    privateChannel.sendMessage("投票済みの選択肢です！！").queue();
                }
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {

        JDA jda = event.getJDA();
        List<Guild> guilds = jda.getGuilds();
        System.out.println("現在 " + guilds.size() + "サーバーで動いてるらしい。");
        int i = 0;
        for (Guild guild : guilds){

            System.out.println(guild.getId() + " : " + guild.getName());

        }

        voteReactionList = new VoteReactionList(jda);
        Earthquake earthquake = new Earthquake();

        long[] lastId = new long[]{-1};

        Timer timer = new Timer();

        final String[] sokuhoText = {""};

        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    EarthquakeResult data1 = earthquake.getData();
                    for (Guild guild : jda.getGuilds()){

                        List<TextChannel> textChannels = guild.getTextChannels();
                        for (TextChannel ch : textChannels){

                            if (ch.getName().equals("nanami_setting")){

                                MessageHistory.MessageRetrieveAction after = ch.getHistoryAfter(1, 100);
                                after.queue((messageHistory -> {

                                    List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                                    for (Message message : retrievedHistory){
                                        if (message.getContentRaw().toLowerCase().startsWith("jisin: ")){

                                            String[] split = message.getContentRaw().split(" ");

                                            try {
                                                TextChannel channel = guild.getTextChannelById(split[1].replaceAll("<","").replaceAll(">",""));
                                                if (channel != null){
                                                    if (earthquake.getLastEventID() != -1){

                                                        System.out.println("Debug : 地震情報取得");
                                                        EarthquakeResult data = earthquake.getData();
                                                        if (new Date().getTime() - data.getHead().getReportDateTime().getTime() > 300000){
                                                            System.out.println("Debug : 時間差 " + (new Date().getTime() - data.getHead().getReportDateTime().getTime()));
                                                            continue;
                                                        }

                                                        if (data.getHead().getEventID() == lastId[0] && (sokuhoText[0].length() != 0 && data.getHead().getInfoKind().equals(sokuhoText[0]))){
                                                            System.out.println("Debug : 前と同じ");
                                                            continue;
                                                        }

                                                        System.out.println("地震情報を送信しました : " + channel.getName());
                                                        StringBuffer sb = new StringBuffer();
                                                        if (earthquake.getData().getHead().getInfoKind().equals("地震情報")){
                                                            sb.append("------ 地震情報 (ここから) ------\n");
                                                            sb.append(data.getHead().getHeadline());
                                                            sb.append("\n");
                                                            sb.append("震源地は");
                                                            sb.append(data.getBody().getEarthquake().getHypocenter().getName());
                                                            sb.append("(");
                                                            sb.append(data.getBody().getEarthquake().getHypocenter().getLongitude());
                                                            sb.append(",");
                                                            sb.append(data.getBody().getEarthquake().getHypocenter().getLatitude());
                                                            sb.append(")\n");
                                                            sb.append("マグニチュードは M ");
                                                            sb.append(data.getBody().getEarthquake().getMagnitude());
                                                            sb.append("と推定されています。\n");
                                                            sb.append(data.getBody().getComments().getObservation());
                                                            sb.append("\n");
                                                            sb.append("最大震度は ");
                                                            sb.append(data.getBody().getIntensity().getObservation().getMaxInt());
                                                            sb.append(" です。\n");

                                                            sb.append("---- 各地の震度 ---- \n");
                                                            Pref[] prefList = data.getBody().getIntensity().getObservation().getPref();
                                                            for (Pref perf : prefList){
                                                                Area[] areaList = perf.getArea();

                                                                for (Area area : areaList){

                                                                    sb.append(area.getName());
                                                                    sb.append(" 震度 ");
                                                                    sb.append(area.getMaxInt());
                                                                    sb.append("\n");

                                                                    if (sb.length() >= 1900){

                                                                        channel.sendMessage(sb.toString()).queue();
                                                                        sb.delete(0, sb.length());

                                                                    }

                                                                }

                                                            }

                                                            sb.append("------ 地震情報 (ここまで) ------");
                                                            channel.sendMessage(sb.toString()).queue();
                                                            System.out.println("Debug : send");
                                                        } else if (earthquake.getData().getHead().getInfoKind().equals("震度速報")) {
                                                            sb.append("**------ 地震速報 (ここから) ------**\n");
                                                            sb.append(data.getHead().getHeadline());
                                                            sb.append("\n");
                                                            Pref[] prefList = data.getBody().getIntensity().getObservation().getPref();
                                                            for (Pref perf : prefList){
                                                                Area[] areaList = perf.getArea();

                                                                for (Area area : areaList){

                                                                    sb.append(area.getName());
                                                                    sb.append(" 震度 ");
                                                                    sb.append(area.getMaxInt());
                                                                    sb.append("\n");

                                                                }

                                                            }

                                                            sb.append(data.getBody().getComments().getObservation());
                                                            sb.append("\n");
                                                            sb.append("**------ 地震速報 (ここまで) ------**\n");
                                                            channel.sendMessage(sb.toString()).queue();
                                                            System.out.println("Debug : send2");
                                                        }



                                                    }

                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                }));
                            }
                        }
                    }
                    if (data1 != null){
                        lastId[0] = data1.getHead().getEventID();
                        sokuhoText[0] = data1.getHead().getInfoKind();
                    }
                } catch (Exception e){

                    RestAction<User> nanami = jda.retrieveUserById("529463370089234466");
                    PrivateChannel dm = nanami.complete().openPrivateChannel().complete();
                    String debug = "----- Error ----- \n" + e.fillInStackTrace().toString();
                    dm.sendMessage(debug).queue();

                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, (1000 * 60));
    }

}
