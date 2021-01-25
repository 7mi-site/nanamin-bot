package xyz.n7mn.dev.Command.game.jinro.ClassPlayer;

import xyz.n7mn.dev.Command.game.jinro.PlayerClass;

public class Werewolf implements PlayerClass {
    @Override
    public String getClassName() {
        return "人狼";
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isWerewolf() {
        return true;
    }
}
