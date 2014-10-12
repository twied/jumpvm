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

package jumpvm.compiler.pama;

import jumpvm.memory.Label;

/** A position in the PaMaCompiler's address environment. */
public class Position {
    /** (Symbolic) address. */
    private final Label address;

    /** Stacking depth. */
    private final int depth;

    /**
     * Create a new Position.
     * 
     * @param address address
     * @param depth stacking depth
     */
    public Position(final int address, final int depth) {
        this(new Label(null, address), depth);
    }

    /**
     * Create a new Position.
     * 
     * @param address symbolic address
     * @param depth stacking depth
     */
    public Position(final Label address, final int depth) {
        this.address = address;
        this.depth = depth;
    }

    /**
     * Returns the symbolic address.
     * 
     * @return the symbolic address
     */
    public final Label getAddress() {
        return address;
    }

    /**
     * Returns the stacking depth.
     * 
     * @return the stacking depth
     */
    public final int getDepth() {
        return depth;
    }
}
