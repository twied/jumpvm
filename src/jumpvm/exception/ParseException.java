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

package jumpvm.exception;

import jumpvm.compiler.Location;
import jumpvm.compiler.Token;

/**
 * Exception indicating failure to parse the source code.
 */
public class ParseException extends Exception {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Location of the offending token. */
    private final Location location;

    /**
     * Create a new ParseException.
     * 
     * @param location location of the failure
     * @param cause the cause for the exception
     */
    public ParseException(final Location location, final RuntimeException cause) {
        super(cause);
        this.location = location;
    }

    /**
     * Create a new ParseException.
     * 
     * @param location location of the unexpected token
     * @param expected what was expected
     * @param found what was encountered
     */
    public ParseException(final Location location, final String expected, final String found) {
        super("Unexpected token: " + found + ". Expected: " + expected + " instead.");
        this.location = location;
    }

    /**
     * Create a new ParseException.
     * 
     * @param location location of the unexpected token
     * @param expected what was expected
     * @param found what was encountered
     */
    public ParseException(final Location location, final Token expected, final String found) {
        this(location, expected.getName(), found);
    }

    /**
     * Create a new ParseException.
     * 
     * @param location location of the unexpected token
     * @param expected what was expected
     * @param found what was encountered
     */
    public ParseException(final Location location, final Token expected, final Token found) {
        this(location, expected.getName(), found.getName());
    }

    /**
     * Returns the location of the offending token.
     * 
     * @return the location of the offending token
     */
    public final Location getLocation() {
        return location;
    }
}
