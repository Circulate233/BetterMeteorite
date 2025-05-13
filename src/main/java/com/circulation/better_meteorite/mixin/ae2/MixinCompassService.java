package com.circulation.better_meteorite.mixin.ae2;

import appeng.services.CompassService;
import appeng.services.compass.CompassReader;
import com.circulation.better_meteorite.BMConfig;
import com.circulation.better_meteorite.util.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Mixin(value = CompassService.class,remap = false)
public class MixinCompassService {

    @Final
    @Shadow
    private static int CHUNK_SIZE;
    @Final
    @Shadow
    private Map<World, CompassReader> worldSet;
    @Final
    @Shadow
    private ExecutorService executor;

    @Unique
    private static Constructor<?> randomComplement$constructor;

    @Unique
    private static List<ResourceLocation> randomComplement$MeteoriteCompassTarget = new ArrayList<>();
    @Unique
    private static final List<Integer> randomComplement$meta = new ArrayList<>();

    @Inject(method = "updateArea(Lnet/minecraft/world/World;III)Ljava/util/concurrent/Future;",at = @At("HEAD"), cancellable = true)
    public void updateAreaMixin(World w, int x, int y, int z, CallbackInfoReturnable<Future<?>> cir) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final int cx = x >> 4;
        final int cdy = y >> 5;
        final int cz = z >> 4;

        final int low_y = cdy << 5;
        final int hi_y = low_y + 32;

        final Chunk c = w.getChunk(cx, cz);

        if (randomComplement$constructor == null){
            try {
                Class<?> clazz = Class.forName("appeng.services.CompassService$CMUpdatePost");
                randomComplement$constructor = clazz.getDeclaredConstructor(CompassService.class,World.class,int.class,int.class,int.class,boolean.class);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            randomComplement$constructor.setAccessible(true);
        }

        if (randomComplement$MeteoriteCompassTarget.isEmpty()){
            if (BMConfig.MeteoriteCompassTarget.length != 0) {
                for (String s : BMConfig.MeteoriteCompassTarget) {
                    var block = Function.getBlockFromName(s);
                    if (block == null)continue;
                    randomComplement$MeteoriteCompassTarget.add(block.getBlock().getRegistryName());
                    randomComplement$meta.add(block.getBlock().getMetaFromState(block));
                }
            } else {
                randomComplement$MeteoriteCompassTarget = null;
            }
            if (randomComplement$MeteoriteCompassTarget.isEmpty())randomComplement$MeteoriteCompassTarget = null;
        }
        if (randomComplement$MeteoriteCompassTarget != null){
            for (int i = 0; i < CHUNK_SIZE; i++) {
                for (int j = 0; j < CHUNK_SIZE; j++) {
                    for (int k = low_y; k < hi_y; k++) {
                        final IBlockState state = c.getBlockState(i, k, j);
                        final Block blk = state.getBlock();
                        int meta = blk.getMetaFromState(state);
                        for (int i1 = 0; i1 < randomComplement$MeteoriteCompassTarget.size(); i1++) {
                            if (blk.getRegistryName() == randomComplement$MeteoriteCompassTarget.get(i1) && meta == randomComplement$meta.get(i1)) {
                                cir.setReturnValue(this.executor.submit((Runnable) randomComplement$constructor.newInstance(this,w, cx, cz, cdy, true)));
                                return;
                            }
                        }
                    }
                }
            }
        }

        cir.setReturnValue(this.executor.submit((Runnable) randomComplement$constructor.newInstance(this,w, cx, cz, cdy, false)));
    }
}
