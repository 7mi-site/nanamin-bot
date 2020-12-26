package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import xyz.n7mn.dev.chat.*;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

public class ChatMessage {

    private final JDA jda;
    private final Guild guild;

    private final User author;
    private final Message message;
    private final String text;

    private final VoteReactionList voteReactionList;
    private final MoneySystem moneySystem;

    public ChatMessage(User author, Message message, VoteReactionList voteReactionList, MoneySystem moneySystem){

        this.author = author;
        this.message = message;
        this.text = message.getContentRaw();

        this.jda = message.getJDA();
        this.guild = message.getGuild();

        this.voteReactionList = voteReactionList;
        this.moneySystem = moneySystem;

    }

    public void run(){
        // コマンドの最初の受ける地点。新コマンドはメソッドを増やしてここに追記する
        if (text.startsWith("n.") || text.endsWith(".gold")){
            System.out.println("---- Debug ----\n" + author.getAsTag() + "\n" + text + "\n----- Debug -----");
            try {
                RestAction<User> nanami = jda.retrieveUserById("529463370089234466");
                PrivateChannel dm = nanami.complete().openPrivateChannel().complete();

                String debug = "----- Debug ----- \n" +
                        "サーバ名: " + guild.getName() +"\n" +
                        "発言チャンネル名: " + message.getTextChannel().getName() + "\n" +
                        "発言者: " + author.getAsTag() + "\n" +
                        "発言内容：`"+text+"`";
                dm.sendMessage(debug).queue();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        Chat command = null;
        if (author.isBot()){
            return;
        }

        if (!text.startsWith("n.") && !text.endsWith(".gold") && (guild.getId().equals("517669763556704258") || guild.getId().equals("781148066761670666"))){
            command = new YululiVideoChatCommand(message.getTextChannel(), message);
            command.run();
            return;
        }

        if (text.equals("n.ping")){
            command = new PingCommand(message.getTextChannel(), message);
        }
        if (text.equals("n.help")){
            command = new HelpCommand(message.getTextChannel(), message);
        }
        if (text.toLowerCase().startsWith("n.vote") && !text.toLowerCase().startsWith("n.votestop")){
            command = new VoteCommand(voteReactionList, message.getTextChannel(), message);
        }
        if (text.startsWith("n.nullpo") || text.startsWith("n.ぬるぽ")){
            command = new NullpoCommand(message.getTextChannel(), message);
        }
        if (text.toLowerCase().startsWith("n.dice")){
            command = new DiceCommand(message.getTextChannel(), message);
        }
        if (text.toLowerCase().startsWith("n.random")){
            command = new RandomCommand(message.getTextChannel(), message);
        }
        if (text.toLowerCase().startsWith("n.play") || text.toLowerCase().startsWith("n.stop")){
            command = new MusicCommand(message.getTextChannel(), message);
        }
        if (text.equals("n.check")){
            command = new SystemCheckCommand(message.getTextChannel(), message);
        }
        if (text.startsWith("n.send")){
            command = new DMSendCommand(message.getTextChannel(), message);
        }
        if (text.equals("n.burn")){
            command = new BurnCommand(message.getTextChannel(), message);
        }

        if (text.equals("n.burst")){
            command = new BurstCommand(message.getTextChannel(), message);
        }

        if (text.startsWith("n.msg")) {
            command = new GetMessageCommand(message.getTextChannel(), message);
        }

        if (text.toLowerCase().startsWith("n.votestop")){
            command = new VoteStopCommand(voteReactionList, message.getTextChannel(), message);
        }

        if (text.toLowerCase().startsWith("n.role")){
            command = new RoleCommand(message.getTextChannel(), message);
        }

        if (text.equals("n.gold") || text.equals("n.れにょこ") || text.equals("n.rennyoko") || text.equals("n.renyoko") || text.equals("n.ﾚﾆｮｺ") || text.equals("n.レニョコ") || text.equals("n.sand") || text.equals("n.sc") || text.equals("n.hentai") ||
                (text.endsWith(".gold") && !text.equals(".gold") && !text.equals("z.gold") && guild.getId().equals("517669763556704258")) || text.equals("n.pan") || text.startsWith("n.kouta") || text.toLowerCase().startsWith("n.n3m_") || text.toLowerCase().startsWith("n.7mi_chan") || text.toLowerCase().equals("n.baka")){
            command = new YululiCommand(message.getTextChannel(), message);
        }


        if (text.toLowerCase().equals("n.game") || text.toLowerCase().startsWith("n.money") || text.toLowerCase().equals("n.slot") || text.toLowerCase().equals("n.omikuji") || text.toLowerCase().startsWith("n.fx") || text.toLowerCase().startsWith("n.rank") || text.toLowerCase().startsWith("n.yosogame") || text.toLowerCase().equals("n.nomoney")){
            command = new GameCommand(moneySystem, message.getTextChannel(), message);
        }

        if (text.toLowerCase().startsWith("n.yuvideo")){
            command = new YululiVideoCommand(message.getTextChannel(), message);
        }

        if (text.toLowerCase().startsWith("n.musicvolume")){
            command = new MusicVolCommand(message.getTextChannel(), message);
        }

        if (text.toLowerCase().equals("n.nowplay")){
            command = new PlayMusicCommand(message.getTextChannel(), message);
        }

        if (command != null){
            command.run();
        }

    }


}
