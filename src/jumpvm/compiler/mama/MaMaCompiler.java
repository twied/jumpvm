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
import java.util.Collections;
import java.util.HashMap;

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
import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.ast.mama.NumeralExpression;
import jumpvm.ast.mama.Program;
import jumpvm.ast.mama.TailExpression;
import jumpvm.ast.mama.UnOpExpression;
import jumpvm.ast.mama.VariableDeclaration;
import jumpvm.code.mama.AllocInstruction;
import jumpvm.code.mama.ApplyInstruction;
import jumpvm.code.mama.ConsInstruction;
import jumpvm.code.mama.EvalInstruction;
import jumpvm.code.mama.GetBasicInstruction;
import jumpvm.code.mama.HaltInstruction;
import jumpvm.code.mama.HdInstruction;
import jumpvm.code.mama.IsNilInstruction;
import jumpvm.code.mama.JFalseInstruction;
import jumpvm.code.mama.LdBInstruction;
import jumpvm.code.mama.LdLInstruction;
import jumpvm.code.mama.MarkInstruction;
import jumpvm.code.mama.MkBasicInstruction;
import jumpvm.code.mama.MkClosInstruction;
import jumpvm.code.mama.MkFunvalInstruction;
import jumpvm.code.mama.MkVecInstruction;
import jumpvm.code.mama.NilInstruction;
import jumpvm.code.mama.OpBinInstruction;
import jumpvm.code.mama.OpUnInstruction;
import jumpvm.code.mama.PushGlobInstruction;
import jumpvm.code.mama.PushLocInstruction;
import jumpvm.code.mama.ReturnInstruction;
import jumpvm.code.mama.RewriteInstruction;
import jumpvm.code.mama.SlideInstruction;
import jumpvm.code.mama.TargInstruction;
import jumpvm.code.mama.TlInstruction;
import jumpvm.code.mama.UJmpInstruction;
import jumpvm.code.mama.UpdateInstruction;
import jumpvm.compiler.Compiler;
import jumpvm.compiler.mama.Position.Type;
import jumpvm.exception.CompileException;
import jumpvm.memory.Label;
import jumpvm.vm.MaMa;

/**
 * MaMachine {@link Compiler}.
 */
public class MaMaCompiler extends Compiler {
    /**
     * MaMachine Code walker.
     */
    public abstract class Code implements MaMaAstWalker {
        /** Variable context. */
        private final HashMap<String, Position> beta;

        /** Stack level. */
        private final int stacklevel;

        /**
         * Create a new code walker.
         * 
         * @param beta variable context
         * @param stacklevel stack level
         */
        public Code(final HashMap<String, Position> beta, final int stacklevel) {
            this.beta = beta;
            this.stacklevel = stacklevel;
        }

        /**
         * Returns the variable context.
         * 
         * @return the variable context
         */
        protected final HashMap<String, Position> getBeta() {
            return beta;
        }

        /**
         * Returns the stack level.
         * 
         * @return the stack level
         */
        protected final int getStackLevel() {
            return stacklevel;
        }

        /**
         * Create new CodeB code walker.
         * 
         * @param modifier modifier to the stacklevel
         * @return new CodeB code walker
         */
        protected final Code newCodeB(final int modifier) {
            return new CodeB(beta, stacklevel + modifier);
        }

        /**
         * Create new CodeC code walker.
         * 
         * @param modifier modifier to the stacklevel
         * @return new CodeC code walker
         */
        protected final Code newCodeC(final int modifier) {
            return new CodeC(beta, stacklevel + modifier);
        }

        /**
         * Create new CodeV code walker.
         * 
         * @param modifier modifier to the stacklevel
         * @return new CodeV code walker
         */
        protected final Code newCodeV(final int modifier) {
            return new CodeV(beta, stacklevel + modifier);
        }
    }

    /**
     * Create a stream of instructions that will leave a {@code BasicValueObject} on the stack as a result.
     */
    private class CodeB extends Code {
        /**
         * Create a new CodeB.
         * 
         * @param beta variable context
         * @param stacklevel stack level
         */
        public CodeB(final HashMap<String, Position> beta, final int stacklevel) {
            super(beta, stacklevel);
        }

        @Override
        public void process(final BinOpExpression node) throws CompileException {
            node.getLhs().process(newCodeB(0));
            node.getRhs().process(newCodeB(1));
            emit(new OpBinInstruction(node, node.getOperator()));
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final ConsExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final HeadExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final IdentifierExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final IfExpression node) throws CompileException {
            final Label elseLabel = new Label("if else");
            final Label endLabel = new Label("if end");

            node.getCondition().process(newCodeB(0));
            emit(new JFalseInstruction(node, elseLabel));
            node.getThenExpression().process(newCodeB(0));
            emit(new UJmpInstruction(node, endLabel));

            elseLabel.setAddress(getCurrentPosition());
            node.getElseExpression().process(newCodeB(0));
            endLabel.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final IsNilExpression node) throws CompileException {
            node.process(newCodeV(0));
            emit(new GetBasicInstruction(node));
        }

        @Override
        public void process(final LambdaExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final LetrecExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final ListExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final NumeralExpression node) throws CompileException {
            emit(new LdBInstruction(node, node.getNumeral()));
        }

        @Override
        public void process(final Program node) throws CompileException {
            return;
        }

        @Override
        public void process(final TailExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final UnOpExpression node) throws CompileException {
            node.getExpression().process(newCodeB(0));
            emit(new OpUnInstruction(node, node.getOperator()));
        }

        @Override
        public void process(final VariableDeclaration node) throws CompileException {
            return;
        }

        /**
         * Everything else: Create CodeV instructions and fetch result with {@code getbasic}.
         * 
         * @param node expression
         * @throws CompileException on failure
         */
        private void vcode(final Expression node) throws CompileException {
            node.process(newCodeV(0));
            emit(new GetBasicInstruction(node));
        }
    }

    /**
     * Create a stream of instructions that will create a closure.
     */
    private class CodeC extends Code {
        /**
         * Create a new CodeC.
         * 
         * @param beta variable context
         * @param stacklevel stack level
         */
        public CodeC(final HashMap<String, Position> beta, final int stacklevel) {
            super(beta, stacklevel);
        }

        @Override
        public void process(final BinOpExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final ConsExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final HeadExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final IdentifierExpression node) throws CompileException {
            getVar(node.getIdentifier(), getBeta(), getStackLevel(), node);
        }

        @Override
        public void process(final IfExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final IsNilExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final LambdaExpression node) throws CompileException {
            final String name = "λ " + String.valueOf(node.getIdentifiers());
            final Label bodyLabel = new Label(name);
            final Label endLabel = new Label("λ end");

            final ArrayList<String> fr = freevar(node);
            pushfree(fr, getBeta(), getStackLevel(), node);
            emit(new MkVecInstruction(node, fr.size(), name + " Globals"));
            emit(new MkVecInstruction(node, 0, name + " Args"));
            emit(new LdLInstruction(node, bodyLabel));
            emit(new MkFunvalInstruction(node, name));

            emit(new UJmpInstruction(node, endLabel));
            bodyLabel.setAddress(getCurrentPosition());
            emit(new TargInstruction(node, node.getIdentifiers().size(), name));

            final HashMap<String, Position> c = new HashMap<String, Position>();
            for (int i = 0; i < node.getIdentifiers().size(); ++i) {
                c.put(node.getIdentifiers().get(i), new Position(Type.LOC, -i - 1));
            }

            for (int j = 0; j < fr.size(); ++j) {
                c.put(fr.get(j), new Position(Type.GLOB, j + 1));
            }

            node.getExpression().process(new CodeV(c, 0));
            emit(new ReturnInstruction(node, node.getIdentifiers().size()));
            endLabel.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final LetrecExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final ListExpression node) throws CompileException {
            node.process(newCodeV(0));
        }

        @Override
        public void process(final NumeralExpression node) throws CompileException {
            node.process(newCodeV(0));
        }

        @Override
        public void process(final Program node) throws CompileException {
            return;
        }

        @Override
        public void process(final TailExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final UnOpExpression node) throws CompileException {
            vcode(node);
        }

        @Override
        public void process(final VariableDeclaration node) throws CompileException {
            return;
        }

        /**
         * Create a closure for the given expression.
         * 
         * @param node expression
         * @throws CompileException on failure
         */
        private void vcode(final Expression node) throws CompileException {
            final Label endLabel = new Label("closure end");
            final Label bodyLabel = new Label("closure body");
            final ArrayList<String> fr = freevar(node);
            pushfree(fr, getBeta(), getStackLevel(), node);
            emit(new MkVecInstruction(node, fr.size(), "Variables"));
            emit(new LdLInstruction(node, bodyLabel));
            emit(new MkClosInstruction(node, node.toString()));
            emit(new UJmpInstruction(node, endLabel));
            bodyLabel.setAddress(getCurrentPosition());
            final HashMap<String, Position> c = new HashMap<String, Position>();
            for (int i = 0; i < fr.size(); ++i) {
                c.put(fr.get(i), new Position(Type.GLOB, i + 1));
            }
            node.process(new CodeV(c, 0));
            emit(new UpdateInstruction(node));
            endLabel.setAddress(getCurrentPosition());
        }
    }

    /**
     * Create a stream of instructions that will leave a pointer to the heap on the stack as a result.
     */
    private class CodeV extends Code {
        /**
         * Create a new CodeV.
         * 
         * @param beta variable context
         * @param stacklevel stack level
         */
        public CodeV(final HashMap<String, Position> beta, final int stacklevel) {
            super(beta, stacklevel);
        }

        @Override
        public void process(final BinOpExpression node) throws CompileException {
            node.process(newCodeB(0));
            emit(new MkBasicInstruction(node));
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            final Label returnAddr = new Label("Return addr");
            final ArrayList<Expression> rExp = new ArrayList<Expression>();
            rExp.addAll(node.getArguments());
            Collections.reverse(rExp);

            emit(new MarkInstruction(node, returnAddr));

            for (int i = 0; i < rExp.size(); ++i) {
                rExp.get(i).process(newCodeC(MaMa.FRAME_SIZE + i));
            }

            node.getBody().process(newCodeV(rExp.size() + MaMa.FRAME_SIZE));

            emit(new ApplyInstruction(node));
            returnAddr.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final ConsExpression node) throws CompileException {
            node.getTail().process(newCodeC(0));
            node.getHead().process(newCodeC(1));
            emit(new ConsInstruction(node));
        }

        @Override
        public void process(final HeadExpression node) throws CompileException {
            node.getExpression().process(newCodeV(0));
            emit(new HdInstruction(node));
            emit(new EvalInstruction(node));
        }

        @Override
        public void process(final IdentifierExpression node) throws CompileException {
            getVar(node.getIdentifier(), getBeta(), getStackLevel(), node);
            emit(new EvalInstruction(node));
        }

        @Override
        public void process(final IfExpression node) throws CompileException {
            final Label elseLabel = new Label("if else");
            final Label endLabel = new Label("if end");

            node.getCondition().process(newCodeB(0));
            emit(new JFalseInstruction(node, elseLabel));
            node.getThenExpression().process(newCodeV(0));
            emit(new UJmpInstruction(node, endLabel));

            elseLabel.setAddress(getCurrentPosition());
            node.getElseExpression().process(newCodeV(0));
            endLabel.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final IsNilExpression node) throws CompileException {
            node.getExpression().process(newCodeV(0));
            emit(new IsNilInstruction(node));
            emit(new MkBasicInstruction(node));
        }

        @Override
        public void process(final LambdaExpression node) throws CompileException {
            node.process(newCodeC(0));
        }

        @Override
        public void process(final LetrecExpression node) throws CompileException {
            final int n = node.getVariableDeclarations().size();
            for (int i = 0; i < n; ++i) {
                emit(new AllocInstruction(node, node.getVariableDeclarations().get(i).getIdentifier()));
            }

            final int stackLevelStar = getStackLevel() + n;

            final HashMap<String, Position> betaStar = new HashMap<String, Position>();
            betaStar.putAll(getBeta());

            for (int i = 0; i < node.getVariableDeclarations().size(); ++i) {
                betaStar.put(node.getVariableDeclarations().get(i).getIdentifier(), new Position(Type.LOC, getStackLevel() + i));
            }

            for (int i = 0; i < node.getVariableDeclarations().size(); ++i) {
                final HashMap<String, Position> localBeta = new HashMap<String, Position>();
                localBeta.putAll(betaStar);
                node.getVariableDeclarations().get(i).getExpression().process(new CodeC(localBeta, stackLevelStar));
                emit(new RewriteInstruction(node, n - i));
            }

            node.getExpression().process(new CodeV(betaStar, stackLevelStar));
            emit(new SlideInstruction(node, n));
        }

        @Override
        public void process(final ListExpression node) throws CompileException {
            emit(new NilInstruction(node));

            if (node.getExpressions() == null) {
                return;
            }

            for (int i = node.getExpressions().size() - 1; i >= 0; --i) {
                final Expression e = node.getExpressions().get(i);
                e.process(newCodeC(1));
                emit(new ConsInstruction(node));
            }
        }

        @Override
        public void process(final NumeralExpression node) throws CompileException {
            node.process(newCodeB(0));
            emit(new MkBasicInstruction(node));
        }

        @Override
        public void process(final Program node) throws CompileException {
            return;
        }

        @Override
        public void process(final TailExpression node) throws CompileException {
            node.getExpression().process(newCodeV(0));
            emit(new TlInstruction(node));
            emit(new EvalInstruction(node));
        }

        @Override
        public void process(final UnOpExpression node) throws CompileException {
            node.process(newCodeB(0));
            emit(new MkBasicInstruction(node));
        }

        @Override
        public void process(final VariableDeclaration node) throws CompileException {
            return;
        }
    }

    /**
     * Create a new MaMaCompiler.
     */
    public MaMaCompiler() {
    }

    /**
     * Returns the list of free variables in the given expression.
     * 
     * @param e expression
     * @return the list of free variables
     * @throws CompileException on failure
     */
    protected final ArrayList<String> freevar(final Expression e) throws CompileException {
        final FreeVar freeVar = new FreeVar();
        e.process(freeVar);
        return freeVar.getVariables();
    }

    /**
     * Emits instructions that will load the local argument or global variable with the given name.
     * 
     * @param v variable name
     * @param beta variable context
     * @param stacklevel current stack level
     * @param node parent AST node
     * @throws CompileException on failure
     */
    protected final void getVar(final String v, final HashMap<String, Position> beta, final int stacklevel, final MaMaAstNode node) throws CompileException {
        final Position position = beta.get(v);

        if (position == null) {
            throw new CompileException(node, "unknown variable: " + v);
        }

        if (position.getType() == Position.Type.LOC) {
            emit(new PushLocInstruction(node, stacklevel - position.getPosition()));
        } else {
            emit(new PushGlobInstruction(node, position.getPosition(), v));
        }
    }

    @Override
    public final void processProgram(final AstNode<?> program) throws CompileException {
        try {
            final Program programNode = (Program) program;
            programNode.getExpression().process(new CodeV(new HashMap<String, Position>(), 0));
            emit(new HaltInstruction(programNode));
        } catch (final RuntimeException e) {
            throw new CompileException(program, e);
        }
    }

    /**
     * Emits instructions that will create the vector of global variables in "{@code C_code (\v1 ... vn.e) beta stacklevel}".
     * 
     * @param v list of free variables in the current expression
     * @param beta variable context
     * @param stacklevel current stack level
     * @param node parent AST node
     * @throws CompileException on failure
     */
    protected final void pushfree(final ArrayList<String> v, final HashMap<String, Position> beta, final int stacklevel, final MaMaAstNode node) throws CompileException {
        for (int i = 0; i < v.size(); ++i) {
            getVar(v.get(i), beta, stacklevel + i, node);
        }
    }
}
