package com.shahnameh.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class PeriModel extends MythModel {
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public PeriModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        look(this.head, state);
        // Fast fluttering wings, dangling legs, waving arms.
        float flutter = Mth.sin(state.ageInTicks * 1.3F) * 0.45F;
        this.leftWing.yRot -= flutter;
        this.rightWing.yRot += flutter;
        float drift = Mth.sin(state.ageInTicks * 0.1F);
        this.rightArm.zRot += 0.3F + drift * 0.15F;
        this.leftArm.zRot -= 0.3F + drift * 0.15F;
        this.rightLeg.xRot += 0.2F + drift * 0.1F;
        this.leftLeg.xRot += 0.2F - drift * 0.1F;
    }
}
