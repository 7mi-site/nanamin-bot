package xyz.n7mn.dev.game;

import net.dv8tion.jda.api.entities.Message;

public class GameMenu extends GameInterface {
    public GameMenu(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {
        String text = "" +
                "----- ななみちゃんbot ゲームメニュー -----\n" +
                "`n.money` --- 現在の所持金をチェックする\n" +
                "`n.slot` --- 1回 100"+getMoneySystem().getCurrency()+"でスロットが遊べる (当たりで最大10倍戻り)\n" +
                "`n.yosogame <賭け金> <数字>` --- 一つの数字を予想して当てるゲーム (当たりで10倍戻り)\n" +
                "`n.fx <賭け金>` --- あがったりさがったり\n" +
                "`n.omikuji` --- おみくじ (結果によって"+getMoneySystem().getCurrency()+"がもらえます)\n" +
                "`n.rank` --- "+getMoneySystem().getCurrency()+"所持数ランキング\n" +
                "`n.nomoney` --- ななみちゃん救済 (所持金がマイナス1,000"+getMoneySystem().getCurrency()+"以下(所持金 < -1000)の方のみ使用可能です。)\n" +
                "(今後さらに実装予定です！)";
        getMessage().reply(text).queue();
    }
}
