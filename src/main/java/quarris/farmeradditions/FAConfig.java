package quarris.farmeradditions;

import net.minecraftforge.common.config.Config;

@Config(modid = FarmerAdditions.MODID)
public class FAConfig {

    @Config.Name("Compatibility for Pokecube Berries")
    public static boolean pokecubeBerryCompat = true;

    @Config.Name("Compatibility for Pokecube Berry Trees")
    public static boolean pokecubeBerryTreeCompat = true;
}
