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
 * Typed object in memory.
 */
public interface MemoryObject {
    /**
     * Returns the description text.
     * 
     * @return the description text
     */
    String getDisplayDescription();

    /**
     * Returns the mouse-over tool tip text.
     * 
     * @return the mouse-over tool tip text
     */
    String getDisplayHoverText();

    /**
     * Returns the displayable type of this object.
     * 
     * @return the displayable type of this object
     */
    String getDisplayType();

    /**
     * Returns the displayable value of this object.
     * 
     * @return the displayable value of this object
     */
    String getDisplayValue();
}
