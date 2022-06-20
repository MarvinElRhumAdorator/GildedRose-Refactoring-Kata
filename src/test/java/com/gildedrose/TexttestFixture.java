package com.gildedrose;

import java.util.stream.Stream;

public class TexttestFixture {

    public static final String DEXTERITY_VEST = "+5 Dexterity Vest";
    public static final String ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose";
    public static final String AGED_BRIE = "Aged Brie";
    public static final String SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    public static final String BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert";

    public static void main(String[] args) {
        System.out.println("OMGHAI!");

        Item[] items = getInputItems();

        GildedRose app = new GildedRose(items);

        int days = 2;
        if (args.length > 0) {
            days = Integer.parseInt(args[0]) + 1;
        }

        for (int i = 0; i < days; i++) {
            System.out.println("-------- day " + i + " --------");
            System.out.println("name, sellIn, quality");
            for (Item item : items) {
                System.out.println(item);
            }
            System.out.println();
            app.updateQuality();
        }
    }

    public static Item copy(Item item) {
        return new Item(item.name, item.sellIn, item.quality);
    }

    public static Item[] copy(Item[] items) {
        return Stream.of(items).map(TexttestFixture::copy).toArray(Item[]::new);
    }

    static Item[] getInputItems() {
        return new Item[]{
            new Item(DEXTERITY_VEST, 10, 20), //
            new Item(AGED_BRIE, 2, 0), //
            new Item(ELIXIR_OF_THE_MONGOOSE, 5, 7), //
            new Item(SULFURAS_HAND_OF_RAGNAROS, 0, 80), //
            new Item(SULFURAS_HAND_OF_RAGNAROS, -1, 80),
            new Item(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 15, 20),
            new Item(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 10, 49),
            new Item(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT, 5, 49),
            // this conjured item does not work properly yet
//            new Item("Conjured Mana Cake", 3, 6)
        };
    }

}
