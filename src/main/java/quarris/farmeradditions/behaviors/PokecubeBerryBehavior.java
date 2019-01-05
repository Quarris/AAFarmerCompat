package quarris.farmeradditions.behaviors;

import com.sun.xml.internal.ws.spi.db.PropertyGetter;
import de.ellpeck.actuallyadditions.api.farmer.FarmerResult;
import de.ellpeck.actuallyadditions.api.farmer.IFarmerBehavior;
import de.ellpeck.actuallyadditions.api.internal.IFarmer;
import de.ellpeck.actuallyadditions.mod.blocks.BlockFarmer;
import de.ellpeck.actuallyadditions.mod.misc.apiimpl.farmer.DefaultFarmerBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import pokecube.core.blocks.berries.BlockBerryCrop;
import pokecube.core.blocks.berries.BlockBerryFruit;
import pokecube.core.blocks.berries.TileEntityBerries;
import pokecube.core.items.berries.BerryManager;
import pokecube.core.items.berries.ItemBerry;

public class PokecubeBerryBehavior implements IFarmerBehavior {

    private void prepareForPlant(World world, BlockPos pos, IFarmer farmer, int use) {
        BlockPos farmland = pos.down();
        Block farmlandBlock = world.getBlockState(farmland).getBlock();
        if (farmlandBlock instanceof BlockDirt || farmlandBlock instanceof BlockGrass) {
            world.setBlockToAir(pos);
            DefaultFarmerBehavior.useHoeAt(world, farmland);
            world.playSound(null, farmland, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            farmer.extractEnergy(use);
        }
    }

    @Override
    public FarmerResult tryPlantSeed(ItemStack seed, World world, BlockPos pos, IFarmer farmer) {
        int use = 350;
        if (farmer.getEnergy() >= use * 2) {
            if (seed.getItem() instanceof ItemBerry) {
                ItemBerry berry = (ItemBerry) seed.getItem();
                IBlockState plantState = berry.getPlant(world, pos).withProperty(BerryManager.type, BerryManager.berryNames.get(berry.index)).withProperty(BlockCrops.AGE, 2);
                Block block = plantState.getBlock();
                if(block.canPlaceBlockAt(world, pos)){
                    prepareForPlant(world, pos, farmer, use);
                    world.setBlockState(pos, BerryManager.berryCrop.getDefaultState(), 2);
                    TileEntityBerries tile = (TileEntityBerries) world.getTileEntity(pos);
                    tile.setBerryId(berry.index);

                    farmer.extractEnergy(use);
                    return FarmerResult.SUCCESS;
                }
            }
        }
        return FarmerResult.FAIL;
    }

    @Override
    public FarmerResult tryHarvestPlant(World world, BlockPos pos, IFarmer farmer) {
        int use = 250;
        if (farmer.getEnergy() >= use) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockBerryCrop) {
                BlockPos up = pos.up();
                IBlockState upState = world.getBlockState(up);
                if (upState.getBlock() instanceof BlockBerryFruit) {
                    NonNullList<ItemStack> drops = NonNullList.create();
                    upState.getBlock().getDrops(drops, world, up, upState, 0);

                    if (!drops.isEmpty()) {
                        if (farmer.canAddToOutput(drops)) {
                            world.playEvent(2001, up, Block.getStateId(upState));
                            world.setBlockToAir(up);

                            farmer.extractEnergy(use);
                            farmer.addToOutput(drops);
                            return FarmerResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return FarmerResult.FAIL;
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
