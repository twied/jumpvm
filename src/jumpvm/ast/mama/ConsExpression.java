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

package jumpvm.ast.mama;

import jumpvm.compiler.Location;
import jumpvm.compiler.mama.MaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Cons {@link Expression}.
 */
public class ConsExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** List head. */
    private final Expression head;

    /** List tail. */
    private final Expression tail;

    /**
     * Create a new ConsExpression.
     * 
     * @param begin begin
     * @param end end
     * @param head list head
     * @param tail list tail
     */
    public ConsExpression(final Location begin, final Location end, final Expression head, final Expression tail) {
        super(begin, end);
        this.head = head;
        this.tail = tail;

        add(head);
        add(tail);
    }

    /**
     * Returns the list head.
     * 
     * @return the list head
     */
    public final Expression getHead() {
        return head;
    }

    /**
     * Returns the list tail.
     * 
     * @return the list tail
     */
    public final Expression getTail() {
        return tail;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Cons";
    }
}
