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

package jumpvm.ast.pama.expressions;

import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.types.IntegerType;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Integer constant {@link Expression}. */
public class IntegerExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Value. */
    private final int value;

    /**
     * Create a new IntegerExpression.
     *
     * @param begin begin
     * @param end end
     * @param value constant integer value
     */
    public IntegerExpression(final Location begin, final Location end, final int value) {
        super(begin, end);
        this.value = value;
        setType(new IntegerType(begin, end));
    }

    @Override
    public final int getMaxStackSize() {
        return 1;
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    public final int getValue() {
        return value;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return String.valueOf(value);
    }
}
