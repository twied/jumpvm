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

package jumpvm.compiler;

/** Location in a stream. */
public class Location {
    /** Resource name. */
    private final String name;

    /** Line number. */
    private final int line;

    /** Column number. */
    private final int column;

    /** Overall character number. */
    private final int character;

    /**
     * Create a new Location.
     * 
     * @param name resource name
     * @param line line number
     * @param column column name
     * @param character overall character number
     */
    public Location(final String name, final int line, final int column, final int character) {
        this.name = name;
        this.line = line;
        this.column = column;
        this.character = character;
    }

    /**
     * Returns the overall character number.
     * 
     * @return the overall character number
     */
    public final int getCharacter() {
        return character;
    }

    /**
     * Returns the column number.
     * 
     * @return the column number
     */
    public final int getColumn() {
        return column;
    }

    /**
     * Returns the line number.
     * 
     * @return the line number
     */
    public final int getLine() {
        return line;
    }

    /**
     * Returns the resource name.
     * 
     * @return the resource name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the location formatted as e.g. gcc does in its error messages.
     * 
     * @return the location formatted as "filename:line:column"
     */
    @Override
    public final String toString() {
        return name + ":" + line + ":" + column;
    }
}
