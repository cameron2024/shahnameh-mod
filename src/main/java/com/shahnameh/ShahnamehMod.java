package com.shahnameh;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

/**
 * Shahnameh: Legends of Persia.
 * Creatures and treasures from Ferdowsi's Shahnameh and older Persian myth.
 */
@Mod(ShahnamehMod.MODID)
public final class ShahnamehMod {
    public static final String MODID = "shahnameh";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ShahnamehMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        ModBlocks.BLOCKS.register(modBusGroup);
        ModEntities.ENTITY_TYPES.register(modBusGroup);
        ModItems.ITEMS.register(modBusGroup);
        ModItems.CREATIVE_TABS.register(modBusGroup);

        ModEvents.register();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.shahnameh.client.ClientSetup.register();
        }
        LOGGER.info("The Book of Kings is open.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
