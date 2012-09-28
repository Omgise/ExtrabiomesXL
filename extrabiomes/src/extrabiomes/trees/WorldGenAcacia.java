/**
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license
 * located in /MMPL-1.0.txt
 */

package extrabiomes.trees;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import extrabiomes.plugin.trees.BlockCustomSapling;

public class WorldGenAcacia extends WorldGenerator {

	private static Block	trunkBlock		= Block.wood;
	private static int		trunkMetadata	= 0;
	private static Block	leavesBlock		= Block.leaves;
	private static int		leavesMetadata	= 0;

	public static void setLeavesBlock(Block block, int metadata) {
		WorldGenAcacia.leavesBlock = block;
		WorldGenAcacia.leavesMetadata = metadata;
	}

	public static void setTrunkBlock(Block block, int metadata) {
		WorldGenAcacia.trunkBlock = block;
		WorldGenAcacia.trunkMetadata = metadata;
	}

	public WorldGenAcacia(final boolean doNotify) {
		super(doNotify);
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y,
			int z)
	{
		final int height = rand.nextInt(4) + 6;
		boolean canGrow = true;

		if (y < 1 || y + height + 1 > 256) return false;

		for (int y1 = y; y1 <= y + 1 + height; y1++) {
			byte clearance = 1;

			if (y1 == y) clearance = 0;

			if (y1 >= y + 1 + height - 2) clearance = 2;

			for (int x1 = x - clearance; x1 <= x + clearance && canGrow; x1++)
				for (int z1 = z - clearance; z1 <= z + clearance
						&& canGrow; z1++)
					if (y1 >= 0 && y1 < 256) {
						final int id = world.getBlockId(x1, y1, z1);

						if (Block.blocksList[id] != null
								&& !Block.blocksList[id].isLeaves(
										world, x1, y1, z1)
								&& id != Block.grass.blockID
								&& id != Block.dirt.blockID
								&& !Block.blocksList[id].isWood(world,
										x1, y1, z1)) canGrow = false;

					} else
						canGrow = false;
		}

		if (!canGrow) return false;

		if (!BlockCustomSapling.isValidSoilID(Integer.valueOf(world
				.getBlockId(x, y - 1, z))) || y >= 256 - height - 1)
			return false;

		world.setBlock(x, y - 1, z, Block.dirt.blockID);
		final byte canopyHeight = 3;
		final int minCanopyRadius = 0;

		for (int y1 = y - canopyHeight + height; y1 <= y + height; y1++)
		{
			final int distanceFromTop = y1 - (y + height);
			final int canopyRadius = minCanopyRadius + 1
					- distanceFromTop;

			for (int x1 = x - canopyRadius; x1 <= x + canopyRadius; x1++)
			{
				final int xOnRadius = x1 - x;

				for (int z1 = z - canopyRadius; z1 <= z + canopyRadius; z1++)
				{
					final int zOnRadius = z1 - z;

					final Block block = Block.blocksList[world
							.getBlockId(x1, y1, z1)];

					if ((Math.abs(xOnRadius) != canopyRadius
							|| Math.abs(zOnRadius) != canopyRadius || rand
							.nextInt(2) != 0 && distanceFromTop != 0)
							&& (block == null || block
									.canBeReplacedByLeaves(world, x1,
											y1, z1)))
						setBlockAndMetadata(world, x1, y1, z1,
								leavesBlock.blockID, leavesMetadata);
				}
			}
		}

		for (int y1 = 0; y1 < height; y1++) {
			final int id = world.getBlockId(x, y + y1, z);

			if (Block.blocksList[id] != null
					&& !Block.blocksList[id].isLeaves(world, x, y + y1,
							z)) continue;

			setBlockAndMetadata(world, x, y + y1, z,
					trunkBlock.blockID, trunkMetadata);

		}
		return true;
	}

}
