package com.ebicep.damagetintplus.mixin;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.CompositeRenderType.class)
public interface ICompositeRenderTypeMixin {

    @Invoker("state")
    RenderType.CompositeState callState();

}
