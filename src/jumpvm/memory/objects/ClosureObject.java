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
 * Unfinished calculation, "Closure".
 */
public class ClosureObject implements MemoryObject {
    /** Pointer to calculation code. */
    private final int cp;

    /** Pointer to calculation globals. */
    private final int gp;

    /** This closure's name. */
    private final String name;

    /**
     * Create a new ClosureObject.
     * 
     * @param cp pointer to calculation code
     * @param gp pointer to calculation globals
     * @param name calculation name
     */
    public ClosureObject(final int cp, final int gp, final String name) {
        this.cp = cp;
        this.gp = gp;
        this.name = name;
    }

    /**
     * Returns the calculation code pointer.
     * 
     * @return the calculation code pointer
     */
    public final int getCp() {
        return cp;
    }

    @Override
    public final String getDisplayDescription() {
        return "→" + name;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Deffered calculation to create the object " + name;
    }

    @Override
    public final String getDisplayType() {
        return "clo";
    }

    @Override
    public final String getDisplayValue() {
        return "cp = " + cp + ", gp = " + gp;
    }

    /**
     * Returns the calculation variables pointer.
     * 
     * @return the calculation variables pointer
     */
    public final int getGp() {
        return gp;
    }
}
