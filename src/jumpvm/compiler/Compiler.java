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

package jumpvm.compiler;

import java.util.ArrayList;

import jumpvm.ast.AstNode;
import jumpvm.code.Instruction;
import jumpvm.exception.CompileException;

/**
 * JumpVM compiler.
 */
public abstract class Compiler {
    /** Resulting list of instructions. */
    private final ArrayList<Instruction> instructions;

    /**
     * Create a new Compiler.
     */
    public Compiler() {
        instructions = new ArrayList<Instruction>();
    }

    /**
     * Emit the given instruction.
     * 
     * @param instruction instruction to be emitted
     */
    protected final void emit(final Instruction instruction) {
        instructions.add(instruction);
    }

    /**
     * Emits the given instruction n times.
     * 
     * @see #emit(Instruction, AstNode)
     * @param n how often the instruction shall be emitted
     * @param instruction instruction to be emitted
     */
    protected final void emitN(final int n, final Instruction instruction) {
        for (int i = 0; i < n; ++i) {
            emit(instruction);
        }
    }

    /**
     * Returns the current position in the resulting instruction stream.
     * 
     * @return the current position in the resulting instruction stream
     */
    protected final int getCurrentPosition() {
        return instructions.size();
    }

    /**
     * Returns the resulting instructions.
     * 
     * @return the resulting instructions
     */
    public final ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Compile the given program.
     * 
     * @param program root node of the program's AST.
     * @throws CompileException on failure
     */
    public abstract void processProgram(AstNode<?> program) throws CompileException;
}
