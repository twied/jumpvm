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
 * WiMa Structure.
 */
public class StructureObject implements MemoryObject {
    /** Identifier. */
    private final String identifier;

    /** Arity. */
    private final int arity;

    /**
     * Create a new StructureObject.
     * 
     * @param identifier identifier
     * @param arity arity
     */
    public StructureObject(final String identifier, final int arity) {
        this.identifier = identifier;
        this.arity = arity;
    }

    /**
     * Returns the arity.
     * 
     * @return the arity
     */
    public final int getArity() {
        return arity;
    }

    @Override
    public final String getDisplayDescription() {
        return getDisplayValue();
    }

    @Override
    public final String getDisplayHoverText() {
        return getDisplayValue();
    }

    @Override
    public final String getDisplayType() {
        return " Σ ";
    }

    @Override
    public final String getDisplayValue() {
        return identifier + "/" + arity;
    }

    /**
     * Returns the identifier.
     * 
     * @return the identifier
     */
    public final String getIdentifier() {
        return identifier;
    }
}
