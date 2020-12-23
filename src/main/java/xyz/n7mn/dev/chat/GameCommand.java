package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.game.*;

import java.util.ArrayList;
import java.util.List;

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

        if (getMessageText().toLowerCase().startsWith("n.game")){
            menu();
            return;
        }

        if (getMessageText().toLowerCase().startsWith("n.money")){
            money();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.slot")){
            slot();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.omikuji") && !getGuild().getId().equals("517669763556704258")){
            omikuji();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.omikuji") && getGuild().getId().equals("517669763556704258")){
            omikuji2();
            return;
        }

        if (getMessageText().toLowerCase().startsWith("n.fx")){
            fx();
            return;
        }

        if (getMessageText().toLowerCase().equals("n.rank")){
            moneyRank();
        }

        if (getMessageText().toLowerCase().equals("n.nomoney")){
            kyusai();
        }
    }

    private void menu(){
        String text = "" +
                "----- ななみちゃんbot ゲームメニュー -----\n" +
                "`n.money` --- 現在の所持金をチェックする\n" +
                "`n.slot` --- 1回 100"+getMoneySystem().getCurrency()+"でスロットが遊べる (当たりで最大10倍戻り)\n" +
                "~~`n.yosogame <賭け金> <数字>` --- 一つの数字を予想して当てるゲーム (当たりで10倍戻り)~~ 開発中！\n" +
                "`n.fx <賭け金>` --- あがったりさがったり\n" +
                "`n.omikuji` --- おみくじ (結果によって"+getMoneySystem().getCurrency()+"がもらえます)\n" +
                "`n.rank` --- "+getMoneySystem().getCurrency()+"所持数ランキング\n" +
                "`n.nomoney` --- ななみちゃん救済 (所持金がマイナス1,000"+getMoneySystem().getCurrency()+"以下(所持金 < -1000)の方のみ使用可能です。)\n" +
                "(今後さらに実装予定です！)";
        getMessage().reply(text).queue();

    }

    private void money(){

        String t = getMessageText().replaceAll("　"," ");
        String[] split = t.split(" ", -1);

        if (split.length == 1){
            Money money = getMoneySystem().getMoney(getUser().getId());
            getMessage().reply("あなたが今持っている所持金は " + money.getMoney() + " " + getMoneySystem().getCurrency() + "ですっ！\n他の人に渡したいときは`n.money pay <相手の名前 or 相手のID> <金額>`でできますっ！").queue();
        } else if (split.length == 4){
            if (split[1].equals("pay")){
                String memberStr = split[2];
                Member targetMember = null;
                try {
                    targetMember = getGuild().getMemberById(memberStr);
                } catch (Exception e){
                    List<Member> members = getGuild().getMembers();

                    for (Member member1 : members){
                        if (member1.getNickname() != null && member1.getNickname().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                        if (member1.getUser().getName().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                    }
                }

                if (getMoneySystem().getMoney(getUser().getId()).getMoney() - Integer.parseInt(split[3]) <= 0){
                    getMessage().reply("所持金が足りませんよ！！").queue();
                    return;
                }

                if (Integer.parseInt(split[3]) <= 0){
                    getMessage().reply("よくない。").queue();
                    return;
                }

                if (targetMember != null){
                    Money fromMoney = getMoneySystem().getMoney(getUser().getId());
                    Money targetMoney = getMoneySystem().getMoney(targetMember.getId());

                    getMoneySystem().setMoney(fromMoney.getDiscordUserID(), fromMoney.getMoney() - Integer.parseInt(split[3]));
                    getMoneySystem().setMoney(targetMoney.getDiscordUserID(), targetMoney.getMoney() + Integer.parseInt(split[3]));

                    if (targetMember.getNickname() != null){
                        getMessage().reply(targetMember.getNickname() + "さんに "+split[3] + " "+ getMoneySystem().getCurrency()+"を送金しましたっ").queue();
                    } else {
                        getMessage().reply(targetMember.getUser().getName() + "さんに "+split[3] + " "+ getMoneySystem().getCurrency()+"を送金しましたっ").queue();
                    }
                } else {
                    getMessage().reply("その人 実は存在しないらしい。").queue();
                }

            }

        }

    }

    private void slot(){
        Slot slot = new Slot();
        String run = slot.run(getMoneySystem(), getMoneySystem().getMoney(getUser().getId()));
        getMessage().reply(run).queue();
    }

    private List<Omikuji> omikuji = new ArrayList<>();
    private void omikuji(){

        omikuji.add(new Omikuji("変態","変態は死刑ですっ！",-5));
        omikuji.add(new Omikuji("凶","ざんねーん！",0));
        omikuji.add(new Omikuji("吉","すごい微妙？",10));
        omikuji.add(new Omikuji("小吉","ちょっと微妙？",20));
        omikuji.add(new Omikuji("中吉","ふつうだね！",30));
        omikuji.add(new Omikuji("大吉","やったね！",40));
        omikuji.add(new Omikuji("遅刻","遅刻はダメですよ...",40));
        omikuji.add(new Omikuji("ななみちゃん","ボーナスですっ！",100));

        OmikujiGame omikujiGame = new OmikujiGame();
        String run = omikujiGame.run(getMoneySystem(), omikuji, getMoneySystem().getMoney(getUser().getId()));
        getMessage().reply(run).queue();
    }

    private void omikuji2(){
        omikuji.add(new Omikuji("変態","変態は死刑ですっ！",-10));
        omikuji.add(new Omikuji("凶","ざんねーん！",0));
        omikuji.add(new Omikuji("吉","すごい微妙？",10));
        omikuji.add(new Omikuji("小吉","ちょっと微妙？",20));
        omikuji.add(new Omikuji("中吉","ふつうだね！",30));
        omikuji.add(new Omikuji("大吉","やったね！",40));
        omikuji.add(new Omikuji("遅刻","遅刻はダメですよ...",50));
        omikuji.add(new Omikuji("ななみちゃん","ボーナスですっ！",100));

        omikuji.add(new Omikuji("ゆるり","昼夜逆転には気をつけよう！",10));
        omikuji.add(new Omikuji("砂",":Sand:",20));
        omikuji.add(new Omikuji("パンマスター","/pan",25));
        omikuji.add(new Omikuji("虫特攻",":thinking:",30));
        omikuji.add(new Omikuji("金装備","はてな",35));
        omikuji.add(new Omikuji("ふーぷれす",":thinking:",75));

        OmikujiGame omikujiGame = new OmikujiGame();
        String run = omikujiGame.run(getMoneySystem(), omikuji, getMoneySystem().getMoney(getUser().getId()));
        getMessage().reply(run).queue();
    }

    private void fx(){

        String[] split = getMessageText().split(" ", -1);
        Fx fx = new Fx();
        // System.out.println("debug "+split.length);
        if (split.length == 2){
            String run = fx.run(getMoneySystem(), getMoneySystem().getMoney(getUser().getId()), Integer.parseInt(split[1]));
            getMessage().reply(run).queue();
            return;
        }

        getMessage().reply("えらーですっ！\n`n.fx <掛け金>`で実行してください！").queue();

    }

    private void moneyRank(){

        StringBuffer sb = new StringBuffer();

        List<Money> moneyList = getMoneySystem().getMoneyList();

        String sendUserID = getUser().getId();

        int i = 1;
        int sendRank = 0;

        sb.append("---- ");
        sb.append(getMoneySystem().getCurrency());
        sb.append(" 所持数ランキング ----\n");

        for (Money money : moneyList){

            Member member = getGuild().getMemberById(money.getDiscordUserID());
            if (member != null && !member.getUser().isBot()){
                if (i <= 10){
                    sb.append(i);
                    sb.append("位 ");
                    if (member.getNickname() != null){
                        sb.append(member.getNickname());
                    } else {
                        sb.append(member.getUser().getName());
                    }
                    sb.append(" : ");
                    sb.append(money.getMoney());
                    sb.append(getMoneySystem().getCurrency());
                    sb.append("\n");
                }

                if (member.getId().equals(sendUserID)){
                    sendRank = i;
                }

                i++;
            }

        }

        sb.append("(あなたの順位は");
        sb.append(sendRank);
        sb.append("位です)");

        getMessage().reply(sb.toString()).queue();

    }

    private void kyusai(){
        Kyusai kyusai = new Kyusai();
        String run = kyusai.run(getMoneySystem(), getMoneySystem().getMoney(getUser().getId()));
        getMessage().reply(run).queue();
    }
}