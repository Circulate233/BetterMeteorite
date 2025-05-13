package com.circulation.better_meteorite.mixin.ae2;

import appeng.worldgen.MeteoriteWorldGen;
import com.circulation.better_meteorite.BetterMeteorite;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MeteoriteWorldGen.class,remap = false)
public class MixinMeteoriteWorldGen {

    @Inject(method = "tryMeteorite",at = @At("RETURN"))
    private void tryMeteorite(World w, int depth, int x, int z, CallbackInfoReturnable<Boolean> cir) {
        BetterMeteorite.proxy.canSkyStone = true;
    }

}
