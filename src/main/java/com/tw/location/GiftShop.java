package com.tw.location;

import com.tw.Player;
import com.tw.util.Gift;

public class GiftShop extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getSymbol() {
        return "G";
    }

    public void get(Player player, Gift gift) {
        switch (gift) {
            case MONEY:
                player.increaseBy(2000);
                break;
            case POINT:
                player.increasePoint(200);
                break;
            case GOD:
                player.getGod();
                break;
        }
    }

    public String listGifts() {
        return Gift.listGifts();
    }
}
