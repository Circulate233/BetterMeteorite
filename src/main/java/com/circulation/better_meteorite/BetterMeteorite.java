package com.circulation.better_meteorite;

import com.circulation.better_meteorite.proxy.CommonProxy;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BetterMeteorite.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
        dependencies = "required-after:mixinbooter@[8.0,);" +
                       "required-after:appliedenergistics2@[v0.56.7,);"
)
public class BetterMeteorite {
    public static final String MOD_ID = "better_meteorite";
    public static final String CLIENT_PROXY = "com.circulation.better_meteorite.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.circulation.better_meteorite.proxy.CommonProxy";

    public static final SimpleNetworkWrapper NET_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy = null;

    @Mod.Instance(MOD_ID)
    public static BetterMeteorite instance = null;
    public static LogWrapper logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

}
