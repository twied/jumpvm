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

package jumpvm.code.wima;

import jumpvm.ast.wima.WiMaAstNode;

/**
 * Create free variable reference on stack.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[FP + i] := new(REF : HP);
 * ST[SP] := ST[FP + i];
 * </pre>
 */
public class PutVarInstruction extends WiMaInstruction {
    /** Offset. */
    private final int offset;

    /** Variable name. */
    private final String identifier;

    /**
     * Create new PutVar Instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param offset Offset
     * @param identifier Variable name
     */
    public PutVarInstruction(final WiMaAstNode sourceNode, final int offset, final String identifier) {
        super(sourceNode);
        this.offset = offset;
        this.identifier = identifier;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Put unbound variable " + identifier + " on the stack";
    }

    @Override
    public final String getMnemonic() {
        return "putvar";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(offset);
    }
}
