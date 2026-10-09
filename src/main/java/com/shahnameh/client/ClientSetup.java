package com.shahnameh.client;

import com.shahnameh.ModEntities;
import com.shahnameh.ShahnamehMod;
import com.shahnameh.client.model.DivModel;
import com.shahnameh.client.model.HumaModel;
import com.shahnameh.client.model.ManticoreModel;
import com.shahnameh.client.model.ModelGeometry;
import com.shahnameh.client.model.PeriModel;
import com.shahnameh.client.model.SimurghModel;
import com.shahnameh.client.renderer.MythRenderer;
import com.shahnameh.entity.Div;
import com.shahnameh.entity.Huma;
import com.shahnameh.entity.Manticore;
import com.shahnameh.entity.Peri;
import com.shahnameh.entity.Simurgh;
import com.shahnameh.entity.WhiteDiv;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client-only setup: model layers and renderers. Only loaded on the client. */
public final class ClientSetup {
    private ClientSetup() {}

    public static final ModelLayerLocation DIV = layer("div");
    public static final ModelLayerLocation WHITE_DIV = layer("white_div");
    public static final ModelLayerLocation MANTICORE = layer("manticore");
    public static final ModelLayerLocation SIMURGH = layer("simurgh");
    public static final ModelLayerLocation HUMA = layer("huma");
    public static final ModelLayerLocation PERI = layer("peri");

    public static void register() {
        EntityRenderersEvent.RegisterLayerDefinitions.BUS.addListener(ClientSetup::onLayers);
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientSetup::onRenderers);
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(ShahnamehMod.id(name), "main");
    }

    private static Identifier texture(String name) {
        return ShahnamehMod.id("textures/entity/" + name + ".png");
    }

    private static void onLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DIV, ModelGeometry::createDiv);
        event.registerLayerDefinition(WHITE_DIV, ModelGeometry::createWhiteDiv);
        event.registerLayerDefinition(MANTICORE, ModelGeometry::createManticore);
        event.registerLayerDefinition(SIMURGH, ModelGeometry::createSimurgh);
        event.registerLayerDefinition(HUMA, ModelGeometry::createHuma);
        event.registerLayerDefinition(PERI, ModelGeometry::createPeri);
    }

    private static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DIV.get(),
            context -> new MythRenderer<Div, DivModel>(context, new DivModel(context.bakeLayer(DIV)), 0.9F, texture("div")));
        event.registerEntityRenderer(ModEntities.WHITE_DIV.get(),
            context -> new MythRenderer<WhiteDiv, DivModel>(context, new DivModel(context.bakeLayer(WHITE_DIV)), 1.3F, texture("white_div")));
        event.registerEntityRenderer(ModEntities.MANTICORE.get(),
            context -> new MythRenderer<Manticore, ManticoreModel>(context, new ManticoreModel(context.bakeLayer(MANTICORE)), 0.7F, texture("manticore")));
        event.registerEntityRenderer(ModEntities.SIMURGH.get(),
            context -> new MythRenderer<Simurgh, SimurghModel>(context, new SimurghModel(context.bakeLayer(SIMURGH)), 1.2F, texture("simurgh")));
        event.registerEntityRenderer(ModEntities.HUMA.get(),
            context -> new MythRenderer<Huma, HumaModel>(context, new HumaModel(context.bakeLayer(HUMA)), 0.4F, texture("huma")));
        event.registerEntityRenderer(ModEntities.PERI.get(),
            context -> new MythRenderer<Peri, PeriModel>(context, new PeriModel(context.bakeLayer(PERI)), 0.25F, texture("peri")));
    }
}
