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

package jumpvm.ast.pama.declarations;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.NamedNode;
import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa function declaration. */
public class FuncDecl extends PaMaAstNode implements NamedNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Function name. */
    private final String identifier;

    /** List of formal parameters. */
    private final ArrayList<FormalParameter> formalParameterList;

    /** Type of the return value. */
    private final Type returnType;

    /** Declarations. */
    private final Declarations declarations;

    /** List of statements. */
    private final ArrayList<Statement> statementList;

    /**
     * Create a new FuncDecl.
     *
     * @param begin begin
     * @param end end
     * @param identifier function name, may be {@code null}
     * @param formalParameterList list of formal parameters
     * @param returnType return type, {@code null} for procedures
     * @param declarations declarations
     * @param statementList list of statements
     */
    public FuncDecl(final Location begin, final Location end, final String identifier, final ArrayList<FormalParameter> formalParameterList, final Type returnType, final Declarations declarations, final ArrayList<Statement> statementList) {
        super(begin, end);
        this.identifier = identifier;
        this.formalParameterList = formalParameterList;
        this.returnType = returnType;
        this.declarations = declarations;
        this.statementList = statementList;

        final DefaultMutableTreeNode parameters = new DefaultMutableTreeNode("Parameters");
        if (!formalParameterList.isEmpty()) {
            add(parameters);
        }
        final DefaultMutableTreeNode statements = new DefaultMutableTreeNode("Statements");
        if (!statementList.isEmpty()) {
            add(statements);
        }
        if (returnType != null) {
            add(returnType);
        }

        add(declarations);

        for (final FormalParameter formalParameter : formalParameterList) {
            parameters.add(formalParameter);
        }
        for (final Statement statement : statementList) {
            statements.add(statement);
        }
    }

    /**
     * Returns the declarations.
     *
     * @return the declarations
     */
    public final Declarations getDeclarations() {
        return declarations;
    }

    /**
     * Returns the list of formal parameters.
     *
     * @return the list of formal parameters
     */
    public final ArrayList<FormalParameter> getFormalParameterList() {
        return formalParameterList;
    }

    @Override
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the type of the result value.
     *
     * @return the type of the result value or {@code null} if this is a procedure
     */
    public final Type getReturnType() {
        return returnType;
    }

    /**
     * Returns the list of statements.
     *
     * @return the list of statements
     */
    public final ArrayList<Statement> getStatementList() {
        return statementList;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return identifier;
    }
}
