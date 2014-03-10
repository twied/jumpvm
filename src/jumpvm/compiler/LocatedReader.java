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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Located Reader.
 * 
 * This {@link Reader} knows about its {@link Location} in the stream.
 */
public class LocatedReader extends Reader {
    /** Underlying {@link Reader}. */
    private final Reader reader;

    /** Resource name. */
    private final String name;

    /** Location of last character read. */
    private Location location;

    /** Current line number. */
    private int line;

    /** Current column number. */
    private int column;

    /** Current character number. */
    private int character;

    /**
     * Create a new LocatedReader.
     * 
     * @param file file to read
     * @throws FileNotFoundException on failure
     */
    public LocatedReader(final File file) throws FileNotFoundException {
        this(new FileReader(file), file.getName());
    }

    /**
     * Create a new LocatedReader.
     * 
     * @param reader stream to read
     * @param name name of the stream
     */
    public LocatedReader(final Reader reader, final String name) {
        this.reader = reader;
        this.name = name;
        this.line = 1;
        this.column = 1;
        this.character = 0;
    }

    @Override
    public final void close() throws IOException {
        reader.close();
    }

    /**
     * Returns the location of the <b>last</b> character.
     * 
     * @return the location of the last character
     */
    public final Location getLocation() {
        return location;
    }

    @Override
    public final int read() {
        location = new Location(name, line, column, character);
        try {
            final char[] c = new char[1];
            if (read(c, 0, 1) == -1) {
                return -1;
            } else {
                return c[0];
            }
        } catch (final IOException e) {
            return -1;
        }
    }

    @Override
    public final int read(final char[] cbuf, final int off, final int len) throws IOException {
        final int retval = reader.read(cbuf, off, len);
        for (int i = 0; i < retval; ++i) {
            ++character;
            if (cbuf[i] == '\n') {
                ++line;
                column = 1;
            } else {
                ++column;
            }
        }

        return retval;
    }
}
