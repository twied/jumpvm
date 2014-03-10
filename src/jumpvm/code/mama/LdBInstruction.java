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

package jumpvm.code.mama;

import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.vm.MaMa;

/**
 * Load basic value.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := value
 * </pre>
 */
public class LdBInstruction extends MaMaInstruction {
    /** Value. */
    private final int value;

    /**
     * Create a new LdB instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param value value
     */
    public LdBInstruction(final MaMaAstNode sourceNode, final int value) {
        super(sourceNode);
        this.value = value;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        vm.getStack().push(new BasicValueObject(value, null, null));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Load basic value";
    }

    @Override
    public final String getMnemonic() {
        return "ldb";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(value);
    }
}
