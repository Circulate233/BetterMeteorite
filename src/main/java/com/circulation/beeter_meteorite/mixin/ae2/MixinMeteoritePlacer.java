package com.circulation.beeter_meteorite.mixin.ae2;

import appeng.api.definitions.IBlockDefinition;
import appeng.core.AEConfig;
import appeng.core.features.AEFeature;
import appeng.worldgen.MeteoritePlacer;
import appeng.worldgen.meteorite.IMeteoriteWorld;
import appeng.worldgen.meteorite.MeteoriteBlockPutter;
import com.circulation.beeter_meteorite.BMConfig;
import com.circulation.beeter_meteorite.util.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = MeteoritePlacer.class,remap = false)
public abstract class MixinMeteoritePlacer {

    @Shadow
    @Final
    private IBlockDefinition skyStoneDefinition;

    @Unique
    private static Map<IBlockState,Integer> randomComplement$skyStoneReward = new HashMap<>();
    @Unique
    private static int randomComplement$sumWeight = 0;

    @Shadow
    @Final
    private MeteoriteBlockPutter putter;

    @Shadow @Final private IBlockDefinition skyChestDefinition;

    @Shadow @Final private static double PRESSES_SPAWN_CHANCE;

    @Shadow @Final private static int SKYSTONE_SPAWN_LIMIT;

    @Shadow private double squaredMeteoriteSize;

    @Shadow protected abstract void placeMeteoriteSkyStone(IMeteoriteWorld w, int x, int y, int z, Block block);

    @Inject(method = "placeMeteorite",at = @At("HEAD"),cancellable = true)
    private void placeMeteoriteMixin(IMeteoriteWorld w, int x, int y, int z, CallbackInfo ci) {
        if (randomComplement$skyStoneReward != null && randomComplement$skyStoneReward.isEmpty()) {
            randomComplement$initReward();
        }
        this.skyStoneDefinition.maybeBlock().ifPresent(block -> this.placeMeteoriteSkyStone(w, x, y, z, block));
        if (randomComplement$skyStoneReward != null && AEConfig.instance().isFeatureEnabled(AEFeature.SPAWN_PRESSES_IN_METEORITES)) {
            IBlockState block = Blocks.STONE.getDefaultState();
            int r = w.getWorld().rand.nextInt(randomComplement$sumWeight + 1);

            for (Map.Entry<IBlockState, Integer> reward : randomComplement$skyStoneReward.entrySet()) {
                var iBlock = reward.getKey();
                var weight = reward.getValue();
                r -= weight;
                if (r <= 0){
                    block = iBlock;
                    break;
                }
            }

            this.putter.put(w, x, y, z, block);
        }
        ci.cancel();
    }

    @Unique
    private void randomComplement$initReward(){
        String[] skyStoneReward = BMConfig.skyStoneReward;
        if (skyStoneReward.length == 0) {
            randomComplement$skyStoneReward = null;
            return;
        }
        int[] sourceWeights = BMConfig.skyStoneRewardWeight;
        int[] skyStoneRewardWeight = new int[skyStoneReward.length];

        if (sourceWeights.length < skyStoneReward.length) {
            System.arraycopy(sourceWeights, 0, skyStoneRewardWeight, 0, sourceWeights.length);
            for (int i = sourceWeights.length; i < skyStoneRewardWeight.length; i++) {
                skyStoneRewardWeight[i] = 1;
            }
        } else {
            skyStoneRewardWeight = sourceWeights;
        }

        for (int i = 0; i < skyStoneReward.length; i++) {
            String blockName = skyStoneReward[i];
            IBlockState block = Function.getBlockFromName(blockName);
            if (block == null)continue;
            int weight = skyStoneRewardWeight[i];
            randomComplement$skyStoneReward.put(block,weight);
            randomComplement$sumWeight += weight;
        }
        if (randomComplement$skyStoneReward.isEmpty())randomComplement$skyStoneReward = null;
    }

}
