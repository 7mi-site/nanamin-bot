package xyz.n7mn.dev.Command.game.jinro.ClassPlayer;

import xyz.n7mn.dev.Command.game.jinro.PlayerClass;

public class Villager implements PlayerClass {

    @Override
    public String getClassName() {
        return "一般人";
    }

    @Override
    public void run() {



    }

    @Override
    public boolean isWerewolf() {
        return false;
    }
}
