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
 * Start backtracking or halt the virtual machine.
 * 
 * <pre>
 * if BTP > FP then
 *     goto backtrack;
 * else
 *     stop;
 * fi
 * </pre>
 */
public class HaltInstruction extends WiMaInstruction {
    /**
     * Create a new HaltInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public HaltInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Start backtracking or halt the virtual machine";
    }

    @Override
    public final String getMnemonic() {
        return "halt";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
