package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.game.*;
import xyz.n7mn.dev.game.OmikujiGame;

public class GameCommand extends GameCommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public GameCommand(MoneySystem moneySystem, TextChannel textChannel, Message message) {
        super(moneySystem, textChannel, message);
    }

    @Override
    public void run() {

        Game game = null;

        if (getMessageText().toLowerCase().startsWith("n.game")){
            game = new GameMenu(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().startsWith("n.money")){
            game = new GameMoney(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().equals("n.slot")){
            game = new SlotGame(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().equals("n.omikuji")){
            game = new OmikujiGame(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().startsWith("n.fx")){
            game = new FxGame(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().equals("n.rank")){
            game = new GameMoneyRank(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().equals("n.nomoney")){
            game = new KyusaiRoulette(getMessage(), getMoneySystem());
        }

        if (getMessageText().toLowerCase().startsWith("n.yosogame")){
            game = new YosoGame(getMessage(), getMoneySystem());
        }

        if (game != null){
            game.run();
        }

    }

}
