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

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.compiler.Location;
import jumpvm.compiler.mama.MaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Call {@link Expression}.
 */
public class CallExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Function body. */
    private final Expression body;

    /** Argument expressions. */
    private final ArrayList<Expression> arguments;

    /**
     * Create a new CallExpression.
     * 
     * @param begin begin
     * @param end end
     * @param body function body
     * @param arguments argument expressions
     */
    public CallExpression(final Location begin, final Location end, final Expression body, final ArrayList<Expression> arguments) {
        super(begin, end);
        this.body = body;
        this.arguments = arguments;

        final DefaultMutableTreeNode functionNode = new DefaultMutableTreeNode("Function");
        final DefaultMutableTreeNode argumentsNode = new DefaultMutableTreeNode("Arguments");
        add(functionNode);
        add(argumentsNode);
        functionNode.add(body);
        for (final Expression argument : arguments) {
            argumentsNode.add(argument);
        }
    }

    /**
     * Returns the argument expressions.
     * 
     * @return the argument expressions
     */
    public final ArrayList<Expression> getArguments() {
        return arguments;
    }

    /**
     * Returns the function body.
     * 
     * @return the function body
     */
    public final Expression getBody() {
        return body;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Call";
    }
}
