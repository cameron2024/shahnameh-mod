package com.shahnameh;

import com.shahnameh.block.KavehBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ShahnamehMod.MODID);

    public static final RegistryObject<Block> KAVEH_BANNER = BLOCKS.register("kaveh_banner",
        () -> new KavehBannerBlock(BlockBehaviour.Properties.of()
            .setId(BLOCKS.key("kaveh_banner"))
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(1.0F)
            .sound(SoundType.WOOD)
            .noOcclusion()
            .lightLevel(state -> 6)));
}
