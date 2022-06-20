package com.gildedrose;

class GildedRose {
    public static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";
    public static final String AGED_BRIE = "Aged Brie";
    public static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";

    private Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            updateItem(items[i]);
        }
    }

    private void updateItem(Item item) {
        if (isNamed(item, SULFURAS_HAND_OF_RAGNAROS)) {
            return;
        }
        updateSellIn(item);
        updateQuality(item);
    }

    private void updateQuality(Item item) {

        switch (item.name) {
            case AGED_BRIE:
                increaseQuality(item, 1);
                break;
            case BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT:
                if (item.sellIn < 5) {
                    increaseQuality(item, 3);
                } else if (item.sellIn < 10) {
                    increaseQuality(item, 2);
                } else {
                    increaseQuality(item, 1);
                }
                break;
            default:
                decreaseQuality(item);
        }

        if (item.sellIn < 0) {
            switch (item.name) {
                case AGED_BRIE:
                    increaseQuality(item, 1);
                    break;
                case BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT:
                    item.quality = 0;
                    break;
                default:
                    decreaseQuality(item);

            }
        }
    }

    private boolean isNamed(Item item, String agedBrie) {
        return item.name.equals(agedBrie);
    }

    private void updateSellIn(Item item) {
        item.sellIn = item.sellIn - 1;
    }

    private void increaseQuality(Item item, int increment) {
        for (int i = 0; i < increment; i++) {
            increaseQuality(item);
        }
    }

    private void increaseQuality(Item item) {
        if (item.quality < 50) {
            item.quality = item.quality + 1;
        }
    }

    private void decreaseQuality(Item item) {
        if (item.quality > 0) {
            item.quality = item.quality - 1;
        }
    }
}
