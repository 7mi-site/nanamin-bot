package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SystemCheckCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public SystemCheckCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        getMessage().reply("---- ななみちゃんbot実行テスト結果 ----\n" + "応答 : OK").queue(message -> {

            StringBuffer sb = new StringBuffer(message.getContentRaw()+"\n");
            TextChannel jisinChannel = null;

            boolean chCheck = false;
            try {
                List<TextChannel> textChannels = getGuild().getTextChannels();


                for (TextChannel channel : textChannels) {
                    if (channel.getName().equals("nanami_setting")) {
                        jisinChannel = channel;
                        chCheck = true;
                        break;
                    }
                }


            } catch (Exception e){
                sb.append("(テキストチャンネルの一覧取得に失敗しました。一部機能の実行に影響が出る可能性があります。)");
                chCheck = false;
            }

            if (chCheck){
                jisinChannel.getHistoryAfter(1, 100).queue((messageHistory -> {
                    List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                    for (Message message1 : retrievedHistory){

                        String contentRaw = message1.getContentRaw();
                        if (contentRaw.startsWith("jisin: ")){

                            String st = contentRaw.replaceAll("jisin: ", "");
                            TextChannel textChannelById = message1.getGuild().getTextChannelById(st);
                            if (textChannelById != null){

                                try {
                                    textChannelById.sendMessage("test").queue((message2 -> {
                                        message2.delete().queue();
                                        sb.append("\n地震情報 : OK");
                                    }));

                                } catch (Exception e){
                                    sb.append("\n地震情報 : NG (チャンネルへの送信権限(、削除権限)がないようです？)");
                                }

                            } else {
                                sb.append("\n地震情報 : NG (指定チャンネルが存在しません。)");
                            }

                        }

                    }
                }));
            }
            if (jisinChannel == null) {
                sb.append("\n地震情報 : NG(チャンネルの存在が確認できません。)");
            }

            message.editMessage(sb.toString()).queue();
        });
    }
}
