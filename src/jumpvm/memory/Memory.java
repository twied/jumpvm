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

import javax.swing.AbstractListModel;

import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.StackObject;

/**
 * Basic memory implementation.
 * 
 * @param <E> Type of the objects to store
 */
public class Memory<E extends MemoryObject> extends AbstractListModel<MemoryObject> {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** This memory's name. */
    private final String name;

    /** Internal memory. */
    private final ArrayList<MemoryObject> memory;

    /**
     * Create a new Memory.
     * 
     * @param name This memory's name
     */
    public Memory(final String name) {
        this.memory = new ArrayList<MemoryObject>();
        this.name = name;
    }

    /**
     * Returns raw access to this memory's data.
     * 
     * @return raw access to this memory's data
     */
    @SuppressWarnings("unchecked")
    public final ArrayList<E> getContent() {
        final ArrayList<E> copy = new ArrayList<E>();
        copy.addAll((ArrayList<E>) memory);
        return copy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final E getElementAt(final int index) {
        return (E) memory.get(index);
    }

    /**
     * Alias for {@link #getElementAt(int)}.
     * 
     * @param index the requested index
     * @return the value at index
     */
    public final E getElementAt(final Register index) {
        return getElementAt(index.getValue());
    }

    /**
     * Alias for {@link #getElementAt(int)}.
     * 
     * @param index the requested index
     * @return the value at index
     */
    public final E getElementAt(final StackObject index) {
        return getElementAt(index.getIntValue());
    }

    /**
     * Returns this memory's name.
     * 
     * @return this memory's name
     */
    public final String getName() {
        return name;
    }

    @Override
    public final int getSize() {
        return memory.size();
    }

    /**
     * Removes the object at the given index.
     * 
     * @param index Adress
     * @return the removed object
     */
    @SuppressWarnings("unchecked")
    protected final E removeElementAt(final int index) {
        final E retValue = (E) memory.remove(index);
        fireIntervalRemoved(this, index, index);
        return retValue;
    }

    /**
     * Clear the content of this memory.
     */
    public final void reset() {
        final int size = memory.size();
        memory.clear();
        fireIntervalRemoved(this, 0, size);
    }

    /**
     * Clear the content of this memory, then set it to given new content.
     * 
     * @param values new content
     */
    public final void reset(final ArrayList<? extends MemoryObject> values) {
        final int size = memory.size();
        memory.clear();
        memory.addAll(values);
        fireIntervalRemoved(this, 0, size);
        fireIntervalAdded(this, 0, memory.size());
    }

    /**
     * Sets the object at a given index.
     * 
     * @param index Adress
     * @param object Value
     */
    public final void setElementAt(final int index, final E object) {
        while (memory.size() <= index) {
            memory.add(null);
        }
        memory.set(index, object);
        fireContentsChanged(this, index, index);
    }

    /**
     * Alias for {@link #setElementAt(int, E)}.
     * 
     * @param index Adress
     * @param object Value
     */
    public final void setElementAt(final Register index, final E object) {
        setElementAt(index.getValue(), object);
    }

    /**
     * Alias for {@link #setElementAt(int, E)}.
     * 
     * @param index Adress
     * @param object Value
     */
    public final void setElementAt(final StackObject index, final E object) {
        setElementAt(index.getIntValue(), object);
    }

    @Override
    public final String toString() {
        return "Memory [name=" + name + ", memory=" + memory + "]";
    }
}
