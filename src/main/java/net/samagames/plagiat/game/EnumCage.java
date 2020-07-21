package net.samagames.plagiat.game;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.security.SecureRandom;
import java.util.Random;

/*
 * This file is part of Plagiat.
 *
 * Plagiat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plagiat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Plagiat.  If not, see <http://www.gnu.org/licenses/>.
 */
enum EnumCage {
    GLASS(Material.GLASS, (byte) 0, 0),
    VOID(Material.GLASS, (byte) 15, 1),
    NICOLAS(Material.MOB_SPAWNER, (byte) 0, 2),
    SLIME(Material.SLIME_BLOCK, (byte) 0, 3),
    SHEEP(Material.WOOL, (byte) -1, 4),
    OBSIDIAN(Material.OBSIDIAN, (byte) 0, 5),
    RAINBOW(Material.STAINED_GLASS, (byte) -1, 6),
    MOSCOU(Material.STAINED_GLASS, (byte) 14, 7),
    ARITCHE(Material.IRON_FENCE, (byte) 0, 8);

    private static final Random random = new SecureRandom();
    private final Material material;
    private final byte data;
    private final int dbId;

    /**
     * EnumCage constructor
     *
     * @param material Material of the cage
     * @param data     Metadata as byte
     * @param dbId     Database ID of the cage
     */
    EnumCage(Material material, byte data, int dbId) {
        this.material = material;
        this.data = data;
        this.dbId = dbId;
    }

    /**
     * Set a block to the cage material and metadata
     *
     * @param block Block to be modified
     */
    @SuppressWarnings("deprecation")
    public void set(Block block) {
        block.setType(this.material);
        block.setData(this.data == -1 ? (byte) EnumCage.random.nextInt(16) : this.data);
    }

    /**
     * Get this cage database id
     *
     * @return ID as int
     */
    public int getDatabaseId() {
        return this.dbId;
    }
}
