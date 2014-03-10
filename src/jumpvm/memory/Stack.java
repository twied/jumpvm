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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import jumpvm.memory.objects.StackObject;

/**
 * JumpVM stack.
 */
public class Stack extends Memory<StackObject> implements Observer {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Register: Stack pointer. */
    private final Register stackPointer;

    /** Frames: Start Index, End Index. */
    private final TreeMap<Integer, Integer> frames;

    /**
     * Create a new stack.
     * 
     * @param stackPointer associated stack pointer
     */
    public Stack(final Register stackPointer) {
        this(stackPointer, "Stack");
    }

    /**
     * Create a new stack.
     * 
     * @param stackPointer associated stack pointer
     * @param name This memory's name
     */
    public Stack(final Register stackPointer, final String name) {
        super(name);
        this.stackPointer = stackPointer;
        this.frames = new TreeMap<Integer, Integer>();

        stackPointer.addObserver(this);
    }

    /**
     * Returns the map of stack frames.
     * 
     * @return the map of stack frames
     */
    public final TreeMap<Integer, Integer> getFrames() {
        return frames;
    }

    /**
     * Returns the the top value from the stack without removing it.
     * 
     * @return the top value from the stack
     */
    public final StackObject peek() {
        return getElementAt(stackPointer.getValue());
    }

    /**
     * Returns and removes the top value from the stack.
     * 
     * @return the top value from the stack
     */
    public final StackObject pop() {
        final StackObject result = removeElementAt(stackPointer.getValue());
        stackPointer.decrement();
        for (final Integer key : new ArrayList<Integer>(frames.keySet())) {
            if (key > stackPointer.getValue()) {
                frames.remove(key);
            }
        }
        return result;
    }

    /**
     * Push a value on the stack.
     * 
     * @param object Object to be put on the stack
     */
    public final void push(final StackObject object) {
        setElementAt(stackPointer.getValue() + 1, object);
        stackPointer.increment();
    }

    /**
     * Start a new frame at the current position and the given amount of organizational cells.
     * 
     * @param length frame header = organizational cells
     */
    public final void startFrame(final int length) {
        frames.put(getSize(), getSize() + length);
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        while (stackPointer.getValue() < (getSize() - 1)) {
            removeElementAt(getSize() - 1);
        }
    }
}
