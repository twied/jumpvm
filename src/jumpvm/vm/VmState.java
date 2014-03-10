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

import java.util.ArrayList;
import java.util.HashMap;

import jumpvm.memory.Memory;
import jumpvm.memory.Register;
import jumpvm.memory.objects.MemoryObject;

/**
 * Snapshot of the state of a {@link JumpVM}.
 */
public class VmState {
    /** Registers. */
    private final HashMap<Register, Integer> registerSet;

    /** Memories. */
    private final HashMap<Memory<?>, ArrayList<?>> memorySet;

    /**
     * Create a new VmState.
     * 
     * @param vm JumpVM
     */
    public VmState(final JumpVM vm) {
        registerSet = new HashMap<Register, Integer>();
        for (final Register register : vm.getDisplayRegisters()) {
            registerSet.put(register, register.getValue());
        }

        memorySet = new HashMap<Memory<?>, ArrayList<?>>();
        for (final Memory<?> memory : vm.getDisplayMemories()) {
            memorySet.put(memory, memory.getContent());
        }
    }

    /**
     * Reset the vm to this state.
     */
    @SuppressWarnings("unchecked")
    public final void set() {
        for (final Register register : registerSet.keySet()) {
            register.setValue(registerSet.get(register));
        }

        for (final Memory<?> memory : memorySet.keySet()) {
            memory.reset((ArrayList<? extends MemoryObject>) memorySet.get(memory));
        }
    }
}
