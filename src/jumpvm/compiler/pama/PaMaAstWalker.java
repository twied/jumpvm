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

import jumpvm.ast.pama.CaseLimb;
import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.Program;
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
import jumpvm.compiler.AstWalker;
import jumpvm.exception.CompileException;

/** PaMachine AST tree walker. */
public interface PaMaAstWalker extends AstWalker {
    /**
     * Process an {@link ArrayType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(ArrayType node) throws CompileException;

    /**
     * Process an {@link AssignmentStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(AssignmentStatement node) throws CompileException;

    /**
     * Process a {@link BooleanExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(BooleanExpression node) throws CompileException;

    /**
     * Process a {@link BooleanType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(BooleanType node) throws CompileException;

    /**
     * Process a {@link CallExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(CallExpression node) throws CompileException;

    /**
     * Process a {@link CallStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(CallStatement node) throws CompileException;

    /**
     * Process a {@link CaseLimb}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(CaseLimb node) throws CompileException;

    /**
     * Process a {@link CaseStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(CaseStatement node) throws CompileException;

    /**
     * Process a {@link ConjunctionExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(ConjunctionExpression node) throws CompileException;

    /**
     * Process a {@link CustomType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(CustomType node) throws CompileException;

    /**
     * Process {@link Declarations}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(Declarations node) throws CompileException;

    /**
     * Process a {@link Designator}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(Designator node) throws CompileException;

    /**
     * Process a {@link DisjunctionExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(DisjunctionExpression node) throws CompileException;

    /**
     * Process a {@link FormalParameter}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(FormalParameter node) throws CompileException;

    /**
     * process a {@link ForStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(ForStatement node) throws CompileException;

    /**
     * process a {@link FuncDecl}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(FuncDecl node) throws CompileException;

    /**
     * process a {@link FunctionType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(FunctionType node) throws CompileException;

    /**
     * process a {@link HighExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(HighExpression node) throws CompileException;

    /**
     * process an {@link IfStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(IfStatement node) throws CompileException;

    /**
     * process an {@link IndirectionDesignatorPart}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(IndirectionDesignatorPart node) throws CompileException;

    /**
     * process an {@link IntegerExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(IntegerExpression node) throws CompileException;

    /**
     * process an {@link IntegerType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(IntegerType node) throws CompileException;

    /**
     * process a {@link ListAccessDesignatorPart}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(ListAccessDesignatorPart node) throws CompileException;

    /**
     * process a {@link LowExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */

    void process(LowExpression node) throws CompileException;

    /**
     * process a {@link NewStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(NewStatement node) throws CompileException;

    /**
     * process a {@link NilExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(NilExpression node) throws CompileException;

    /**
     * process a {@link PointerType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(PointerType node) throws CompileException;

    /**
     * process a {@link Program}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(Program node) throws CompileException;

    /**
     * process a {@link ReadlnStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(ReadlnStatement node) throws CompileException;

    /**
     * process a {@link RecordType}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(RecordType node) throws CompileException;

    /**
     * process a {@link RelationalExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(RelationalExpression node) throws CompileException;

    /**
     * process a {@link RepeatStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(RepeatStatement node) throws CompileException;

    /**
     * process a {@link SelectorDesignatorPart}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(SelectorDesignatorPart node) throws CompileException;

    /**
     * process a {@link TypeDecl}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(TypeDecl node) throws CompileException;

    /**
     * process an {@link UnaryExpression}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(UnaryExpression node) throws CompileException;

    /**
     * process a {@link VarDecl}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(VarDecl node) throws CompileException;

    /**
     * process a {@link WhileStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(WhileStatement node) throws CompileException;

    /**
     * process a {@link WritelnStatement}.
     *
     * @param node AST node
     * @throws CompileException on failure
     */
    void process(WritelnStatement node) throws CompileException;
}
