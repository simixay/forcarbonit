package com.forcarbonit.game;

import lombok.Getter;
import lombok.Setter;

public class CellInfo {
    @Getter
    @Setter
    private int _bounties = 0;

    @Getter
    @Setter
    private Player _player;// = new Player();

    @Getter
    @Setter
    private boolean _hasMountain;

    @Getter
    @Setter
    private boolean _hasPlayer;

    @Getter
    @Setter
    private boolean _hasBounty;

    public void setNewAdventurer(Player player) {
        _hasBounty = false;
        _hasMountain = false;
        _hasPlayer = true;
        _player = player;
    }

    public void setAdventurer(Player player) {
        _player = player;
        _hasPlayer = true;
    }

    public void setNewMountain() {
        _hasMountain = true;
        _hasPlayer = false;
        _hasBounty = false;
    }

    public void setNewBounty(int bounty) {
        _hasBounty = true;
        _hasPlayer = false;
        _hasMountain = false;
        _bounties = bounty;
    }

    public void setBounty(int bounty) {
        _hasBounty = true;
        _bounties = bounty;
    }
}
