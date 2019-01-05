package quarris.farmeradditions;

import net.minecraftforge.fml.common.Loader;

public class Compat {

    public static boolean isPokecubeMobsLoaded;

    public static void preInit() {
        isPokecubeMobsLoaded = Loader.isModLoaded("pokecube_mobs");
    }
}
