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

import java.util.ArrayList;

/**
 * MaMa Vector.
 */
public class VectorObject implements MemoryObject {
    /** Elements. */
    private final ArrayList<Integer> vector;

    /** Name. */
    private final String name;

    /**
     * Create a new {@link VectorObject}.
     * 
     * @param vector elements
     * @param name name
     */
    public VectorObject(final ArrayList<Integer> vector, final String name) {
        this.vector = vector;
        this.name = name;
    }

    @Override
    public final String getDisplayDescription() {
        return name;
    }

    @Override
    public final String getDisplayHoverText() {
        return name;
    }

    @Override
    public final String getDisplayType() {
        return "vec";
    }

    @Override
    public final String getDisplayValue() {
        return vector.toString();
    }

    /**
     * Returns the elements.
     * 
     * @return the elements
     */
    public final ArrayList<Integer> getVector() {
        return vector;
    }
}
