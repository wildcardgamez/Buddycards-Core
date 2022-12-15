package com.wildcard.buddycards.battles;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.container.PlaymatContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class BuddycardBattle {
    static final String LOG = "battlesLog" + Buddycards.MOD_ID + ".";
    public final PlaymatContainer player1, player2;
    public boolean isPlayer1Turn = true;

    public BuddycardBattle(PlaymatContainer player1, PlaymatContainer player2) {
        if(Math.random() < .5) {
            this.player1 = player1;
            this.player2 = player2;
        }
        else {
            this.player1 = player2;
            this.player2 = player1;
        }
        player1.tryDrawCard();
        player1.tryDrawCard();
        player2.tryDrawCard();
        player2.tryDrawCard();
        battleLogOther("go_first");
        battleLogBoth("starting_draw");
        player1.tryDrawCard();
        battleLogOther("turn_draw");
        System.out.println("Game started");
    }

    public void battleLog(String name) {
        if(isPlayer1Turn)
            player1.battleLog.add(new TranslatableComponent(LOG + name));
        else
            player2.battleLog.add(new TranslatableComponent(LOG + name));
    }

    public void battleLogOther(String name) {
        if(isPlayer1Turn) {
            player1.battleLog.add(new TranslatableComponent(LOG + name));
            player2.battleLog.add(new TranslatableComponent(LOG + name + ".other"));
        } else {
            player2.battleLog.add(new TranslatableComponent(LOG + name));
            player1.battleLog.add(new TranslatableComponent(LOG + name + ".other"));
        }
    }

    public void battleLogBoth(String name) {
        player1.battleLog.add(new TranslatableComponent(LOG + name));
        player2.battleLog.add(new TranslatableComponent(LOG + name));
    }
}
