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
 * Function object.
 */
public class FunValObject implements MemoryObject {
    /** Function code pointer. */
    private final int cf;

    /** Function arguments pointer. */
    private final int fap;

    /** Function globals pointer. */
    private final int fgp;

    /** Function name. */
    private final String name;

    /**
     * Create a new FunValObject.
     * 
     * @param cf function code pointer
     * @param fap function arguments pointer
     * @param fgp function globals pointer
     * @param name function name
     */
    public FunValObject(final int cf, final int fap, final int fgp, final String name) {
        this.cf = cf;
        this.fap = fap;
        this.fgp = fgp;
        this.name = name;
    }

    /**
     * Returns the function code pointer.
     * 
     * @return the function code pointer
     */
    public final int getCf() {
        return cf;
    }

    @Override
    public final String getDisplayDescription() {
        return name;
    }

    @Override
    public final String getDisplayHoverText() {
        return null;
    }

    @Override
    public final String getDisplayType() {
        return "fun";
    }

    @Override
    public final String getDisplayValue() {
        return "cf = " + cf + ", fap = " + fap + ", fgp = " + fgp;
    }

    /**
     * Returns the function arguments pointer.
     * 
     * @return the function arguments pointer
     */
    public final int getFap() {
        return fap;
    }

    /**
     * Returns the function globals pointer.
     * 
     * @return the function globals pointer
     */
    public final int getFgp() {
        return fgp;
    }
}
