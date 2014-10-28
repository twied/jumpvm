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
import jumpvm.memory.objects.StackObject;

/**
 * PaMachine - an imperative language similar to Pascal.
 */
public class PaMa extends JumpVM {
    /** Stack size. */
    public static final int STACKSIZE = 0x100;

    /** Offset of the return value in a frame. */
    public static final int OFFSET_RETVAL = 0;

    /** Offset of the static predecessor in a frame. */
    public static final int OFFSET_SPD = 1;

    /** Offset of the dynamic predecessor in a frame. */
    public static final int OFFSET_DPD = 2;

    /** Offset of the old extreme pointer value in a frame. */
    public static final int OFFSET_EP = 3;

    /** Offset of the return address in a frame. */
    public static final int OFFSET_RSA = 4;

    /** Size of a frame. */
    public static final int FRAME_SIZE = 5;

    /** Stack. */
    private final Stack stack;

    /** Heap. */
    private final Heap heap;

    /** Stack pointer. */
    private final Register stackPointer;

    /** New pointer. */
    private final Register newPointer;

    /** Extreme pointer. */
    private final Register extremePointer;

    /** Mark pointer. */
    private final Register markPointer;

    /** Create a new PaMa VM. */
    public PaMa() {
        this.stackPointer = new Register("SP", "Stack Pointer", -1);
        this.newPointer = new Register("NP", "New Pointer", STACKSIZE);
        this.extremePointer = new Register("EP", "Extreme stack Pointer", 0);
        this.markPointer = new Register("MP", "Mark Pointer", 0);

        addDisplayRegister(stackPointer);
        addDisplayRegister(newPointer);
        addDisplayRegister(extremePointer);
        addDisplayRegister(markPointer);

        this.stack = new Stack(stackPointer);
        this.heap = new Heap();
        addDisplayMemory(stack);
        addDisplayMemory(heap);
    }

    /**
     * Returns the element at the given address.
     *
     * @param address address
     * @return the element at the given address
     */
    public final StackObject getElementAt(final int address) {
        if (address <= stackPointer.getValue()) {
            return stack.getElementAt(address);
        } else {
            return (StackObject) heap.getElementAt(address);
        }
    }

    /**
     * Returns the extreme pointer register.
     *
     * @return the extreme pointer register
     */
    public final Register getExtremePointer() {
        return extremePointer;
    }

    /**
     * Returns the mark pointer register.
     *
     * @return the mark pointer register
     */
    public final Register getMarkPointer() {
        return markPointer;
    }

    /**
     * Returns the new pointer register.
     *
     * @return the new pointer register
     */
    public final Register getNewPointer() {
        return newPointer;
    }

    /**
     * Returns the stack pointer register.
     *
     * @return the stack pointer register
     */
    public final Register getStackPointer() {
        return stackPointer;
    }

    /**
     * Returns the element on top of the stack without removing it.
     *
     * @return the element on top of the stack
     */
    public final StackObject peek() {
        return stack.peek();
    }

    /**
     * Returns the element on top of the stack and removes it from there.
     *
     * @return the element on top of the stack
     */
    public final StackObject pop() {
        return stack.pop();
    }

    /**
     * Place object on top of the stack. Updates stack pointer register accordingly.
     *
     * @param object object
     */
    public final void push(final StackObject object) {
        stack.push(object);
    }

    /**
     * Sets element at the given address.
     *
     * @param address address
     * @param object object
     */
    public final void setElementAt(final int address, final StackObject object) {
        if (address <= extremePointer.getValue()) {
            stack.setElementAt(address, object);
        } else {
            heap.setElementAt(address, object);
        }
    }

    /** Mark the beginning of a new frame. */
    public final void startFrame() {
        stack.startFrame(FRAME_SIZE);
    }
}
