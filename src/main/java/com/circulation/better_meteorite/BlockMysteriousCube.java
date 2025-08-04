package com.circulation.better_meteorite;

import appeng.api.AEApi;
import appeng.api.definitions.IMaterials;
import appeng.core.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BlockMysteriousCube extends Block {

    public static BlockMysteriousCube mysteriousCube = new BlockMysteriousCube();
    public static ItemBlock mysteriousCubeItem;
    public static final Set<ItemStack> press = new HashSet<>();
    public static final IMaterials aeItems = AEApi.instance().definitions().materials();

    public BlockMysteriousCube() {
        super(Material.IRON);
        this.setResistance(1000);
        this.setHardness(10);
        this.setCreativeTab(CreativeTab.instance);
        this.setRegistryName(new ResourceLocation(BetterMeteorite.MOD_ID, "mysterious_cube"));
        this.setTranslationKey(BetterMeteorite.MOD_ID + '.' + "mysterious_cube");
    }

    @Override
    public void getDrops(@NotNull NonNullList<ItemStack> drops, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune) {
        if (press.isEmpty()) {
            aeItems.calcProcessorPress().maybeStack(1).ifPresent(press::add);
            aeItems.engProcessorPress().maybeStack(1).ifPresent(press::add);
            aeItems.logicProcessorPress().maybeStack(1).ifPresent(press::add);
            aeItems.siliconPress().maybeStack(1).ifPresent(press::add);
        }
        drops.addAll(press);
    }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
