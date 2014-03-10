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

package jumpvm.exception;

import jumpvm.code.Instruction;

/**
 * Exception indicating failure to execute an instruction on a vm.
 */
public class ExecutionException extends Exception {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Offending instruction. */
    private final Instruction instruction;

    /**
     * Create a new ExecutionException.
     * 
     * @param instruction offending instruction
     * @param cause the cause for the exception
     */
    public ExecutionException(final Instruction instruction, final RuntimeException cause) {
        super(cause);
        this.instruction = instruction;
    }

    /**
     * Create a new ExecutionException.
     * 
     * @param instruction offending instruction
     * @param message the detail message
     */
    public ExecutionException(final Instruction instruction, final String message) {
        super(message);
        this.instruction = instruction;
    }

    /**
     * Returns the offending instruction.
     * 
     * @return the offending instruction
     */
    public final Instruction getInstruction() {
        return instruction;
    }
}
