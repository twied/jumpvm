/*
 * JumpVM: The Java Unified Multi Paradigm Virtual Machine.
 * Copyright (C) 2013 Tim Wiederhake
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package jumpvm.memory.objects;

/**
 * List link.
 */
public class ConsObject implements MemoryObject {
    /** Location of head element on the heap. */
    private final int hd;

    /** Location of tail element on the heap. */
    private final int tl;

    /**
     * Create a new ConsObject.
     * 
     * @param hd location of head element on the heap
     * @param tl location of tail element on the heap
     */
    public ConsObject(final int hd, final int tl) {
        this.hd = hd;
        this.tl = tl;
    }

    @Override
    public final String getDisplayDescription() {
        return "⚭";
    }

    @Override
    public final String getDisplayHoverText() {
        return "List link";
    }

    @Override
    public final String getDisplayType() {
        return "Cons";
    }

    @Override
    public final String getDisplayValue() {
        return "hd = " + hd + ", tl = " + tl;
    }

    /**
     * Returns the location of the head element.
     * 
     * @return the location of the head element
     */
    public final int getHd() {
        return hd;
    }

    /**
     * Returns the location of the tail element.
     * 
     * @return the location of the tail element
     */
    public final int getTl() {
        return tl;
    }
}
