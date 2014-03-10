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

import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;

/** JumpVM heap. */
public class Heap extends Memory<MemoryObject> {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new Heap.
     */
    public Heap() {
        this("Heap");
    }

    /**
     * Create a new Heap.
     * 
     * @param name This memorys name
     */
    public Heap(final String name) {
        super(name);
    }

    /**
     * Allocate space for a new object on the heap and return a pointer to its address.
     * 
     * @param object object to put on the next free heap cell
     * @param descriptionShort short description for the pointer to this cell
     * @param descriptionLong long description for the pointer to this cell
     * @return a pointer to the object on the heap
     */
    public final PointerObject allocate(final MemoryObject object, final String descriptionShort, final String descriptionLong) {
        final int index = getSize();
        setElementAt(index, object);
        return new PointerObject(index, Type.POINTER_HEAP, descriptionShort, descriptionLong);
    }
}
