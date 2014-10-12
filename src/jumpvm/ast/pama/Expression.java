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

package jumpvm.ast.pama;

import jumpvm.compiler.Location;

/** Base class for all PaMa Expressions. */
public abstract class Expression extends PaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Type of this expression. */
    private Type type;

    /** Will this end up in a function call as a reference argument? */
    private boolean reference;

    /**
     * Create a new Expression.
     *
     * @param begin begin
     * @param end end
     */
    public Expression(final Location begin, final Location end) {
        super(begin, end);
    }

    /**
     * Returns the maximum stack usage on execution.
     *
     * @return the maximum stack usage on execution
     */
    public abstract int getMaxStackSize();

    /**
     * Returns the type of this expression.
     *
     * @return the type of this expression
     */
    public final Type getType() {
        return type;
    }

    /**
     * Is this expression used in a function call as a reference argument?
     *
     * @return {@code true} if reference argument for call
     */
    public final boolean isReference() {
        return reference;
    }

    /**
     * Mark this expression as reference argument to a function / procedure call.
     *
     * @param reference if reference or not
     */
    public final void setReference(final boolean reference) {
        this.reference = reference;
    }

    /**
     * Set the type of this expression.
     *
     * @param type type of this expression
     */
    public final void setType(final Type type) {
        this.type = type;
    }
}
