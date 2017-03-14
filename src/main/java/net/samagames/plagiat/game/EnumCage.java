package net.samagames.plagiat.game;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.security.SecureRandom;
import java.util.Random;

/**
 * List of all cages
 */
enum EnumCage
{
    GLASS(Material.GLASS, (byte)0, 0),
    VOID(Material.GLASS, (byte)15, 1),
    NICOLAS(Material.MOB_SPAWNER, (byte)0, 2),
    SLIME(Material.SLIME_BLOCK, (byte)0, 3),
    SHEEP(Material.WOOL, (byte)-1, 4),
    OBSIDIAN(Material.OBSIDIAN, (byte)0, 5),
    RAINBOW(Material.STAINED_GLASS, (byte)-1, 6);

    private static Random random = new SecureRandom();
    private Material material;
    private byte data;
    private int dbId;

    /**
     * EnumCage constructor
     *
     * @param material Material of the cage
     * @param data Metadata as byte
     * @param dbId Database ID of the cage
     */
    EnumCage(Material material, byte data, int dbId)
    {
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
    public void set(Block block)
    {
        block.setType(this.material);
        block.setData(this.data == -1 ? (byte)EnumCage.random.nextInt(16) : this.data);
    }

    /**
     * Get this cage database id
     *
     * @return ID as int
     */
    public int getDatabaseId()
    {
        return this.dbId;
    }
}
