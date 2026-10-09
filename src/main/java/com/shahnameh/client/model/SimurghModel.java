package com.shahnameh.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class SimurghModel extends MythModel {
    private final ModelPart neck;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;

    public SimurghModel(ModelPart root) {
        super(root);
        this.neck = root.getChild("neck");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.tail = root.getChild("tail");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.neck.yRot += state.yRot * DEG * 0.5F;
        float flap = Mth.sin(state.ageInTicks * 0.18F) * 0.55F;
        this.leftWing.zRot += flap;
        this.rightWing.zRot -= flap;
        this.tail.xRot += Mth.sin(state.ageInTicks * 0.09F) * 0.06F;
    }
}
