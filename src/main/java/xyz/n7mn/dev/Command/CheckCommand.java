package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.NanamiFunction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.util.List;

public class CheckCommand extends Chat {
    public CheckCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder();
        long startTime = System.currentTimeMillis();
        getMessage().reply("応答したよっ").queue(message -> {

            TextChannel jisinChannel = null;
            builder.setTitle("ななみちゃんbot Ver"+ NanamiFunction.getVersion() +"実行テスト結果");
            builder.addField("応答", "OK", false);

            boolean chCheck = false;
            try {
                List<TextChannel> textChannels = getGuild().getTextChannels();

                for (TextChannel channel : textChannels) {
                    if (channel.getName().equals("nanami_setting")) {
                        builder.addField("テキストチャンネルの一覧取得","成功", false);
                        jisinChannel = channel;
                        chCheck = true;
                        break;
                    }
                }
            } catch (Exception e){
                builder.addField("テキストチャンネルの一覧取得","失敗", false);
                chCheck = false;
            }

            if (chCheck){
                builder.addField("ななみちゃん設定チャンネル","あり", false);
                jisinChannel.getHistoryAfter(1, 100).queue(messageHistory -> {
                    List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                    for (Message message1 : retrievedHistory){
                        String[] st = message1.getContentRaw().split(" ", -1);
                        // System.out.println(st[0].toLowerCase().startsWith("jisin"));
                        if (st[0].toLowerCase().startsWith("jisin")){
                            builder.addField("地震情報出力設定","あり", false);
                            TextChannel textChannelById = getGuild().getTextChannelById(st[1]);
                            if (textChannelById != null){
                                builder.addField("地震情報出力チャンネル","あり", false);
                                try {
                                    if (textChannelById.canTalk()){
                                        builder.addField("出力","できる", false);
                                    } else {
                                        builder.addField("出力","できない", false);
                                    }
                                    break;
                                } catch (Exception e){
                                    builder.addField("出力","できない", false);
                                }
                            } else {
                                builder.addField("地震情報出力チャンネル","なし", false);
                            }
                            break;
                        }
                    }
                    builder.setFooter("テストに "+(System.currentTimeMillis() - startTime)+"msかかったらしいですっ....？");
                    message.editMessageEmbeds(builder.build()).queue();
                });
            } else {
                builder.addField("ななみちゃん設定チャンネル","なし", false);
                builder.setFooter("テストに "+(System.currentTimeMillis() - startTime)+"msかかったらしいですっ....？");
                message.editMessageEmbeds(builder.build()).queue();
            }
        });
    }
}
