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
 * {@code letrec} {@link Expression}.
 */
public class LetrecExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Variable declarations. */
    private final ArrayList<VariableDeclaration> variableDeclarations;

    /** Value expression. */
    private final Expression expression;

    /**
     * Create a new LetrecExpression.
     * 
     * @param begin begin
     * @param end end
     * @param variableDeclarations variable declarations
     * @param expression value expression
     */
    public LetrecExpression(final Location begin, final Location end, final ArrayList<VariableDeclaration> variableDeclarations, final Expression expression) {
        super(begin, end);
        this.variableDeclarations = variableDeclarations;
        this.expression = expression;

        final DefaultMutableTreeNode declarationsNode = new DefaultMutableTreeNode("Declarations");
        final DefaultMutableTreeNode expressionNode = new DefaultMutableTreeNode("Expression");
        add(declarationsNode);
        add(expressionNode);
        for (final VariableDeclaration declaration : variableDeclarations) {
            declarationsNode.add(declaration);
        }
        expressionNode.add(expression);
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
     * Returns the variable declarations.
     * 
     * @return the variable declarations
     */
    public final ArrayList<VariableDeclaration> getVariableDeclarations() {
        return variableDeclarations;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Letrec:");
        for (final VariableDeclaration declaration : variableDeclarations) {
            stringBuilder.append(" ");
            stringBuilder.append(declaration.getIdentifier());
        }
        return stringBuilder.toString();
    }
}
