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
 * Variable declaration {@link MaMaAstNode}.
 */
public class VariableDeclaration extends MaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Variable identifier. */
    private final String identifier;

    /** Value expression. */
    private final Expression expression;

    /**
     * Create a new VariableDeclaration.
     * 
     * @param begin begin
     * @param end end
     * @param identifier variable identifier
     * @param expression value expression
     */
    public VariableDeclaration(final Location begin, final Location end, final String identifier, final Expression expression) {
        super(begin, end);
        this.identifier = identifier;
        this.expression = expression;

        add(expression);
    }

    /**
     * Returns the value expression.
     * 
     * @return the value expression
     */
    public final Expression getExpression() {
        return expression;
    }

    /**
     * Returns the variable identifier.
     * 
     * @return the variable identifier
     */
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Variable " + identifier;
    }
}
