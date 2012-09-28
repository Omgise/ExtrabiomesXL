/**
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license
 * located in /MMPL-1.0.txt
 */

package extrabiomes.plugin.trees;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class BlockCustomLog extends BlockLog {

	protected BlockCustomLog(int blockID) {
		super(blockID);
		blockIndexInTexture = 97;
		setTextureFile("/extrabiomes/extrabiomes.png");
		setStepSound(soundWoodFootstep);
		setRequiresSelfNotify();
		setHardness(2.0F);
		setBurnProperties(blockID, 5, 5);
		setResistance(Block.wood.getExplosionResistance(null) * 5.0F);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		final int orientation = metadata & 12;
		final int type = metadata & 3;
		if (orientation == 0 && (side == 1 || side == 0)
				|| orientation == 4 && (side == 5 || side == 4)
				|| orientation == 8 && (side == 2 || side == 3))
			return blockIndexInTexture + 16 + type;
		return blockIndexInTexture + type;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockID,
			CreativeTabs par2CreativeTabs, List list)
	{
		for (final WoodType type : WoodType.values())
			list.add(new ItemStack(blockID, 1, type.metadata()));
	}

	@Override
	public int idDropped(int metadata, Random rand, int unused) {
		return blockID;
	}

}
