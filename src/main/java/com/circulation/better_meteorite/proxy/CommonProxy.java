package com.circulation.better_meteorite.proxy;

import appeng.core.CreativeTab;
import com.circulation.better_meteorite.BetterMeteorite;
import com.circulation.better_meteorite.BlockMysteriousCube;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("MethodMayBeStatic")
public class CommonProxy {

    public CommonProxy() {
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(BlockMysteriousCube.mysteriousCube);
    }
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        BlockMysteriousCube.mysteriousCubeItem = new ItemBlock(BlockMysteriousCube.mysteriousCube);
        BlockMysteriousCube.mysteriousCubeItem.setRegistryName(new ResourceLocation(BetterMeteorite.MOD_ID, "mysterious_cube"));
        BlockMysteriousCube.mysteriousCubeItem.setTranslationKey(BetterMeteorite.MOD_ID + '.' + "mysterious_cube");
        BlockMysteriousCube.mysteriousCubeItem.setCreativeTab(CreativeTab.instance);
        event.getRegistry().register(BlockMysteriousCube.mysteriousCubeItem);
    }

    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init() {
    }

    public void postInit() {
    }
}
