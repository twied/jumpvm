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

package jumpvm.vm;

import jumpvm.memory.Register;
import jumpvm.memory.Stack;

/**
 * BfMachine - an esoteric language similar to Brainfuck.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Brainfuck">http://en.wikipedia.org/wiki/Brainfuck</a>
 */
public class BfMa extends JumpVM {
    /** "Infinite" memory band. */
    private final Stack stack;

    /** Head position. */
    private final Register cellPointer;

    /** Create a new BfMa VM. */
    public BfMa() {
        this.cellPointer = new Register("CP", "Cell Pointer", 0);
        addDisplayRegister(cellPointer);

        this.stack = new Stack(new Register("SP", "Stack Pointer", -1));
        addDisplayMemory(stack);
    }

    /**
     * Returns the head position.
     * 
     * @return the head position
     */
    public final Register getCellPointer() {
        return cellPointer;
    }

    /**
     * Returns the infinite memory band.
     * 
     * @return the infinite memory band
     */
    public final Stack getStack() {
        return stack;
    }
}
