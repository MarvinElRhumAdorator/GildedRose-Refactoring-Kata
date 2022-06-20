package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.gildedrose.TexttestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class GildedRoseTest {


    public static final int MAX_QUALITY = 50;

    @ParameterizedTest
    @ValueSource(strings = {DEXTERITY_VEST, ELIXIR_OF_THE_MONGOOSE})
    void should_decrease_quality_for_normal_items(String itemName) {
        Item[] inputItems = getInputItems();
        Item specificItem = getItemWithName(itemName, inputItems);
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(oldItem.quality - 1);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {AGED_BRIE})
    void should_increase_quality_for_brie(String itemName) {
        Item[] inputItems = getInputItems();
        Item specificItem = getItemWithName(itemName, inputItems);
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(oldItem.quality + 1);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {SULFURAS_HAND_OF_RAGNAROS})
    void should_not_change_sulfuras(String itemName) {
        Item[] inputItems = getInputItems();
        Item[] specificItems = getItemsWithName(itemName, inputItems);
        Item[] oldItems = copy(specificItems);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        for (int i = 0; i < specificItems.length; i++) {
            Item specificItem = specificItems[i];
            Item oldItem = oldItems[i];
            assertThat(specificItem.quality)
                .as("expected quality after updating " + oldItem)
                .isEqualTo(oldItem.quality);

            assertThat(specificItem.sellIn)
                .as("expected sellIn after updating " + oldItem)
                .isEqualTo(oldItem.sellIn);
        }
    }

    @Test
    void should_never_get_quality_more_than_50_when_different_from_sulfuras() {
        Item[] inputItems = getInputItems();
        Item[] specificItems = Stream.of(inputItems)
            .filter(item -> !item.name.equals(SULFURAS_HAND_OF_RAGNAROS))
            .toArray(Item[]::new);
        Item[] oldItems = copy(specificItems);

        GildedRose app = new GildedRose(inputItems);

        for (int j = 0; j < 100; j++) {
            app.updateQuality();
            for (int i = 0; i < specificItems.length; i++) {
                Item specificItem = specificItems[i];
                Item oldItem = oldItems[i];
                assertThat(specificItem.quality)
                    .as("expected quality after updating " + oldItem)
                    .isLessThanOrEqualTo(50);
            }
        }
    }

    @Test
    void should_never_get_quality_negative() {
        Item[] inputItems = getInputItems();

        GildedRose app = new GildedRose(inputItems);

        for (int j = 0; j < 100; j++) {
            Item[] oldItems = copy(inputItems);
            app.updateQuality();
            for (int i = 0; i < inputItems.length; i++) {
                Item specificItem = inputItems[i];
                Item oldItem = oldItems[i];
                assertThat(specificItem.quality)
                    .as("expected quality after updating " + oldItem)
                    .isGreaterThanOrEqualTo(0);
            }
        }
    }

    @Test
    void should_increase_quality_for_concert_from_more_than_10_days() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn > 10)
            .findFirst().orElseThrow();
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(oldItem.quality + 1);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @Test
    void should_increase_quality_for_concert_from_more_than_5_days() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn <= 10)
            .filter(item -> item.sellIn > 5)
            .findFirst().orElseThrow();
        specificItem.quality = 48; // Can be more than 50 when updated, so test increase by 2
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(oldItem.quality + 2);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @Test
    void should_increase_quality_for_concert_from_more_than_5_days_to_max_quality() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn <= 10)
            .filter(item -> item.sellIn > 5)
            .findFirst().orElseThrow();
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(MAX_QUALITY);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @Test
    void should_increase_quality_for_concert_from_at_most_5_days() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn <= 5)
            .findFirst().orElseThrow();
        specificItem.quality = 45; // Can be more than 50 when updated, so test increase by 2
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(oldItem.quality + 3);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @Test
    void should_increase_quality_for_concert_from_at_most_5_days_to_max_quality() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn <= 5)
            .findFirst().orElseThrow();
        specificItem.sellIn = 0; // After concert

        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(0);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    @Test
    void should_set_quality_to_0_after_concert() {
        Item[] inputItems = getInputItems();
        Item specificItem = Stream.of(inputItems)
            .filter(item -> item.name.equals(BACKSTAGE_PASSES_TO_A_TAFKAL_80_ETC_CONCERT))
            .filter(item -> item.sellIn <= 5)
            .findFirst().orElseThrow();
        Item oldItem = TexttestFixture.copy(specificItem);

        GildedRose app = new GildedRose(inputItems);

        app.updateQuality();

        assertThat(specificItem.quality)
            .as("expected quality after updating " + oldItem)
            .isEqualTo(MAX_QUALITY);

        assertThat(specificItem.sellIn)
            .as("expected sellIn after updating " + oldItem)
            .isEqualTo(oldItem.sellIn - 1);
    }

    private Item getItemWithName(String itemName, Item[] inputItems) {
        return Stream.of(inputItems).filter(item -> item.name.equals(itemName)).findFirst().orElseThrow();
    }

    private Item[] getItemsWithName(String itemName, Item[] inputItems) {
        return Stream.of(inputItems).filter(item -> item.name.equals(itemName)).toArray(Item[]::new);
    }
}
