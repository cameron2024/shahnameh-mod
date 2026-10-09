package com.shahnameh;

import com.shahnameh.item.LoreItem;
import com.shahnameh.item.MythSpawnEggItem;
import com.shahnameh.item.SimurghFeatherItem;
import com.shahnameh.item.WhiteDivHeartItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShahnamehMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShahnamehMod.MODID);

    // --- Treasures ---------------------------------------------------------

    public static final RegistryObject<Item> SIMURGH_FEATHER = ITEMS.register("simurgh_feather",
        () -> new SimurghFeatherItem(new Item.Properties()
            .setId(ITEMS.key("simurgh_feather"))
            .stacksTo(16)
            .rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIV_HORN = ITEMS.register("div_horn",
        () -> new LoreItem(new Item.Properties()
            .setId(ITEMS.key("div_horn"))));

    public static final RegistryObject<Item> WHITE_DIV_HEART = ITEMS.register("white_div_heart",
        () -> new WhiteDivHeartItem(new Item.Properties()
            .setId(ITEMS.key("white_div_heart"))
            .stacksTo(16)
            .rarity(Rarity.RARE)));

    /** Gorz-e Gavsar: Fereydun's ox-headed mace, forged by Kaveh the blacksmith. */
    public static final RegistryObject<Item> OX_MACE = ITEMS.register("ox_mace",
        () -> new LoreItem(new Item.Properties()
            .setId(ITEMS.key("ox_mace"))
            .durability(900)
            .rarity(Rarity.RARE)
            .attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 10.0, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.1, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
                .build())));

    public static final RegistryObject<Item> KAVEH_BANNER = ITEMS.register("kaveh_banner",
        () -> new BlockItem(ModBlocks.KAVEH_BANNER.get(), new Item.Properties()
            .setId(ITEMS.key("kaveh_banner"))
            .rarity(Rarity.UNCOMMON)));

    // --- Spawn eggs --------------------------------------------------------

    public static final RegistryObject<Item> DIV_SPAWN_EGG = ITEMS.register("div_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("div_spawn_egg")), ModEntities.DIV));
    public static final RegistryObject<Item> WHITE_DIV_SPAWN_EGG = ITEMS.register("white_div_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("white_div_spawn_egg")).rarity(Rarity.EPIC), ModEntities.WHITE_DIV));
    public static final RegistryObject<Item> MANTICORE_SPAWN_EGG = ITEMS.register("manticore_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("manticore_spawn_egg")), ModEntities.MANTICORE));
    public static final RegistryObject<Item> SIMURGH_SPAWN_EGG = ITEMS.register("simurgh_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("simurgh_spawn_egg")).rarity(Rarity.EPIC), ModEntities.SIMURGH));
    public static final RegistryObject<Item> HUMA_SPAWN_EGG = ITEMS.register("huma_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("huma_spawn_egg")), ModEntities.HUMA));
    public static final RegistryObject<Item> PERI_SPAWN_EGG = ITEMS.register("peri_spawn_egg",
        () -> new MythSpawnEggItem(new Item.Properties().setId(ITEMS.key("peri_spawn_egg")), ModEntities.PERI));

    // --- Creative tab ------------------------------------------------------

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("shahnameh", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.shahnameh"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> new ItemStack(SIMURGH_FEATHER.get()))
        .displayItems((_, output) -> {
            output.accept(SIMURGH_FEATHER.get());
            output.accept(OX_MACE.get());
            output.accept(KAVEH_BANNER.get());
            output.accept(WHITE_DIV_HEART.get());
            output.accept(DIV_HORN.get());
            output.accept(SIMURGH_SPAWN_EGG.get());
            output.accept(HUMA_SPAWN_EGG.get());
            output.accept(PERI_SPAWN_EGG.get());
            output.accept(MANTICORE_SPAWN_EGG.get());
            output.accept(DIV_SPAWN_EGG.get());
            output.accept(WHITE_DIV_SPAWN_EGG.get());
        })
        .build());
}
