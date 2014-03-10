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

import jumpvm.ast.AstNode;
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
import jumpvm.compiler.DotBackend;
import jumpvm.exception.CompileException;

/**
 * MaMachine {@link DotBackend}.
 */
public class MaMaDotBackend extends DotBackend implements MaMaAstWalker {
    /**
     * Create a new MaMaDotBackend.
     */
    public MaMaDotBackend() {
        super(false);
    }

    @Override
    public final void process(final BinOpExpression node) throws CompileException {
        node.getLhs().process(this);
        node.getRhs().process(this);

        emitNode(node, node.getOperator().toString());
        emitEdge(node, node.getLhs(), "lhs");
        emitEdge(node, node.getRhs(), "rhs");
    }

    @Override
    public final void process(final CallExpression node) throws CompileException {
        node.getBody().process(this);

        emitNode(node, "call");
        emitEdge(node, node.getBody(), "function");

        for (final Expression argument : node.getArguments()) {
            argument.process(this);
            emitEdge(node, argument, "argument");
        }
    }

    @Override
    public final void process(final ConsExpression node) throws CompileException {
        node.getHead().process(this);
        node.getTail().process(this);

        emitNode(node, "cons");
        emitEdge(node, node.getHead(), "head");
        emitEdge(node, node.getTail(), "tail");
    }

    @Override
    public final void process(final HeadExpression node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, "head");
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void process(final IdentifierExpression node) throws CompileException {
        emitNode(node, node.getIdentifier());
    }

    @Override
    public final void process(final IfExpression node) throws CompileException {
        node.getCondition().process(this);
        node.getThenExpression().process(this);
        node.getElseExpression().process(this);

        emitNode(node, "if");
        emitEdge(node, node.getCondition(), "if");
        emitEdge(node, node.getThenExpression(), "then");
        emitEdge(node, node.getElseExpression(), "else");
    }

    @Override
    public final void process(final IsNilExpression node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, "isnil");
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void process(final LambdaExpression node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, "lambda");
        for (final String identifier : node.getIdentifiers()) {
            emitNode(identifier, identifier);
            emitEdge(node, identifier, "var");
        }

        emitEdge(node, node.getExpression(), "expression");
    }

    @Override
    public final void process(final LetrecExpression node) throws CompileException {
        emitNode(node, "letrec");
        for (final VariableDeclaration v : node.getVariableDeclarations()) {
            v.process(this);
            emitEdge(node, v, "definition");
        }

        node.getExpression().process(this);
        emitEdge(node, node.getExpression(), "expression");
    }

    @Override
    public final void process(final ListExpression node) throws CompileException {
        emitNode(node, "list");

        if (node.getExpressions() == null) {
            return;
        }

        for (final Expression expression : node.getExpressions()) {
            expression.process(this);
            emitEdge(node, expression);
        }
    }

    @Override
    public final void process(final NumeralExpression node) throws CompileException {
        emitNode(node, node.getNumeral().toString());
    }

    @Override
    public final void process(final Program node) throws CompileException {
        begin();
        node.getExpression().process(this);
        emitEdge(ROOT, node.getExpression());
        end();
    }

    @Override
    public final void process(final TailExpression node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, "tail");
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void process(final UnOpExpression node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, node.getOperator().toString());
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void process(final VariableDeclaration node) throws CompileException {
        node.getExpression().process(this);

        emitNode(node, node.getIdentifier());
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void processProgram(final AstNode<?> program) throws CompileException {
        try {
            process((Program) program);
        } catch (final RuntimeException e) {
            throw new CompileException(program, e);
        }
    }
}
