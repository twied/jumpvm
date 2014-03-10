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

import java.util.HashMap;

import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;

/** WiMa VM. */
public class WiMa extends JumpVM {
    /** Modus "read" for register {@link #modus}. */
    public static final int MODUS_READ = 0;

    /** Modus "write" for register {@link #modus}. */
    public static final int MODUS_WRITE = 1;

    /** Stack frame offset: Positive return address. */
    public static final int OFFSET_ADDR_POS = -1;

    /** Stack frame offset: Frame pointer. */
    public static final int OFFSET_REG_FP = 0;

    /** Stack frame offset: Back track pointer. */
    public static final int OFFSET_REG_BTP = 1;

    /** Stack frame offset: Trail pointer. */
    public static final int OFFSET_REG_TP = 2;

    /** Stack frame offset: Heap pointer. */
    public static final int OFFSET_REG_HP = 3;

    /** Stack frame offset: Negative return address. */
    public static final int OFFSET_ADDR_NEG = 4;

    /** Number of organizational cells in a stack frame. */
    public static final int FRAME_SIZE = 5;

    /** Stack pointer register. */
    private final Register stackPointer;

    /** Frame pointer register. */
    private final Register framePointer;

    /** Back track pointer register. */
    private final Register backTrackPointer;

    /** Heap pointer register. */
    private final Register heapPointer;

    /** Trail pointer register. */
    private final Register trailPointer;

    /** Modus register. */
    private final Register modus;

    /** Stack memory. */
    private final Stack stack;

    /** Heap memory. */
    private final Heap heap;

    /** Trail memory. */
    private final Heap trail;

    /**
     * Create a new WiMa.
     */
    public WiMa() {
        this.stackPointer = new Register("SP", "Stack Pointer", -1);
        this.framePointer = new Register("FP", "Frame Pointer", 0);
        this.backTrackPointer = new Register("BTP", "Back Track Pointer", 0);
        this.heapPointer = new Register("HP", "Heap Pointer", 0);
        this.trailPointer = new Register("TP", "Trail Pointer", -1);

        final HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(MODUS_READ, "read");
        map.put(MODUS_WRITE, "write");
        this.modus = new Register("Modus", "Modus", MODUS_READ, map);

        addDisplayRegister(stackPointer);
        addDisplayRegister(framePointer);
        addDisplayRegister(backTrackPointer);
        addDisplayRegister(heapPointer);
        addDisplayRegister(trailPointer);
        addDisplayRegister(modus);

        this.stack = new Stack(stackPointer);
        this.heap = new Heap();
        this.trail = new Heap("Trail");
        addDisplayMemory(stack);
        addDisplayMemory(heap);
        addDisplayMemory(trail);
    }

    /**
     * Returns the back track pointer register.
     * 
     * @return the back track pointer register
     */
    public final Register getBackTrackPointer() {
        return backTrackPointer;
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
     * Returns the heap memory.
     * 
     * @return the heap memory
     */
    public final Heap getHeap() {
        return heap;
    }

    /**
     * Returns the heap pointer register.
     * 
     * @return the heap pointer register
     */
    public final Register getHeapPointer() {
        return heapPointer;
    }

    /**
     * Returns the modus register.
     * 
     * @return the modus register
     */
    public final Register getModus() {
        return modus;
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

    /**
     * Returns the trail memory.
     * 
     * @return the trail memory
     */
    public final Heap getTrail() {
        return trail;
    }

    /**
     * Returns the trail pointer register.
     * 
     * @return the trail pointer register
     */
    public final Register getTrailPointer() {
        return trailPointer;
    }
}
