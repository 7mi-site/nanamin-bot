package xyz.n7mn.dev.game;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.n7mn.dev.api.Money;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Long defaultMoney = 1000L;

    private final String[] gameList = new String[]{
            "money",
            "omikuji",
            "fx",
            "giveme"
    };
    private final String[] gameDescList = new String[]{
            "コイン数が確認できるよ！",
            "おみくじが引けるよ！",
            "ハイリスク・ハイリターンなFXができるよ！",
            "・・・お金ないときの最終手段ですよ？"
    };

    public void run(SlashCommandInteractionEvent event){

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("ななみちゃんbot ミニゲーム");
        builder.setDescription("");
        builder.setColor(Color.PINK);
        builder.clearFields();

        if (event.getOption("種類").getAsString().equals("help")){
            builder.setDescription("いま遊べるミニゲームは以下のとおりです！");
            for (int i = 0; i < gameList.length; i++){
                builder.addField("/game "+gameList[i], gameDescList[i], false);
            }

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        if (event.getOption("種類").getAsString().equals("money")){
            Long money = Money.get(event.getMember().getId());
            if (money == null){
                money = defaultMoney;
                Money.set(event.getMember().getId(), defaultMoney);
            }

            builder.setDescription("現在のコイン数は\n"+money+"コインです！");
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        if (event.getOption("種類").getAsString().equals("omikuji")){
            File file = new File("./money/default.yml");

            List<OmikujiData> list = new ArrayList<>();

            if (new File("./money/"+event.getGuild().getId()+".yml").exists()){
                file = new File("./money/"+event.getGuild().getId()+".yml");
            }

            try {
                YamlMapping yaml = Yaml.createYamlInput(file).readYamlMapping();
                for (int i = 0; i < yaml.yamlSequence("Value").size(); i++){
                    list.add(new OmikujiData(yaml.yamlSequence("Value").string(i), yaml.yamlSequence("Money").longNumber(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int i = new SecureRandom().nextInt(list.size());
            OmikujiData data = list.get(i);
            Long money = Money.get(event.getMember().getId());
            if (money == null){
                money = defaultMoney;
            }
            Money.set(event.getMember().getId(), money + data.getAddMoney());

            builder.setDescription("おみくじの結果は\n"+data.getValue()+"\nでした！\n("+data.getAddMoney()+"コイン獲得！)");
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            return;
        }

        if (event.getOption("種類").getAsString().equals("fx")){

            if (event.getOption("掛け金") == null){
                builder.setColor(Color.RED);
                builder.setDescription("掛け金を入れてくださいね？");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            int money = event.getOption("掛け金").getAsInt();
            long nowMoney = (Money.get(event.getMember().getId()) == null ? defaultMoney : Money.get(event.getMember().getId()));

            if (money <= 0){
                builder.setColor(Color.RED);
                builder.setDescription("掛け金は0以上の数字を入れてくださいね？");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            if (money > nowMoney){
                builder.setColor(Color.RED);
                builder.setDescription("・・・お金が足りてないですよ？");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            int multiple = 0;
            if (money <= 100000){
                multiple = money / 100;
            } else {
                multiple = money / 150;
            }

            if (multiple == 0){
                multiple = 1;
            }

            long startMoney = ((long) money * multiple);

            long count1 = new SecureRandom().nextLong();
            long count2 = new SecureRandom().nextLong();
            long count3 = new SecureRandom().nextLong();
            long count4 = new SecureRandom().nextLong();
            long count5 = new SecureRandom().nextLong();

            if (count1 > startMoney){
                count1 = (long) (startMoney * 0.9);
            }
            if (count1 < -startMoney){
                count1 = (long) -(startMoney * 0.9);
            }

            if (count2 > startMoney){
                count2 = (long) (startMoney * 0.9);
            }
            if (count2 < -startMoney){
                count2 = (long) -(startMoney * 0.9);
            }

            if (count3 > startMoney){
                count3 = (long) (startMoney * 0.9);
            }
            if (count3 < -startMoney){
                count3 = (long) -(startMoney * 0.9);
            }

            if (count4 > startMoney){
                count4 = (long) (startMoney * 0.9);
            }
            if (count4 < -startMoney){
                count4 = (long) -(startMoney * 0.9);
            }

            if (count5 > startMoney){
                count5 = (long) (startMoney * 0.9);
            }
            if (count5 < -startMoney){
                count5 = (long) -(startMoney * 0.9);
            }

            if (!new SecureRandom().nextBoolean()){
                count1 = -count1;
            }
            if (new SecureRandom().nextBoolean()){
                count2 = -count2;
            }
            if (!new SecureRandom().nextBoolean()){
                count3 = -count3;
            }
            if (new SecureRandom().nextBoolean()){
                count4 = -count4;
            }
            if (!new SecureRandom().nextBoolean()){
                count5 = -count5;
            }

            long endMoney = startMoney + count1 + count2 + count3 + count4 + count5 - ((long) money * multiple);

            builder.setDescription(multiple+"倍もーど！\n開始資金は"+startMoney+"コイン！");

            builder.addField("1回目！", Math.abs(count1)+"コイン" + (count1 >= 0 ? "あがったよ！" : "さがったよ！"), false);
            builder.addField("2回目！", Math.abs(count2)+"コイン" + (count2 >= 0 ? "あがったよ！" : "さがったよ！"), false);
            builder.addField("3回目！", Math.abs(count3)+"コイン" + (count3 >= 0 ? "あがったよ！" : "さがったよ！"), false);
            builder.addField("4回目！", Math.abs(count4)+"コイン" + (count4 >= 0 ? "あがったよ！" : "さがったよ！"), false);
            builder.addField("5回目！", Math.abs(count5)+"コイン" + (count5 >= 0 ? "あがったよ！" : "さがったよ！"), false);
            builder.addField("結果！！", endMoney+"コイン獲得！", false);

            Money.set(event.getMember().getId(), nowMoney + endMoney);
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            return;
        }

        if (event.getOption("種類").getAsString().equals("giveme")){
            String[] strings = {"・・・。", "・・・プライドないないでちゅか～？", "・・・。", "・・・。"};

            Long mon = Money.get(event.getMember().getId());
            if (mon == null || mon >= 0){
                builder.setDescription("・・・あなたにはまだ関係ないと思いますよ？");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                return;
            }

            long money = mon + new SecureRandom().nextInt();
            if (money >= 1000) { money = 1000L; }
            Money.set(event.getMember().getId(), money);

            builder.addField("現在の所持金：",money+" コイン", false);

            builder.setDescription(strings[new SecureRandom().nextInt(strings.length)]);
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            return;
        }

        builder.setDescription("まだ実装されていないか存在しません！");
        event.replyEmbeds(builder.build()).setEphemeral(false).queue();
    }

}
