package net.samagames.plagiat.modules.dimensions;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

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
public class InternalBlockBreakEvent extends BlockEvent {
    private static final HandlerList handlerList = new HandlerList();

    /**
     * Event constructor
     *
     * @param block Broken block
     */
    public InternalBlockBreakEvent(Block block) {
        super(block);
    }

    /**
     * Get this event handler list
     *
     * @return HandlerList instance
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return InternalBlockBreakEvent.handlerList;
    }

    /**
     * Get this event handler list
     * Basic implement
     *
     * @return HandlerList instance
     */
    @Override
    public HandlerList getHandlers() {
        return InternalBlockBreakEvent.handlerList;
    }
}
