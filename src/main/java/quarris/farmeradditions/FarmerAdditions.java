package quarris.farmeradditions;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import quarris.farmeradditions.behaviors.PokecubeBerryBehavior;
import quarris.farmeradditions.behaviors.PokecubeBerryTreeBehavior;


@Mod(modid = FarmerAdditions.MODID, name = FarmerAdditions.NAME, version = FarmerAdditions.VERSION, dependencies = "required-after:actuallyadditions;")
public class FarmerAdditions
{
    public static final String MODID = "farmeradditions";
    public static final String NAME = "AA Farmer Compat";
    public static final String VERSION = "1.0.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        Compat.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        if (Compat.isPokecubeMobsLoaded) {
            if (FAConfig.pokecubeBerryCompat) {
                ActuallyAdditionsAPI.addFarmerBehavior(new PokecubeBerryBehavior());
            }
            if (FAConfig.pokecubeBerryTreeCompat) {
                ActuallyAdditionsAPI.addFarmerBehavior(new PokecubeBerryTreeBehavior());
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent e) {
        if (e.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }
}
