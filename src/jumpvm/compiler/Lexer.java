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

import java.io.File;
import java.io.FileNotFoundException;

/**
 * JumpVM lexer.
 * 
 * @param <T> Token class
 */
public abstract class Lexer<T extends Token> {
    /** Underlying {@link LocatedReader}. */
    private final LocatedReader reader;

    /** Last character read. */
    private int lastChar;

    /** Begin of last token read. */
    private Location location;

    /** Textual representation of last token read. */
    private String identifier;

    /**
     * Create a new lexer.
     * 
     * @param file source
     * @throws FileNotFoundException on failure
     */
    public Lexer(final File file) throws FileNotFoundException {
        this(new LocatedReader(file));
    }

    /**
     * Create a new lexer.
     * 
     * @param reader source
     */
    public Lexer(final LocatedReader reader) {
        this.reader = reader;
        this.lastChar = ' ';
    }

    /**
     * Returns identifier of last IDENTIFIER token. May be {@code null}.
     * 
     * @return identifier of last IDENTIFIER token
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the last character read.
     * 
     * @return the last character read
     */
    protected final int getLastChar() {
        return lastChar;
    }

    /**
     * Returns the begin of the last token read.
     * 
     * @return the begin of the last token read
     */
    public final Location getLocation() {
        return location;
    }

    /**
     * Advances the lexer one character and returns the character read.
     * 
     * @return the character read.
     */
    protected final int getNextChar() {
        lastChar = reader.read();
        return lastChar;
    }

    /**
     * Marks the beginning of a new token.
     */
    protected final void markBegin() {
        location = reader.getLocation();
    }

    /**
     * Returns the next token.
     * 
     * @return the next token
     */
    public abstract T nextToken();

    /**
     * Sets the current identifier string to {@code identifier}.
     * 
     * @param identifier current identifier string
     */
    public final void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }
}
