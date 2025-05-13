package com.circulation.beeter_meteorite;

import net.minecraftforge.common.config.Config;

@Config(modid = BeeterMeteorite.MOD_ID)
public class BMConfig {
    @Config.Comment({"Give the meteorite compass a new target"})
    @Config.Name("MeteoriteCompassTarget")
    @Config.RequiresMcRestart
    public static String[] MeteoriteCompassTarget = {BeeterMeteorite.MOD_ID + ":mysterious_cube:0"};

    @Config.Comment({"Replace the Sky Stone Chest,modid:id:meta"})
    @Config.Name("skyStoneReward")
    @Config.RequiresMcRestart
    public static String[] skyStoneReward = {BeeterMeteorite.MOD_ID + ":mysterious_cube:0"};

    @Config.Comment({"Each entry in skyStoneReward corresponds to a weight value. If unspecified, the default is 1."})
    @Config.Name("skyStoneRewardWeight")
    @Config.RequiresMcRestart
    public static int[] skyStoneRewardWeight = {1};

    @Config.Comment({"Replace the composition of meteorites,modid:id:meta"})
    @Config.Name("skyStone")
    @Config.RequiresMcRestart
    public static String[] skyStone = {"appliedenergistics2:sky_stone_block:0"};

    @Config.Comment({"Each entry in skyStone corresponds to a weight value. If unspecified, the default is 1."})
    @Config.Name("skyStoneWeight")
    @Config.RequiresMcRestart
    public static int[] skyStoneWeight = {1};
}
