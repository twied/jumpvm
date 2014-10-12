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

package jumpvm.compiler.pama;

import jumpvm.ast.AstNode;
import jumpvm.ast.pama.CaseLimb;
import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.DesignatorPart;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.Program;
import jumpvm.ast.pama.Range;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.declarations.FuncDecl;
import jumpvm.ast.pama.declarations.TypeDecl;
import jumpvm.ast.pama.declarations.VarDecl;
import jumpvm.ast.pama.designatorpart.IndirectionDesignatorPart;
import jumpvm.ast.pama.designatorpart.ListAccessDesignatorPart;
import jumpvm.ast.pama.designatorpart.SelectorDesignatorPart;
import jumpvm.ast.pama.expressions.BooleanExpression;
import jumpvm.ast.pama.expressions.CallExpression;
import jumpvm.ast.pama.expressions.ConjunctionExpression;
import jumpvm.ast.pama.expressions.DisjunctionExpression;
import jumpvm.ast.pama.expressions.HighExpression;
import jumpvm.ast.pama.expressions.IntegerExpression;
import jumpvm.ast.pama.expressions.LowExpression;
import jumpvm.ast.pama.expressions.NilExpression;
import jumpvm.ast.pama.expressions.RelationalExpression;
import jumpvm.ast.pama.expressions.UnaryExpression;
import jumpvm.ast.pama.statements.AssignmentStatement;
import jumpvm.ast.pama.statements.CallStatement;
import jumpvm.ast.pama.statements.CaseStatement;
import jumpvm.ast.pama.statements.ForStatement;
import jumpvm.ast.pama.statements.IfStatement;
import jumpvm.ast.pama.statements.NewStatement;
import jumpvm.ast.pama.statements.ReadlnStatement;
import jumpvm.ast.pama.statements.RepeatStatement;
import jumpvm.ast.pama.statements.WhileStatement;
import jumpvm.ast.pama.statements.WritelnStatement;
import jumpvm.ast.pama.types.ArrayType;
import jumpvm.ast.pama.types.BooleanType;
import jumpvm.ast.pama.types.CustomType;
import jumpvm.ast.pama.types.FunctionType;
import jumpvm.ast.pama.types.IntegerType;
import jumpvm.ast.pama.types.PointerType;
import jumpvm.ast.pama.types.RecordType;
import jumpvm.compiler.DotBackend;
import jumpvm.exception.CompileException;

/** PaMachine {@link DotBackend}. */
public class PaMaDotBackend extends DotBackend implements PaMaAstWalker {

    /** Create a new PaMaDotBackend. */
    public PaMaDotBackend() {
        super(false);
    }

    @Override
    public final void process(final ArrayType node) throws CompileException {
        emitNode(node, "Array");
        emitNode(node.getDimensions(), "Dimensions");
        emitEdge(node, node.getDimensions());

        if (!node.getDimensions().isEmpty() && (node.getDimensions().get(0) == null)) {
            final String s = "[" + node.getDimensions().size() + "]";
            emitNode(s, s);
            emitEdge(node.getDimensions(), s);
        } else {
            for (final Range range : node.getDimensions()) {
                emitNode(range, String.valueOf(range.getLow()) + " .. " + String.valueOf(range.getHigh()));
                emitEdge(node.getDimensions(), range);
            }
        }

        node.getBaseType().process(this);
        emitEdge(node, node.getBaseType(), "Type");
    }

    @Override
    public final void process(final AssignmentStatement node) throws CompileException {
        emitNode(node, ":=");

        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator(), "lhs");

        node.getExpression().process(this);
        emitEdge(node, node.getExpression(), "rhs");
    }

    @Override
    public final void process(final BooleanExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getValue()));
    }

    @Override
    public final void process(final BooleanType node) throws CompileException {
        emitNode(node, "Boolean");
    }

    @Override
    public final void process(final CallExpression node) throws CompileException {
        emitNode(node, node.getIdentifier() + "()");

        for (final Expression expression : node.getExpressionList()) {
            expression.process(this);
            emitEdge(node, expression);
        }
    }

    @Override
    public final void process(final CallStatement node) throws CompileException {
        emitNode(node, node.getIdentifier() + "()");

        for (final Expression expression : node.getExpressionList()) {
            expression.process(this);
            emitEdge(node, expression);
        }
    }

    @Override
    public final void process(final CaseLimb node) throws CompileException {
        emitNode(node, String.valueOf(node.getValue()));

        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node, statement);
        }
    }

    @Override
    public final void process(final CaseStatement node) throws CompileException {
        emitNode(node, "Case");

        node.getExpression().process(this);
        emitEdge(node, node.getExpression(), "Expression");

        emitNode(node.getCaseLimbList(), "Limbs");
        emitEdge(node, node.getCaseLimbList());
        for (final CaseLimb caseLimb : node.getCaseLimbList()) {
            caseLimb.process(this);
            emitEdge(node.getCaseLimbList(), caseLimb);
        }
    }

    @Override
    public final void process(final ConjunctionExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getOperator()));
        node.getLhs().process(this);
        emitEdge(node, node.getLhs(), "lhs");
        node.getRhs().process(this);
        emitEdge(node, node.getRhs(), "rhs");
    }

    @Override
    public final void process(final CustomType node) throws CompileException {
        emitNode(node, node.getIdentifier());
    }

    @Override
    public final void process(final Declarations node) throws CompileException {
        emitNode(node, "Declarations");

        if (!node.getTypeDeclList().isEmpty()) {
            emitNode(node.getTypeDeclList(), "Types");
            emitEdge(node, node.getTypeDeclList());
            for (final TypeDecl typeDecl : node.getTypeDeclList()) {
                typeDecl.process(this);
                emitEdge(node.getTypeDeclList(), typeDecl);
            }
        }

        if (!node.getVarDeclList().isEmpty()) {
            emitNode(node.getVarDeclList(), "Variables");
            emitEdge(node, node.getVarDeclList());
            for (final VarDecl varDecl : node.getVarDeclList()) {
                varDecl.process(this);
                emitEdge(node.getVarDeclList(), varDecl);
            }
        }

        if (!node.getProcDeclList().isEmpty()) {
            emitNode(node.getProcDeclList(), "Procedures");
            emitEdge(node, node.getProcDeclList());
            for (final FuncDecl funcDecl : node.getProcDeclList()) {
                funcDecl.process(this);
                emitEdge(node.getProcDeclList(), funcDecl);
            }
        }
    }

    @Override
    public final void process(final Designator node) throws CompileException {
        emitNode(node, node.getIdentifier());
        for (final DesignatorPart designatorPart : node.getDesignatorPartList()) {
            designatorPart.process(this);
            emitEdge(node, designatorPart);
        }
    }

    @Override
    public final void process(final DisjunctionExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getOperator()));
        node.getLhs().process(this);
        emitEdge(node, node.getLhs(), "lhs");
        node.getRhs().process(this);
        emitEdge(node, node.getRhs(), "rhs");
    }

    @Override
    public final void process(final FormalParameter node) throws CompileException {
        emitNode(node, node.getIdentifier());
        node.getType().process(this);
        if (node.isReference()) {
            emitEdge(node, node.getType(), "var");
        } else {
            emitEdge(node, node.getType());
        }
    }

    @Override
    public final void process(final ForStatement node) throws CompileException {
        emitNode(node, "For");
        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator(), "Variable");
        node.getFromExpression().process(this);
        emitEdge(node, node.getFromExpression(), "from");
        node.getToExpression().process(this);
        emitEdge(node, node.getToExpression(), "to");
        emitNode(node.getStatementList(), "Statements");
        emitEdge(node, node.getStatementList());
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node.getStatementList(), statement);
        }
    }

    @Override
    public final void process(final FuncDecl node) throws CompileException {
        emitNode(node, node.getIdentifier());
        emitNode(node.getFormalParameterList(), "Parameters");
        emitEdge(node, node.getFormalParameterList());
        for (final FormalParameter formalParameter : node.getFormalParameterList()) {
            formalParameter.process(this);
            emitEdge(node.getFormalParameterList(), formalParameter);
        }
        node.getDeclarations().process(this);
        emitEdge(node, node.getDeclarations());

        if (node.getReturnType() != null) {
            node.getReturnType().process(this);
            emitEdge(node, node.getReturnType(), "Return");
        }
        emitNode(node.getStatementList(), "Statements");
        emitEdge(node, node.getStatementList());
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node.getStatementList(), statement);
        }
    }

    @Override
    public final void process(final FunctionType node) throws CompileException {
        if (node.getReturnType() != null) {
            emitNode(node, "Function");
            node.getReturnType().process(this);
            emitEdge(node, node.getReturnType(), "Return");
        } else {
            emitNode(node, "Procedure");
        }

        for (final FormalParameter parameter : node.getParameterList()) {
            parameter.process(this);
            emitEdge(node, parameter);
        }
    }

    @Override
    public final void process(final HighExpression node) throws CompileException {
        emitNode(node, "High " + node.getDimension());
        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator());
    }

    @Override
    public final void process(final IfStatement node) throws CompileException {
        emitNode(node, "If");
        node.getCondition().process(this);
        emitEdge(node, node.getCondition(), "Condition");
        emitNode(node.getThenStatementList(), "Then");
        emitEdge(node, node.getThenStatementList());
        for (final Statement statement : node.getThenStatementList()) {
            statement.process(this);
            emitEdge(node.getThenStatementList(), statement);
        }

        if (!node.getElseStatementList().isEmpty()) {
            emitNode(node.getElseStatementList(), "Else");
            emitEdge(node, node.getElseStatementList());
            for (final Statement statement : node.getElseStatementList()) {
                statement.process(this);
                emitEdge(node.getElseStatementList(), statement);
            }
        }
    }

    @Override
    public final void process(final IndirectionDesignatorPart node) throws CompileException {
        emitNode(node, "^");
    }

    @Override
    public final void process(final IntegerExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getValue()));
    }

    @Override
    public final void process(final IntegerType node) throws CompileException {
        emitNode(node, "Integer");
    }

    @Override
    public final void process(final ListAccessDesignatorPart node) throws CompileException {
        emitNode(node, "[]");
        for (final Expression expression : node.getExpressionList()) {
            expression.process(this);
            emitEdge(node, expression);
        }
    }

    @Override
    public final void process(final LowExpression node) throws CompileException {
        emitNode(node, "Low " + node.getDimension());
        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator());
    }

    @Override
    public final void process(final NewStatement node) throws CompileException {
        emitNode(node, "New");
        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator());
    }

    @Override
    public final void process(final NilExpression node) throws CompileException {
        emitNode(node, "Nil");
    }

    @Override
    public final void process(final PointerType node) throws CompileException {
        emitNode(node, "^");
        node.getType().process(this);
        emitEdge(node, node.getType());
    }

    @Override
    public final void process(final Program node) throws CompileException {
        begin();
        node.getDeclarations().process(this);
        emitEdge(ROOT, node.getDeclarations());

        emitNode(node.getStatementList(), "Statements");
        emitEdge(ROOT, node.getStatementList());

        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node.getStatementList(), statement);
        }
        end();
    }

    @Override
    public final void process(final ReadlnStatement node) throws CompileException {
        emitNode(node, "Readln");
        node.getDesignator().process(this);
        emitEdge(node, node.getDesignator());
    }

    @Override
    public final void process(final RecordType node) throws CompileException {
        emitNode(node, "Record");
        for (final VarDecl varDecl : node.getVarDeclList()) {
            varDecl.process(this);
            emitEdge(node, varDecl);
        }
    }

    @Override
    public final void process(final RelationalExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getOperator()));
        node.getLhs().process(this);
        emitEdge(node, node.getLhs(), "lhs");
        node.getRhs().process(this);
        emitEdge(node, node.getRhs(), "rhs");
    }

    @Override
    public final void process(final RepeatStatement node) throws CompileException {
        emitNode(node, "Repeat");
        emitNode(node.getStatementList(), "Statements");
        emitEdge(node, node.getStatementList());

        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node.getStatementList(), statement);
        }

        node.getCondition().process(this);
        emitEdge(node, node.getCondition(), "Condition");
    }

    @Override
    public final void process(final SelectorDesignatorPart node) throws CompileException {
        emitNode(node, "." + node.getIdentifier());
    }

    @Override
    public final void process(final TypeDecl node) throws CompileException {
        emitNode(node, node.getIdentifier());
        node.getType().process(this);
        emitEdge(node, node.getType());
    }

    @Override
    public final void process(final UnaryExpression node) throws CompileException {
        emitNode(node, String.valueOf(node.getOperator()));
        node.getExpression().process(this);
        emitEdge(node, node.getExpression());
    }

    @Override
    public final void process(final VarDecl node) throws CompileException {
        emitNode(node, node.getIdentifier());
        node.getType().process(this);
        emitEdge(node, node.getType());
    }

    @Override
    public final void process(final WhileStatement node) throws CompileException {
        emitNode(node, "While");
        node.getCondition().process(this);
        emitEdge(node, node.getCondition(), "Condition");

        emitNode(node.getStatementList(), "Statements");
        emitEdge(node, node.getStatementList());
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
            emitEdge(node.getStatementList(), statement);
        }
    }

    @Override
    public final void process(final WritelnStatement node) throws CompileException {
        emitNode(node, "Writeln");
        node.getExpression().process(this);
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
