package com.shahnameh.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class HumaModel extends MythModel {
    private final ModelPart head;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public HumaModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        look(this.head, state);
        // The Huma glides: long slow beats.
        float flap = Mth.sin(state.ageInTicks * 0.12F) * 0.35F;
        this.leftWing.zRot += flap;
        this.rightWing.zRot -= flap;
    }
}
