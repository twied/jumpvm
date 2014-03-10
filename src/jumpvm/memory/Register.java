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

import java.util.HashMap;
import java.util.Observable;

import jumpvm.memory.objects.StackObject;

/**
 * JumpVM register.
 */
public class Register extends Observable {
    /** Short name or abbreviation. */
    private final String shortName;

    /** Long name. */
    private final String longName;

    /** Default value. */
    private final int defaultValue;

    /** Current value. */
    private int value;

    /** Displayable value map: Values that will be displayed with special string instead of the integer value. */
    private final HashMap<Integer, String> displayValueMap;

    /**
     * Create a new Register.
     * 
     * @param shortName short name or abbreviation
     * @param longName long name
     * @param defaultValue default value
     */
    public Register(final String shortName, final String longName, final int defaultValue) {
        this(shortName, longName, defaultValue, new HashMap<Integer, String>());
    }

    /**
     * Create a new Register.
     * 
     * @param shortName short name or abbreviation
     * @param longName long name
     * @param defaultValue default value
     * @param displayValueMap maps how values shall be displayed
     */
    public Register(final String shortName, final String longName, final int defaultValue, final HashMap<Integer, String> displayValueMap) {
        this.shortName = shortName;
        this.longName = longName;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.displayValueMap = displayValueMap;
    }

    /**
     * Decrement current value.
     */
    public final void decrement() {
        --value;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the default value.
     * 
     * @return the default value
     */
    public final int getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the displayable value of this register. This may be a string like "OK" instead of "1" etc.
     * 
     * @return the displayable value of this register
     */
    public final String getDisplayValue() {
        final String result = displayValueMap.get(value);
        if (result == null) {
            return String.valueOf(value);
        } else {
            return result;
        }
    }

    /**
     * Returns the long name.
     * 
     * @return the long name
     */
    public final String getLongName() {
        return longName;
    }

    /**
     * Returns the short name.
     * 
     * @return the short name
     */
    public final String getShortName() {
        return shortName;
    }

    /**
     * Returns the current value.
     * 
     * @return the current value
     */
    public final int getValue() {
        return value;
    }

    /**
     * Increment current value.
     */
    public final void increment() {
        ++value;
        setChanged();
        notifyObservers();
    }

    /**
     * Resets the register to its default value.
     */
    public final void reset() {
        setValue(defaultValue);
    }

    /**
     * Sets the current value.
     * 
     * @param value new value
     */
    public final void setValue(final int value) {
        this.value = value;
        setChanged();
        notifyObservers();
    }

    /**
     * Alias for {@link #setValue(int)}.
     * 
     * @param value new value
     */
    public final void setValue(final Label value) {
        setValue(value.getAddress());
    }

    /**
     * Alias for {@link #setValue(int)}.
     * 
     * @param value new value
     */
    public final void setValue(final Register value) {
        setValue(value.getValue());
    }

    /**
     * Alias for {@link #setValue(int)}.
     * 
     * @param value new value
     */
    public final void setValue(final StackObject value) {
        setValue(value.getIntValue());
    }
}
