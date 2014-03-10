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

import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;

/**
 * MaMachine - a functional language similar to Haskell.
 */
public class MaMa extends JumpVM {
    /** Number of organizational cells in a stack frame. */
    public static final int FRAME_SIZE = 3;

    /** Stack pointer register. */
    private final Register stackPointer;

    /** Frame pointer register. */
    private final Register framePointer;

    /** Global pointer register. */
    private final Register globalPointer;

    /** Stack memory. */
    private final Stack stack;

    /** Heap memory. */
    private final Heap heap;

    /** Create a new MaMa VM. */
    public MaMa() {
        this.stackPointer = new Register("SP", "Stack Pointer", -1);
        this.framePointer = new Register("FP", "Frame Pointer", -1);
        this.globalPointer = new Register("GP", "Global Pointer", -1);
        addDisplayRegister(stackPointer);
        addDisplayRegister(framePointer);
        addDisplayRegister(globalPointer);

        this.stack = new Stack(stackPointer);
        this.heap = new Heap();
        addDisplayMemory(stack);
        addDisplayMemory(heap);
    }

    /**
     * Returns the frame pointer register.
     * 
     * @return the frame pointer register
     */
    public final Register getFramePointer() {
        return framePointer;
    }

    /**
     * Returns the global pointer register.
     * 
     * @return the global pointer register
     */
    public final Register getGlobalPointer() {
        return globalPointer;
    }

    /**
     * Returns the heap memory.
     * 
     * @return the heap memory
     */
    public final Heap getHeap() {
        return heap;
    }

    /**
     * Returns the stack memory.
     * 
     * @return the stack memory
     */
    public final Stack getStack() {
        return stack;
    }

    /**
     * Returns the stack pointer register.
     * 
     * @return the stack pointer register
     */
    public final Register getStackPointer() {
        return stackPointer;
    }
}
