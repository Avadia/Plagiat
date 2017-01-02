package net.samagames.plagiat.modules.dimensions;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

/**
 * Internal class to handle breaking block for Dimensions module
 */
public class InternalBlockBreakEvent extends BlockEvent
{
    private static final HandlerList handlerList = new HandlerList();

    /**
     * Event constructor
     *
     * @param block Broken block
     */
    public InternalBlockBreakEvent(Block block)
    {
        super(block);
    }

    /**
     * Get this event handler list
     * Basic implement
     *
     * @return HandlerList instance
     */
    @Override
    public HandlerList getHandlers()
    {
        return InternalBlockBreakEvent.handlerList;
    }

    /**
     * Get this event handler list
     *
     * @return HandlerList instance
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList()
    {
        return InternalBlockBreakEvent.handlerList;
    }
}
