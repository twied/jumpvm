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

package jumpvm.compiler.mama;

/**
 * Position of a variable.
 * 
 * Can either be a global or local address.
 */
public class Position {
    /** Global / Local address. */
    public static enum Type {
        /** Local address. */
        LOC,

        /** Global address. */
        GLOB
    }

    /** Type of the address. */
    private final Type type;

    /** Address. */
    private final int position;

    /**
     * Create a new Position.
     * 
     * @param type type of the address
     * @param position address
     */
    public Position(final Type type, final int position) {
        this.type = type;
        this.position = position;
    }

    /**
     * Returns the address.
     * 
     * @return the address
     */
    public final int getPosition() {
        return position;
    }

    /**
     * Returns the type of the address.
     * 
     * @return the type of the address
     */
    public final Type getType() {
        return type;
    }

    @Override
    public final String toString() {
        return "Position [type=" + type + ", position=" + position + "]";
    }
}
