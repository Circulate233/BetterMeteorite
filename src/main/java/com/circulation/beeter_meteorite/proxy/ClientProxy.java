package com.circulation.beeter_meteorite.proxy;

import com.circulation.beeter_meteorite.BlockMysteriousCube;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@SuppressWarnings("MethodMayBeStatic")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ResourceLocation registryName = Objects.requireNonNull(BlockMysteriousCube.mysteriousCubeItem.getRegistryName());
        ModelBakery.registerItemVariants(BlockMysteriousCube.mysteriousCubeItem, registryName);
        ModelLoader.setCustomModelResourceLocation(BlockMysteriousCube.mysteriousCubeItem, 0, new ModelResourceLocation(registryName, "inventory"));
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

}