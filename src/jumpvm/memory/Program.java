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

import java.util.List;

import jumpvm.code.Instruction;

/**
 * JumpVM program memory.
 */
public class Program extends Memory<Instruction> {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new program.
     */
    public Program() {
        this("Program");
    }

    /**
     * Create a new program.
     * 
     * @param name This memory's name
     */
    public Program(final String name) {
        super(name);
    }

    /**
     * Append an {@link Instruction} to the program.
     * 
     * @param instruction Instruction to append
     */
    public final void add(final Instruction instruction) {
        setElementAt(getSize(), instruction);
    }

    /**
     * Append a list of {@link Instruction}s to the program.
     * 
     * @param instructions Instructions to append
     */
    public final void add(final List<Instruction> instructions) {
        for (final Instruction instruction : instructions) {
            add(instruction);
        }
    }

    /**
     * Append another Program to this program.
     * 
     * @param program Program to append
     */
    public final void add(final Program program) {
        for (int i = 0; i < program.getSize(); ++i) {
            add(program.getElementAt(i));
        }
    }
}
