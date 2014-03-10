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

package jumpvm.code;

import jumpvm.ast.AstNode;
import jumpvm.memory.objects.MemoryObject;

/**
 * Instruction for the JumpVM.
 */
public abstract class Instruction implements MemoryObject {
    /** AstNode that is responsible for this instruction's creation. */
    private final AstNode<?> sourceNode;

    /**
     * Create a new Instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public Instruction(final AstNode<?> sourceNode) {
        this.sourceNode = sourceNode;
    }

    /**
     * Do not display a description. The description goes in the hoover text.
     * 
     * @return {@code null}
     */
    @Override
    public final String getDisplayDescription() {
        return null;
    }

    /**
     * Do not display "instruction" as type for each instruction.
     * 
     * @return {@code null}
     */
    @Override
    public final String getDisplayType() {
        return null;
    }

    @Override
    public final String getDisplayValue() {
        final String parameter = getParameter();
        if (parameter == null) {
            return getMnemonic();
        } else {
            return getMnemonic() + " <b>" + getParameter() + "</b>";
        }
    }

    /**
     * Returns the instruction's mnemonic.
     * 
     * @return the instruction's mnemonic
     */
    public abstract String getMnemonic();

    /**
     * Returns the instruction's parameter.
     * 
     * @return the instruction's parameter or {@code null} if the instruction has none.
     */
    public abstract String getParameter();

    /**
     * Returns the AstNode that is responsible for this instruction's creation.
     * 
     * @return the AstNode that is responsible for this instruction's creation
     */
    public final AstNode<?> getSourceNode() {
        return sourceNode;
    }
}
