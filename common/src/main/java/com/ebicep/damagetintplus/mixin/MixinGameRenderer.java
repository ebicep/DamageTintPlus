package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        if (DamageTintPlus.INSTANCE.getUpdateTintColor()) {
            DamageTintPlus.INSTANCE.resetTintColor();
        }
    }

}
