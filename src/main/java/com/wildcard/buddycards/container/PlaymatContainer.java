package com.wildcard.buddycards.container;

import net.minecraft.world.SimpleContainer;

public class PlaymatContainer extends SimpleContainer {
    public PlaymatContainer() {
        super(7);
    }

    public PlaymatContainer(PlaymatContainer opponent) {
        super(7);
        this.opponent = opponent;
    }

    public PlaymatContainer opponent;
}
