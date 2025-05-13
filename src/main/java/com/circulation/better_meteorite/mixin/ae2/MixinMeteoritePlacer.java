package com.circulation.better_meteorite.mixin.ae2;

import appeng.api.definitions.IBlockDefinition;
import appeng.core.AEConfig;
import appeng.core.features.AEFeature;
import appeng.worldgen.MeteoritePlacer;
import appeng.worldgen.meteorite.IMeteoriteWorld;
import appeng.worldgen.meteorite.MeteoriteBlockPutter;
import com.circulation.better_meteorite.BMConfig;
import com.circulation.better_meteorite.BetterMeteorite;
import com.circulation.better_meteorite.util.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = MeteoritePlacer.class,remap = false)
public abstract class MixinMeteoritePlacer {

    @Shadow
    @Final
    private IBlockDefinition skyStoneDefinition;

    @Unique
    private static Map<IBlockState,Integer> betterMeteorite$skyStoneReward = new HashMap<>();
    @Unique
    private static Map<IBlockState,Integer> betterMeteorite$skyStone = new HashMap<>();
    @Unique
    private static int betterMeteorite$sumWeight = 0;
    @Unique
    private static int betterMeteorite$sumStoneWeight = 0;
    @Unique
    private static IBlockState betterMeteorite$skyStoneInstance;

    @Shadow
    @Final
    private MeteoriteBlockPutter putter;

    @Shadow
    protected abstract void placeMeteoriteSkyStone(IMeteoriteWorld w, int x, int y, int z, Block block);

    @Shadow
    @Final
    private Collection<Block> invalidSpawn;

    @Inject(method = "<init>",at = @At("TAIL"))
    private void onInit(CallbackInfo ci){
        if (betterMeteorite$skyStoneReward != null && betterMeteorite$skyStoneReward.isEmpty()) {
            betterMeteorite$initReward();
        }
    }

    @Redirect(method = "placeMeteoriteSkyStone",at = @At(value = "INVOKE", target = "Lappeng/worldgen/meteorite/MeteoriteBlockPutter;put(Lappeng/worldgen/meteorite/IMeteoriteWorld;IIILnet/minecraft/block/Block;)Z"))
    protected boolean putMixin(MeteoriteBlockPutter instance, IMeteoriteWorld w, int i, int j, int k, Block blk){
        if (betterMeteorite$skyStone != null && betterMeteorite$skyStoneInstance != null){
            Block original = w.getBlock(i, j, k);
            if (original != Blocks.BEDROCK && original != blk) {
                w.setBlock(i, j, k, betterMeteorite$skyStoneInstance, 3);
                return true;
            } else {
                return false;
            }
        } else {
            return instance.put(w, i, j, k, blk);
        }
    }

    @Inject(method = "placeMeteorite",at = @At("HEAD"),cancellable = true)
    private void placeMeteoriteMixin(IMeteoriteWorld w, int x, int y, int z, CallbackInfo ci) {
        if (BetterMeteorite.proxy.canSkyStone && betterMeteorite$skyStone != null) {
            int r = w.getWorld().rand.nextInt(betterMeteorite$sumStoneWeight) + 1;
            for (Map.Entry<IBlockState, Integer> reward : betterMeteorite$skyStone.entrySet()) {
                var iBlock = reward.getKey();
                var weight = reward.getValue();
                r -= weight;
                if (r <= 0) {
                    betterMeteorite$skyStoneInstance = iBlock;
                    break;
                }
            }
        }
        BetterMeteorite.proxy.canSkyStone = false;

        this.skyStoneDefinition.maybeBlock().ifPresent(block -> this.placeMeteoriteSkyStone(w, x, y, z, block));

        if (betterMeteorite$skyStoneReward != null && AEConfig.instance().isFeatureEnabled(AEFeature.SPAWN_PRESSES_IN_METEORITES)) {
            IBlockState block = Blocks.STONE.getDefaultState();
            int r = w.getWorld().rand.nextInt(betterMeteorite$sumWeight) + 1;

            for (Map.Entry<IBlockState, Integer> reward : betterMeteorite$skyStoneReward.entrySet()) {
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
    private void betterMeteorite$initReward(){
        String[] skyStoneReward = BMConfig.skyStoneReward;
        if (skyStoneReward.length == 0) {
            betterMeteorite$skyStoneReward = null;
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
            betterMeteorite$skyStoneReward.put(block,weight);
            betterMeteorite$sumWeight += weight;
        }
        if (betterMeteorite$skyStoneReward.isEmpty())betterMeteorite$skyStoneReward = null;


        String[] skyStone = BMConfig.skyStone;
        if (skyStone.length == 0) {
            betterMeteorite$skyStone = null;
            return;
        }
        int[] sourceStoneWeights = BMConfig.skyStoneWeight;
        int[] skyStoneWeight = new int[skyStone.length];

        if (sourceStoneWeights.length < skyStone.length) {
            System.arraycopy(sourceStoneWeights, 0, skyStoneWeight, 0, sourceStoneWeights.length);
            for (int i = sourceStoneWeights.length; i < skyStoneWeight.length; i++) {
                skyStoneWeight[i] = 1;
            }
        } else {
            skyStoneWeight = sourceStoneWeights;
        }

        for (int i = 0; i < skyStone.length; i++) {
            String blockName = skyStone[i];
            IBlockState block = Function.getBlockFromName(blockName);
            if (block == null)continue;
            int weight = skyStoneWeight[i];
            betterMeteorite$skyStone.put(block,weight);
            betterMeteorite$sumStoneWeight += weight;
            this.invalidSpawn.add(block.getBlock());
        }
        if (betterMeteorite$skyStone.isEmpty())betterMeteorite$skyStone = null;
    }

}
