package com.circulation.beeter_meteorite;

import appeng.api.AEApi;
import appeng.api.definitions.IMaterials;
import appeng.core.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

public class BlockMysteriousCube extends Block {

    public static BlockMysteriousCube mysteriousCube = new BlockMysteriousCube();
    public static ItemBlock mysteriousCubeItem;
    public static IMaterials aeItems = AEApi.instance().definitions().materials();

    public BlockMysteriousCube() {
        super(Material.ROCK);
        this.setResistance(10.0F);
        this.setHardness(10);
        this.setCreativeTab(CreativeTab.BUILDING_BLOCKS);
        this.setRegistryName(new ResourceLocation(BeeterMeteorite.MOD_ID, "mysterious_cube"));
        this.setTranslationKey(BeeterMeteorite.MOD_ID + '.' + "mysterious_cube");
    }

    @Override
    public void getDrops(@NotNull NonNullList<ItemStack> drops, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune)
    {
        aeItems.calcProcessorPress().maybeStack(1).ifPresent(drops::add);
        aeItems.engProcessorPress().maybeStack(1).ifPresent(drops::add);
        aeItems.logicProcessorPress().maybeStack(1).ifPresent(drops::add);
        aeItems.siliconPress().maybeStack(1).ifPresent(drops::add);
    }

}
