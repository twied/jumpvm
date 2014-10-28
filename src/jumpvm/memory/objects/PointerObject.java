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

import jumpvm.memory.Label;

/**
 * A typed Pointer.
 */
public class PointerObject extends StackObject {
    /** Type of the pointer. */
    public static enum Type {
        /** Pointer to program memory. */
        POINTER_PROGRAM,

        /** Pointer to stack memory. */
        POINTER_STACK,

        /** Pointer to heap memory. */
        POINTER_HEAP,

        /** Nil pointer. */
        POINTER_NIL
    }

    /** Address. */
    private final int intValue;

    /** Pointer type. */
    private final Type type;

    /** Short description of this pointer's meaning. */
    private final String descriptionShort;

    /** Long description of this pointer's meaning. */
    private final String descriptionLong;

    /**
     * Create a new PointerObject.
     * 
     * @param intValue address
     * @param type pointer type
     * @param descriptionShort short description of this pointer's meaning
     * @param descriptionLong long description of this pointer's meaning
     */
    public PointerObject(final int intValue, final Type type, final String descriptionShort, final String descriptionLong) {
        this.intValue = intValue;
        this.type = type;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
    }

    /**
     * Create a new PointerObject that points to the given label in PROGRAM memory.
     * 
     * @param label address
     */
    public PointerObject(final Label label) {
        this(label.getAddress(), Type.POINTER_PROGRAM, "→" + label.getName(), "Pointer to " + label.getName());
    }

    @Override
    public final String getDisplayDescription() {
        return descriptionShort;
    }

    @Override
    public final String getDisplayHoverText() {
        return descriptionLong;
    }

    @Override
    public final String getDisplayType() {
        switch (type) {
        case POINTER_HEAP:
            return "→H ";
        case POINTER_NIL:
            return " ↛ ";
        case POINTER_PROGRAM:
            return "→P ";
        case POINTER_STACK:
            return "→S ";
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public final int getIntValue() {
        return intValue;
    }

    /**
     * Returns the pointer type.
     * 
     * @return the pointer type
     */
    public final Type getType() {
        return type;
    }
}
