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

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import jumpvm.code.Instruction;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Memory;
import jumpvm.memory.Program;
import jumpvm.memory.Register;

/**
 * JumpVM base VM.
 */
public abstract class JumpVM {
    /** VM can execute another step. */
    public static final int STATUS_OK = 1;

    /** VM terminated. */
    public static final int STATUS_STOP = 0;

    /** VM state. */
    private final Register status;

    /** Program step counter. */
    private final Register stepCounter;

    /** Program counter register. */
    private final Register programCounter;

    /** Program memory. */
    private final Program program;

    /** Registers to display. */
    private final ArrayList<Register> displayRegisters;

    /** Memories to display. */
    private final ArrayList<Memory<?>> displayMemories;

    /** Output writer. */
    private PrintWriter writer;

    /**
     * Create a new JumpVM.
     */
    public JumpVM() {
        final HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(STATUS_OK, "OK");
        map.put(STATUS_STOP, "STOP");

        this.status = new Register("Status", "JumpVM Status", STATUS_OK, map);
        this.stepCounter = new Register("Step", "Step Counter", 0);
        this.programCounter = new Register("PC", "Program Counter", 0);
        this.program = new Program();
        this.displayRegisters = new ArrayList<Register>();
        this.displayMemories = new ArrayList<Memory<?>>();
        this.writer = new PrintWriter(System.out);

        displayRegisters.add(stepCounter);
        displayRegisters.add(status);
        displayRegisters.add(programCounter);
        displayMemories.add(program);
    }

    /**
     * Add a memory to the list of displayed memories.
     * 
     * @param memory memory to add to the list
     */
    public final void addDisplayMemory(final Memory<?> memory) {
        displayMemories.add(memory);
    }

    /**
     * Add a register to the list of displayed registers.
     * 
     * @param register register to add to the list
     */
    public final void addDisplayRegister(final Register register) {
        displayRegisters.add(register);
    }

    /**
     * Returns the list of displayed memories with theyr associated register, if any.
     * 
     * @return the list of displayed memories
     */
    public final ArrayList<Memory<?>> getDisplayMemories() {
        return displayMemories;
    }

    /**
     * Returns the list of displayed registers.
     * 
     * @return the list of displayed registers
     */
    public final ArrayList<Register> getDisplayRegisters() {
        return displayRegisters;
    }

    /**
     * Returns the program memory.
     * 
     * @return the program memory
     */
    public final Program getProgram() {
        return program;
    }

    /**
     * Returns the program counter register.
     * 
     * @return the program counter register
     */
    public final Register getProgramCounter() {
        return programCounter;
    }

    /**
     * Returns the vm status register.
     * 
     * @return the vm status register
     */
    public final Register getStatus() {
        return status;
    }

    /**
     * Returns the output writer.
     * 
     * @return the writer that is used for all VM output
     */
    public final PrintWriter getWriter() {
        return writer;
    }

    /**
     * Returns true if the JumpVM can execute another step.
     * 
     * @return true if the JumpVM can execute another step
     */
    public final boolean isRunning() {
        return status.getValue() == STATUS_OK;
    }

    /**
     * Resets the virtual machine but keeps the content of the program store intact.
     */
    public final void reset() {
        for (final Register register : displayRegisters) {
            register.reset();
        }

        for (final Memory<?> memory : displayMemories) {
            if (memory != program) {
                memory.reset();
            }
        }

        status.setValue(STATUS_OK);
    }

    /**
     * Resets the virtual machine and sets the program store to the given set of instructions.
     * 
     * @param instructions new program
     */
    public final void reset(final ArrayList<Instruction> instructions) {
        reset();
        program.reset(instructions);
    }

    /**
     * Set the output writer.
     * 
     * @param writer the writer that is used for all VM output
     */
    public final void setWriter(final Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    /**
     * Executes a fetch-decode-execute cycle.
     * 
     * @return the previous VmState
     * @throws ExecutionException on failure
     */
    public final VmState step() throws ExecutionException {

        /* save state */
        final VmState state = new VmState(this);

        /* increase step counter */
        stepCounter.setValue(stepCounter.getValue() + 1);

        /* get current instruction */
        final Instruction i;
        try {
            i = program.getElementAt(programCounter.getValue());
        } catch (final RuntimeException e) {
            state.set();
            throw new ExecutionException(null, e);
        }

        /* increment program counter */
        programCounter.increment();

        /* execute instruction */
        try {
            i.execute(this);
        } catch (final RuntimeException e) {
            state.set();
            throw new ExecutionException(i, e);
        }

        return state;
    }
}
