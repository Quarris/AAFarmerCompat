package quarris.farmeradditions.behaviors;

import de.ellpeck.actuallyadditions.api.farmer.FarmerResult;
import de.ellpeck.actuallyadditions.api.farmer.IFarmerBehavior;
import de.ellpeck.actuallyadditions.api.internal.IFarmer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pokecube.core.blocks.berries.BlockBerryFruit;

public class PokecubeBerryTreeBehavior implements IFarmerBehavior {

    @Override
    public FarmerResult tryPlantSeed(ItemStack seed, World world, BlockPos pos, IFarmer farmer) {
        return FarmerResult.FAIL;
    }

    @Override
    public FarmerResult tryHarvestPlant(World world, BlockPos pos, IFarmer farmer) {
        int use = 350;
        if (farmer.getEnergy() >= use) {
            for (int i = 0; i < 5; i++) {
                BlockPos pos1 = pos.up(i);
                IBlockState state = world.getBlockState(pos1);
                if (state.getBlock() instanceof BlockBerryFruit) {
                    NonNullList<ItemStack> drops = NonNullList.create();
                    state.getBlock().getDrops(drops, world, pos1, state, 0);

                    if (!drops.isEmpty()) {
                        if (farmer.canAddToOutput(drops)) {
                            world.playEvent(2001, pos1, Block.getStateId(state));
                            world.setBlockToAir(pos1);

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
        return 0;
    }
}
