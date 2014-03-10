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

package jumpvm.compiler.mama;

import java.util.ArrayList;

import jumpvm.ast.mama.BinOpExpression;
import jumpvm.ast.mama.CallExpression;
import jumpvm.ast.mama.ConsExpression;
import jumpvm.ast.mama.Expression;
import jumpvm.ast.mama.HeadExpression;
import jumpvm.ast.mama.IdentifierExpression;
import jumpvm.ast.mama.IfExpression;
import jumpvm.ast.mama.IsNilExpression;
import jumpvm.ast.mama.LambdaExpression;
import jumpvm.ast.mama.LetrecExpression;
import jumpvm.ast.mama.ListExpression;
import jumpvm.ast.mama.NumeralExpression;
import jumpvm.ast.mama.Program;
import jumpvm.ast.mama.TailExpression;
import jumpvm.ast.mama.UnOpExpression;
import jumpvm.ast.mama.VariableDeclaration;
import jumpvm.exception.CompileException;

/**
 * Bound variables {@link MaMaAstWalker}.
 * 
 * Creates a list of bound variables in a given MaMa AST.
 */
public class BoundVar implements MaMaAstWalker {
    /** Bound variables. */
    private final ArrayList<String> variables;

    /**
     * Create a new BoundVar.
     */
    public BoundVar() {
        variables = new ArrayList<String>();
    }

    /**
     * Returns the list of bound variables.
     * 
     * @return the list of bound variables
     */
    public final ArrayList<String> getVariables() {
        return variables;
    }

    @Override
    public final void process(final BinOpExpression node) throws CompileException {
        node.getRhs().process(this);
        node.getLhs().process(this);
    }

    @Override
    public final void process(final CallExpression node) throws CompileException {
        node.getBody().process(this);
        for (final Expression argument : node.getArguments()) {
            argument.process(this);
        }
    }

    @Override
    public final void process(final ConsExpression node) throws CompileException {
        node.getHead().process(this);
        node.getTail().process(this);
    }

    @Override
    public final void process(final HeadExpression node) throws CompileException {
        node.getExpression().process(this);
    }

    @Override
    public final void process(final IdentifierExpression node) throws CompileException {
        return;
    }

    @Override
    public final void process(final IfExpression node) throws CompileException {
        node.getCondition().process(this);
        node.getThenExpression().process(this);
        node.getElseExpression().process(this);
    }

    @Override
    public final void process(final IsNilExpression node) throws CompileException {
        node.getExpression().process(this);
    }

    @Override
    public final void process(final LambdaExpression node) throws CompileException {
        final FreeVar freeVar = new FreeVar();

        node.getExpression().process(this);
        node.getExpression().process(freeVar);
        freeVar.getVariables().retainAll(node.getIdentifiers());
        for (final String variable : freeVar.getVariables()) {
            if (!variables.contains(variable)) {
                variables.add(variable);
            }
        }
    }

    @Override
    public final void process(final LetrecExpression node) throws CompileException {
        final ArrayList<String> removeAll = new ArrayList<String>();

        node.getExpression().process(this);
        for (final VariableDeclaration d : node.getVariableDeclarations()) {
            d.getExpression().process(this);
            removeAll.add(d.getIdentifier());
        }
        variables.removeAll(removeAll);
    }

    @Override
    public final void process(final ListExpression node) throws CompileException {
        for (final Expression expression : node.getExpressions()) {
            expression.process(this);
        }
    }

    @Override
    public final void process(final NumeralExpression node) throws CompileException {
        return;
    }

    @Override
    public final void process(final Program node) throws CompileException {
        node.getExpression().process(this);
    }

    @Override
    public final void process(final TailExpression node) throws CompileException {
        node.getExpression().process(this);
    }

    @Override
    public final void process(final UnOpExpression node) throws CompileException {
        node.getExpression().process(this);
    }

    @Override
    public final void process(final VariableDeclaration node) {
        return;
    }
}
