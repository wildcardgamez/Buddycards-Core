package com.wildcard.buddycards.battles.game;

import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.container.BattleContainer;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

/**
 * to add new effects you have to add the new icon character to the font by adding 0ED0 + ordinal to the json as the character id and adding the texture as the next 5x5 icon.
 */
public enum BattleStatusEffect {

    EMPTY,
    FIRE(new BattleAbility(BattleEvent.TURN, "status.fire", fire()), 0xFFff6430),
    SLEEP(new BattleAbility(BattleEvent.FIGHT, "status.sleep", sleep()), 0xFFbafff6);



    private final BattleAbility ability;
    private final int color;

    BattleStatusEffect(BattleAbility ability, int color) {
        this.ability = ability;
        this.color = color;
    }

    BattleStatusEffect() {
        ability = null;
        color = 0xFFFFFF;
    }

    public BattleAbility getAbility() {
        return ability;
    }

    public int getColor() {
        return color;
    }

    private static BattleAbility.BattleAbilityFunc fire() {
        return (game,slot,target,source) ->{
            if(BattleContainer.random.nextBoolean()) {
                game.state[slot].status=EMPTY;
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.fire.log1")),List.of(TextureBattleIcon.statusIcon(FIRE),TextureBattleIcon.dividerIcon,BuddycardBattleIcon.create(game.getCard(slot)),TextureBattleIcon.statusIcon(EMPTY))));
            } else{
                game.turnPower[slot]--;
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.fire.log2")),List.of(TextureBattleIcon.statusIcon(FIRE),TextureBattleIcon.dividerIcon,BuddycardBattleIcon.create(game.getCard(slot)),TextureBattleIcon.subtractIcon(1))));
                game.updatePower(slot);
            }
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc sleep() {
        return (game, slot, target, source) -> {
            game.state[slot].status = EMPTY;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.sleep.log")), List.of(TextureBattleIcon.statusIcon(SLEEP), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.statusIcon(EMPTY))));
            return false;
        };
    }
}
