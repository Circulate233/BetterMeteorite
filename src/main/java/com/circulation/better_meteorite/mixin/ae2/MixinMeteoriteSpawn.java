package com.circulation.better_meteorite.mixin.ae2;

import com.circulation.better_meteorite.BetterMeteorite;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.worldgen.MeteoriteWorldGen$MeteoriteSpawn",remap = false)
public class MixinMeteoriteSpawn {

    @Inject(method = "call",at = @At("TAIL"))
    public void call(World world, CallbackInfoReturnable<Object> cir) {
        BetterMeteorite.proxy.canSkyStone = true;
    }
}
