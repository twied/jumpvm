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

import jumpvm.memory.Register;

/**
 * Basic integral value.
 */
public class BasicValueObject extends StackObject {
    /** Value. */
    private final int intValue;

    /** Short description of this values's meaning. */
    private final String descriptionShort;

    /** Long description of this values's meaning. */
    private final String descriptionLong;

    /**
     * Create a new BasicValueObject.
     * 
     * @param intValue value
     * @param descriptionShort short description of this value's meaning
     * @param descriptionLong long description of this value's meaning
     */
    public BasicValueObject(final int intValue, final String descriptionShort, final String descriptionLong) {
        this.intValue = intValue;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
    }

    /**
     * Create a new BasicValueObject from a given register.
     * 
     * @param register register
     */
    public BasicValueObject(final Register register) {
        this(register.getValue(), register.getShortName(), register.getLongName());
    }

    /**
     * Create a new BasicValueObject from a given register -- but with a different value.
     * 
     * @param register register
     * @param value value
     */
    public BasicValueObject(final Register register, final int value) {
        this(value, register.getShortName(), register.getLongName());
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
        return " # ";
    }

    @Override
    public final int getIntValue() {
        return intValue;
    }
}
