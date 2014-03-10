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

package jumpvm.memory;

/**
 * Named pointer to program memory.
 */
public class Label {
    /** Name. */
    private final String name;

    /** Address. */
    private int address;

    /**
     * Create a new Label.
     * 
     * @param name name
     */
    public Label(final String name) {
        this.name = name;
        this.address = -1;
    }

    /**
     * Returns the address.
     * 
     * @return the address
     */
    public final int getAddress() {
        return address;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the address.
     * 
     * @param address new address
     */
    public final void setAddress(final int address) {
        this.address = address;
    }

    @Override
    public final String toString() {
        return String.valueOf(address) + " (" + name + ")";
    }
}
