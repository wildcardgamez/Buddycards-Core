package com.wildcard.buddycards.battles.game;

import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.container.BattleContainer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

/**
 * to add new effects you have to add the new icon character to the font by adding 0ED0 + ordinal to the json as the character id and adding the texture as the next 5x5 icon.
 */
public enum BattleStatusEffect {

    EMPTY,
    FIRE(new BattleAbility(BattleEvent.TURN, "status.fire", fire()), 0xFFff6430),
    SLEEP(new BattleAbility(BattleEvent.FIGHT, "status.sleep", sleep()), 0xFFbafff6),
    STRENGTH(new BattleAbility(BattleEvent.FIGHT, "status.strength", strength()), 0xFFab002b),
    RESISTANCE(new BattleAbility(BattleEvent.DAMAGED, "status.resistance", resistance()), 0xFF627886),
    REGENERATION(new BattleAbility(BattleEvent.TURN, "status.regeneration", regeneration()), 0xFFf633ad),
    WEAKNESS(new BattleAbility(BattleEvent.FIGHT, "status.weakness", weakness()), 0xFF937725),
    POISON(new BattleAbility(BattleEvent.TURN, "status.poison", poison()), 0xFF55a03c),
    WITHER(new BattleAbility(BattleEvent.TURN, "status.wither", wither()), 0xFF000000),
    AIRBORNE(new BattleAbility(BattleEvent.TURN, "status.airborne", airborne()), 0xFFffdc75),
    STUNNED(new BattleAbility(BattleEvent.FIGHT, "status.stunned", stunned()), 0xFF937725);

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

    private static BattleAbility.BattleAbilityFunc strength() {
        return (game, slot, target, source) -> {
            game.state[slot].power+=2;
            game.state[slot].status = EMPTY;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.strength.log")), List.of(TextureBattleIcon.statusIcon(STRENGTH), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(2), TextureBattleIcon.statusIcon(EMPTY))));
            game.updatePower(slot);
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc resistance() {
        return (game, slot, target, source) -> {
            game.state[slot].power++;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.resistance.log")), List.of(TextureBattleIcon.statusIcon(RESISTANCE), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc regeneration() {
        return (game, slot, target, source) -> {
            game.state[slot].power++;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.regeneration.log")), List.of(TextureBattleIcon.statusIcon(REGENERATION), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc weakness() {
        return (game, slot, target, source) -> {
            game.state[slot].power -= 2;
            game.state[slot].status = EMPTY;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.weakness.log")), List.of(TextureBattleIcon.statusIcon(SLEEP), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.subtractIcon(2), TextureBattleIcon.statusIcon(EMPTY))));
            game.updatePower(slot);
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc poison() {
        return (game, slot, target, source) -> {
            if(game.state[slot].power > 0) {
                game.state[slot].power--;
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.poison.log")), List.of(TextureBattleIcon.statusIcon(SLEEP), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(slot);
            }
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc wither() {
        return (game, slot, target, source) -> {
            game.state[slot].power--;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.wither.log")), List.of(TextureBattleIcon.statusIcon(SLEEP), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.subtractIcon(1))));
            game.updatePower(slot);
            return true;
        };
    }

    private static BattleAbility.BattleAbilityFunc airborne() {
        return (game, slot, target, source) -> {
            int damage = game.turnPower[slot];
            if(BattleGame.getOwner(slot))
                game.container.health2-=damage;
            else
                game.container.health1-=damage;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.airborne.log1")).append("" + damage).append(new TranslatableComponent("battles.ability.buddycards.status.airborne.log2")).append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.status.airborne.log3")), List.of(TextureBattleIcon.statusIcon(AIRBORNE), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(damage), TextureBattleIcon.statusIcon(EMPTY))));
            return false;
        };
    }

    private static BattleAbility.BattleAbilityFunc stunned() {
        return (game, slot, target, source) -> {
            game.state[slot].status = EMPTY;
            game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(slot).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.status.stunned.log")), List.of(TextureBattleIcon.statusIcon(STUNNED), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.statusIcon(EMPTY))));
            return false;
        };
    }

}
