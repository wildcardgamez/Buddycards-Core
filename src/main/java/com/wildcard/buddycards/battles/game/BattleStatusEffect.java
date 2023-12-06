package com.wildcard.buddycards.battles.game;

import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.container.BattleContainer;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public final class BattleStatusEffect {
    public static final ArrayList<BattleStatusEffect> EFFECTS = new ArrayList<>();

    public static final BattleStatusEffect EMPTY = new BattleStatusEffect(0);
    public static final BattleStatusEffect FIRE = new BattleStatusEffect(1, new BattleAbility(BattleEvent.TURN, "status.fire", (game, slot, target, source) -> {
        if(BattleContainer.random.nextBoolean()) {
            game.state[slot].status = 0;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.fire.log1")), List.of(TextureBattleIcon.statusIcon(1), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.statusIcon(0))));
        }
        else {
            game.turnPower[slot]--;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.fire.log2")), List.of(TextureBattleIcon.statusIcon(1), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.subtractIcon(1))));
            game.updatePower(slot);
        }
        return true;
    }));
    public static final BattleStatusEffect SLEEP = new BattleStatusEffect(2, new BattleAbility(BattleEvent.FIGHT, "status.sleep", (game, slot, target, source) -> {
        game.state[slot].status = 0;
        game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.sleep.log")), List.of(TextureBattleIcon.statusIcon(2), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.statusIcon(0))));
        return false;
    }));

    BattleStatusEffect(int identifier, BattleAbility ability) {
        ABILITY = ability;
        ID = identifier;
        EFFECTS.add(identifier, this);
    }

    BattleStatusEffect(int identifier) {
        ABILITY = null;
        ID = identifier;
        EFFECTS.add(identifier, this);
    }

    private final BattleAbility ABILITY;
    private final int ID;

    public BattleAbility getAbility() {
        return ABILITY;
    }

    public int getId() {
        return ID;
    }
}
