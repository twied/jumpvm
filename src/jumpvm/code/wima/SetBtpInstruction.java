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
import jumpvm.memory.Label;

/**
 * Create back track point.
 * 
 * <pre>
 * ST[FP + 1] := BTP;
 * ST[FP + 2] := TP;
 * ST[FP + 3] := HP;
 * ST[FP + 4] := l;
 * BTP := FP;
 * </pre>
 */
public class SetBtpInstruction extends WiMaInstruction {
    /** Next alternative. */
    private final Label label;

    /**
     * Create new SetBtpInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param label next alternative
     */
    public SetBtpInstruction(final WiMaAstNode sourceNode, final Label label) {
        super(sourceNode);
        this.label = label;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create back track point with next alternative at instruction " + label.getAddress() + " (" + label.getName() + ")";
    }

    @Override
    public final String getMnemonic() {
        return "setbtp";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(label.getAddress());
    }
}
