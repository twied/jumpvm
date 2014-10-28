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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jumpvm.ast.AstNode;
import jumpvm.ast.pama.CaseLimb;
import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.DesignatorPart;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.NamedNode;
import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.ast.pama.Program;
import jumpvm.ast.pama.Range;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.Type;
import jumpvm.ast.pama.declarations.FuncDecl;
import jumpvm.ast.pama.declarations.TypeDecl;
import jumpvm.ast.pama.declarations.VarDecl;
import jumpvm.ast.pama.declarations.VarDecl.Source;
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
import jumpvm.code.pama.AddInstruction;
import jumpvm.code.pama.AndInstruction;
import jumpvm.code.pama.ChdInstruction;
import jumpvm.code.pama.CupInstruction;
import jumpvm.code.pama.CupiInstruction;
import jumpvm.code.pama.DivInstruction;
import jumpvm.code.pama.DplInstruction;
import jumpvm.code.pama.EquInstruction;
import jumpvm.code.pama.FjpInstruction;
import jumpvm.code.pama.GeqInstruction;
import jumpvm.code.pama.GrtInstruction;
import jumpvm.code.pama.HighInstruction;
import jumpvm.code.pama.IncInstruction;
import jumpvm.code.pama.IndInstruction;
import jumpvm.code.pama.IxaInstruction;
import jumpvm.code.pama.IxjInstruction;
import jumpvm.code.pama.LdaInstruction;
import jumpvm.code.pama.LdcInstruction;
import jumpvm.code.pama.LddInstruction;
import jumpvm.code.pama.LeqInstruction;
import jumpvm.code.pama.LesInstruction;
import jumpvm.code.pama.LodInstruction;
import jumpvm.code.pama.LowInstruction;
import jumpvm.code.pama.MovdInstruction;
import jumpvm.code.pama.MovsInstruction;
import jumpvm.code.pama.MstInstruction;
import jumpvm.code.pama.MstfInstruction;
import jumpvm.code.pama.MulInstruction;
import jumpvm.code.pama.NegInstruction;
import jumpvm.code.pama.NeqInstruction;
import jumpvm.code.pama.NewInstruction;
import jumpvm.code.pama.NotInstruction;
import jumpvm.code.pama.OrInstruction;
import jumpvm.code.pama.PopInstruction;
import jumpvm.code.pama.ReadlnInstruction;
import jumpvm.code.pama.RetfInstruction;
import jumpvm.code.pama.RetpInstruction;
import jumpvm.code.pama.SepInstruction;
import jumpvm.code.pama.SliInstruction;
import jumpvm.code.pama.SmpInstruction;
import jumpvm.code.pama.SofInstruction;
import jumpvm.code.pama.SspInstruction;
import jumpvm.code.pama.StoInstruction;
import jumpvm.code.pama.StpInstruction;
import jumpvm.code.pama.SubInstruction;
import jumpvm.code.pama.UjpInstruction;
import jumpvm.code.pama.WritelnInstruction;
import jumpvm.compiler.Compiler;
import jumpvm.exception.CompileException;
import jumpvm.memory.Label;
import jumpvm.vm.PaMa;

/** PaMachine {@link Compiler}. */
public class PaMaCompiler extends Compiler {
    /** PaMachine code walker. */
    public abstract class Code implements PaMaAstWalker {
        /** Address environment. */
        private final HashMap<NamedNode, Position> rho;

        /** Current stacking depth. */
        private final int depth;

        /** Current procedure. */
        private final String currentProcedure;

        /** Next free address in address environment. */
        private int nextAddress;

        /**
         * Copy a PaMachine code walker.
         *
         * @param other PaMachine code walker
         */
        private Code(final Code other) {
            this(other.currentProcedure, other.rho, other.depth, other.nextAddress);
        }

        /**
         * Copy a PaMachine code walker and modify the state.
         *
         * @param other PaMachine code walker
         * @param currentProcedure current procedure
         * @param depthModifier modifier to the stacking depth
         */
        public Code(final Code other, final String currentProcedure, final int depthModifier) {
            this(currentProcedure, other.rho, other.depth + depthModifier, PaMa.FRAME_SIZE);
        }

        /**
         * Create a new PaMachine code walker with the given settings.
         *
         * @param currentProcedure current procedure
         * @param rho address environment
         * @param depth stacking depth
         * @param nextAddress next free address in address environment
         */
        private Code(final String currentProcedure, final HashMap<NamedNode, Position> rho, final int depth, final int nextAddress) {
            this.currentProcedure = currentProcedure;
            this.rho = new HashMap<NamedNode, Position>();
            this.rho.putAll(rho);
            this.depth = depth;
            this.nextAddress = nextAddress;
        }

        /**
         * Create a new CodeL object and process the node with it.
         *
         * @param node node
         * @throws CompileException on failure
         */
        protected final void codeL(final PaMaAstNode node) throws CompileException {
            node.process(new CodeL(this));
        }

        /**
         * Create a new CodeR object and process the node with it.
         *
         * @param node node
         * @throws CompileException on failure
         */
        protected final void codeR(final PaMaAstNode node) throws CompileException {
            node.process(new CodeR(this));
        }

        /**
         * Create a new CodeSt object and process the node with it.
         *
         * @param node node
         * @throws CompileException on failure
         */
        protected final void codeSt(final PaMaAstNode node) throws CompileException {
            node.process(new CodeSt(this));
        }

        /**
         * Create a new CodeSt object and process the node with it.
         *
         * @param node node
         * @param procedureName procedure name
         * @param depthmodifier stacking depth modifier
         * @throws CompileException on failure
         */
        protected final void codeSt(final PaMaAstNode node, final String procedureName, final int depthmodifier) throws CompileException {
            node.process(new CodeSt(this, procedureName, depthmodifier));
        }

        /**
         * Include the procedure / function declarations in the address environment. Translate them as well.
         *
         * @param procDeclList list of procedure declarations
         * @throws CompileException on failure
         */
        protected final void elabPdecls(final ArrayList<FuncDecl> procDeclList) throws CompileException {
            final HashMap<FuncDecl, Position> procDeclMap = new HashMap<FuncDecl, Position>();

            /* create label for each procedure / function */
            for (final FuncDecl procDecl : procDeclList) {
                final Position position = new Position(new Label(procDecl.getIdentifier()), getDepth());
                setPosition(procDecl, position);
                procDeclMap.put(procDecl, position);
            }

            /* translate each procedure / function */
            for (final FuncDecl procDecl : procDeclList) {
                procDeclMap.get(procDecl).getAddress().setAddress(getCurrentPosition());
                codeSt(procDecl, procDecl.getIdentifier(), 1);
            }
        }

        /**
         * Include the parameters in the address environment.
         *
         * @param formalParameterList list of parameters
         */
        protected final void elabSpecs(final ArrayList<FormalParameter> formalParameterList) {
            for (final FormalParameter formalParameter : formalParameterList) {
                setPosition(formalParameter, new Position(nextAddress, getDepth()));
                if (formalParameter.isReference()) {
                    nextAddress += 1;
                } else if (formalParameter.getType().getResolvedType() instanceof ArrayType) {
                    nextAddress += ((ArrayType) formalParameter.getType().getResolvedType()).getDescriptorSize();
                } else {
                    nextAddress += formalParameter.getType().getSize();
                }
            }
        }

        /**
         * Include the local variables in the address environment.
         *
         * @param varDeclList list of variable declarations
         * @return maximum stack usage to prepare the environment
         */
        protected final int elabVdecls(final ArrayList<VarDecl> varDeclList) {
            int maxStack = 0;
            for (final VarDecl varDecl : varDeclList) {
                if (varDecl.getSource() == Source.REFERENCE_PARAMETER) {
                    if (varDecl.getType().getResolvedType() instanceof ArrayType) {
                        maxStack = Math.max(maxStack, varDecl.getType().getSize());
                    }
                    continue;
                }

                setPosition(varDecl, new Position(nextAddress, getDepth()));
                nextAddress += varDecl.getType().getSize();
                if (varDecl.getType().getResolvedType() instanceof ArrayType) {
                    nextAddress += ((ArrayType) varDecl.getType().getResolvedType()).getDescriptorSize();
                    maxStack = 2;
                }
            }
            return maxStack;
        }

        /**
         * Helper function for the translation of {@link CallExpression}s and {@link CallStatement}.
         *
         * @param node node
         * @param identifier function name
         * @param expressionList argument list
         * @throws CompileException on failure
         */
        protected final void emitCall(final PaMaAstNode node, final String identifier, final ArrayList<Expression> expressionList) throws CompileException {
            final Position positionDirect = getPositionOfFunction(identifier);
            final Position positionParameterValue = getPositionOfParameterValue(identifier);
            final Position positionParameterReference = getPositionOfParameterReference(identifier);
            final Position positionVariable = getPositionOfVariable(identifier);

            final Position position;
            final boolean direct;

            if (positionDirect == null) {
                if (positionParameterValue != null) {
                    position = positionParameterValue;
                } else if (positionParameterReference != null) {
                    position = positionParameterReference;
                } else if (positionVariable != null) {
                    position = positionVariable;
                } else {
                    throw new CompileException(node);
                }
                direct = false;
            } else {
                position = positionDirect;
                direct = true;
            }

            final int depthDifference = getDepth() - position.getDepth();
            if (direct) {
                emit(new MstInstruction(node, depthDifference, identifier));
            } else {
                emit(new MstfInstruction(node, depthDifference, position.getAddress().getAddress(), identifier));
            }

            int s = 0;
            for (int i = 0; i < expressionList.size(); ++i) {
                final Expression expression = expressionList.get(i);
                final Type type = expression.getType().getResolvedType();

                if (expression.isReference()) {
                    codeL(expression);
                    s += 1;
                } else if (type instanceof RecordType) {
                    codeL(expression);
                    emit(new MovsInstruction(expression, type.getSize(), expression.getType().toString()));
                    s += type.getSize();
                } else if (type instanceof ArrayType) {
                    final int movssize = ((ArrayType) type).getDescriptorSize();
                    codeL(expression);
                    emit(new MovsInstruction(expression, movssize, expression.getType().toString()));
                    s += movssize;
                } else if (type instanceof FunctionType) {
                    codeL(expression);
                    s += type.getSize();
                } else {
                    codeR(expression);
                    s += type.getSize();
                }
            }

            if (direct) {
                emit(new CupInstruction(node, s, position.getAddress()));
            } else {
                emit(new SmpInstruction(node, s));
                emit(new CupiInstruction(node, depthDifference, position.getAddress().getAddress(), identifier));
            }
        }

        /**
         * Returns the stacking depth.
         *
         * @return the stacking depth
         */
        protected final int getDepth() {
            return depth;
        }

        /**
         * Returns the next free address in the address environment.
         *
         * @return the next free address in the address environment
         */
        protected final int getNextAddress() {
            return nextAddress;
        }

        /**
         * Returns the position of the function / procedure with the given name.
         *
         * @param identifier name of the declaration
         * @return the position or {@code null} if there is no such declaration
         */
        protected final Position getPositionOfFunction(final String identifier) {
            for (final Entry<NamedNode, Position> entry : rho.entrySet()) {
                if (entry.getKey() instanceof FuncDecl) {
                    if (identifier.equals(((FuncDecl) entry.getKey()).getIdentifier())) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        /**
         * Returns the position of the reference parameter with the given name.
         *
         * @param identifier name of the declaration
         * @return the position or {@code null} if there is no such declaration
         */
        protected final Position getPositionOfParameterReference(final String identifier) {
            for (final Entry<NamedNode, Position> entry : rho.entrySet()) {
                if (entry.getKey() instanceof FormalParameter) {
                    final FormalParameter formalParameter = (FormalParameter) entry.getKey();
                    if (formalParameter.isReference() && identifier.equals(formalParameter.getIdentifier())) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        /**
         * Returns the position of the value parameter with the given name.
         *
         * @param identifier name of the declaration
         * @return the position or {@code null} if there is no such declaration
         */
        protected final Position getPositionOfParameterValue(final String identifier) {
            for (final Entry<NamedNode, Position> entry : rho.entrySet()) {
                if (entry.getKey() instanceof FormalParameter) {
                    final FormalParameter formalParameter = (FormalParameter) entry.getKey();
                    if (!formalParameter.isReference() && identifier.equals(formalParameter.getIdentifier())) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        /**
         * Returns the position of the variable with the given name.
         *
         * @param identifier name of the declaration
         * @return the position or {@code null} if there is no such declaration
         */
        protected final Position getPositionOfVariable(final String identifier) {
            for (final Entry<NamedNode, Position> entry : rho.entrySet()) {
                if (entry.getKey() instanceof VarDecl) {
                    if (identifier.equals(((VarDecl) entry.getKey()).getIdentifier())) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        /**
         * Returns the position of the function / procedure with the given name.
         *
         * @param identifier name of the declaration
         * @return the position or {@code null} if there is no such declaration
         */

        protected final FuncDecl getProcDecl(final String identifier) {
            for (final Object key : rho.keySet()) {
                if ((key instanceof FuncDecl) && identifier.equals(((FuncDecl) key).getIdentifier())) {
                    return (FuncDecl) key;
                }
            }
            return null;
        }

        /**
         * Returns if the designator denotes the current procedure.
         *
         * @param designator designator
         * @return true if current procedure
         */
        protected final boolean isCurrentProcedure(final Designator designator) {
            return currentProcedure.equals(designator.getIdentifier()) && designator.getDesignatorPartList().isEmpty();
        }

        /**
         * Set the position of a declaration in the address environment.
         *
         * @param object declaration
         * @param position position
         */
        protected final void setPosition(final NamedNode object, final Position position) {
            final ArrayList<NamedNode> remove = new ArrayList<NamedNode>();
            for (final NamedNode node : rho.keySet()) {
                if (node.getIdentifier().equals(object.getIdentifier())) {
                    remove.add(node);
                }
            }
            for (final NamedNode node : remove) {
                rho.remove(node);
            }
            rho.put(object, position);
        }
    }

    /**
     * Code functions to create the address (l-value) of an expression.
     */
    private class CodeL extends Code {
        /**
         * Create a new CodeL instance.
         *
         * @param code address environment etc. to inherit
         */
        public CodeL(final Code code) {
            super(code);
        }

        @Override
        public void process(final ArrayType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final AssignmentStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final BooleanExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final BooleanType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CallStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CaseLimb node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CaseStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ConjunctionExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CustomType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Declarations node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Designator node) throws CompileException {
            Position position;
            /* is a reference parameter */
            position = getPositionOfParameterReference(node.getIdentifier());
            if (position != null) {
                emit(new LodInstruction(node, getDepth() - position.getDepth(), position.getAddress().getAddress(), node.getIdentifier()));
                for (final DesignatorPart designatorPart : node.getDesignatorPartList()) {
                    designatorPart.process(this);
                }
                return;
            }

            /* is a value parameter */
            position = getPositionOfParameterValue(node.getIdentifier());
            if (position != null) {
                emit(new LdaInstruction(node, getDepth() - position.getDepth(), position.getAddress().getAddress(), node.getIdentifier()));
                for (final DesignatorPart designatorPart : node.getDesignatorPartList()) {
                    designatorPart.process(this);
                }
                return;
            }

            /* is a value */
            position = getPositionOfVariable(node.getIdentifier());
            if (position != null) {
                emit(new LdaInstruction(node, getDepth() - position.getDepth(), position.getAddress().getAddress(), node.getIdentifier()));
                for (final DesignatorPart designatorPart : node.getDesignatorPartList()) {
                    designatorPart.process(this);
                }
                return;
            }

            /* is current procedure */
            if (isCurrentProcedure(node)) {
                emit(new LdaInstruction(node, 0, 0, node.getIdentifier()));
                return;
            }

            /* is another procedure */
            position = getPositionOfFunction(node.getIdentifier());
            if (position != null) {
                emit(new LdcInstruction(node, position.getAddress().getAddress(), position.getAddress().getName(), null));
                emit(new LdaInstruction(node, getDepth() - position.getDepth(), 0, node.getIdentifier()));
                return;
            }

            throw new CompileException(node);
        }

        @Override
        public void process(final DisjunctionExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FormalParameter node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ForStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FuncDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FunctionType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final HighExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IfStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public final void process(final IndirectionDesignatorPart node) throws CompileException {
            emit(new IndInstruction(node));
        }

        @Override
        public void process(final IntegerExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IntegerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ListAccessDesignatorPart node) throws CompileException {
            emit(new DplInstruction(node));
            emit(new IndInstruction(node));
            emit(new LdcInstruction(node, 0, null, "Subtractor"));
            final int size = 2 * (node.getExpressionList().size() + 1);
            for (int i = 0; i < (node.getExpressionList().size()); ++i) {
                codeR(node.getExpressionList().get(i));
                emit(new ChdInstruction(node, i + 1));
                emit(new AddInstruction(node));
                if (i < (node.getExpressionList().size() - 1)) {
                    emit(new LddInstruction(node, size + i + 1));
                    emit(new MulInstruction(node));
                }
            }
            emit(new IxaInstruction(node, ((ArrayType) node.getType()).getBaseType().getSize()));
            emit(new SliInstruction(node));
        }

        @Override
        public void process(final LowExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final NewStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final NilExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final PointerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Program node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ReadlnStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RecordType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RelationalExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RepeatStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public final void process(final SelectorDesignatorPart node) throws CompileException {
            final RecordType type = (RecordType) node.getType();
            int offset = 0;
            for (final VarDecl varDecl : type.getVarDeclList()) {
                if (varDecl.getIdentifier().equals(node.getIdentifier())) {
                    emit(new IncInstruction(node, offset));
                    return;
                }
                offset += varDecl.getType().getSize();
            }
            throw new CompileException(node);
        }

        @Override
        public void process(final TypeDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final UnaryExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final VarDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final WhileStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final WritelnStatement node) throws CompileException {
            throw new CompileException(node);
        }
    }

    /**
     * Code functions to create the value (r-value) of an expression.
     */
    private class CodeR extends Code {
        /**
         * Create a new CodeR instance.
         *
         * @param code address environment etc. to inherit
         */
        public CodeR(final Code code) {
            super(code);
        }

        @Override
        public void process(final ArrayType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final AssignmentStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public final void process(final BooleanExpression node) throws CompileException {
            if (node.getValue()) {
                emit(new LdcInstruction(node, 1, "true", "true"));
            } else {
                emit(new LdcInstruction(node, 0, "false", "false"));
            }
        }

        @Override
        public void process(final BooleanType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            emitCall(node, node.getIdentifier(), node.getExpressionList());
        }

        @Override
        public void process(final CallStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CaseLimb node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CaseStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ConjunctionExpression node) throws CompileException {
            node.getLhs().process(this);
            node.getRhs().process(this);

            switch (node.getOperator()) {
            case ADD:
                emit(new AddInstruction(node));
                break;
            case OR:
                emit(new OrInstruction(node));
                break;
            case SUB:
                emit(new SubInstruction(node));
                break;
            default:
                throw new CompileException(node);
            }
        }

        @Override
        public void process(final CustomType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Declarations node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Designator node) throws CompileException {
            codeL(node);
            emit(new IndInstruction(node));
        }

        @Override
        public void process(final DisjunctionExpression node) throws CompileException {
            node.getLhs().process(this);
            node.getRhs().process(this);

            switch (node.getOperator()) {
            case AND:
                emit(new AndInstruction(node));
                break;
            case DIV:
                emit(new DivInstruction(node));
                break;
            case MUL:
                emit(new MulInstruction(node));
                break;
            default:
                throw new CompileException(node);
            }
        }

        @Override
        public void process(final FormalParameter node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ForStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FuncDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FunctionType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final HighExpression node) throws CompileException {
            codeL(node.getDesignator());
            emit(new LdcInstruction(node, node.getDimension(), null, "high " + node.getDimension()));
            emit(new HighInstruction(node));
        }

        @Override
        public void process(final IfStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IndirectionDesignatorPart node) throws CompileException {
            /* handled by CodeL */
            throw new CompileException(node);
        }

        @Override
        public void process(final IntegerExpression node) throws CompileException {
            emit(new LdcInstruction(node, node.getValue(), null, null));
        }

        @Override
        public void process(final IntegerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ListAccessDesignatorPart node) throws CompileException {
            /* handled by CodeL */
            throw new CompileException(node);
        }

        @Override
        public void process(final LowExpression node) throws CompileException {
            codeL(node.getDesignator());
            emit(new LdcInstruction(node, node.getDimension(), null, "low " + node.getDimension()));
            emit(new LowInstruction(node));
        }

        @Override
        public void process(final NewStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final NilExpression node) throws CompileException {
            emit(new LdcInstruction(node, 0, "nil", null));
        }

        @Override
        public void process(final PointerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Program node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ReadlnStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RecordType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public final void process(final RelationalExpression node) throws CompileException {
            node.getLhs().process(this);
            node.getRhs().process(this);

            switch (node.getOperator()) {
            case EQL:
                emit(new EquInstruction(node));
                break;
            case GT:
                emit(new GrtInstruction(node));
                break;
            case GTE:
                emit(new GeqInstruction(node));
                break;
            case LT:
                emit(new LesInstruction(node));
                break;
            case LTE:
                emit(new LeqInstruction(node));
                break;
            case NEQ:
                emit(new NeqInstruction(node));
                break;
            default:
                throw new CompileException(node);
            }
        }

        @Override
        public void process(final RepeatStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final SelectorDesignatorPart node) throws CompileException {
            /* handled by CodeL */
            throw new CompileException(node);
        }

        @Override
        public void process(final TypeDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final UnaryExpression node) throws CompileException {
            node.getExpression().process(this);
            switch (node.getOperator()) {
            case NEG:
                emit(new NegInstruction(node));
                break;
            case NOT:
                emit(new NotInstruction(node));
                break;
            default:
                throw new CompileException(node);
            }
        }

        @Override
        public void process(final VarDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final WhileStatement node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final WritelnStatement node) throws CompileException {
            throw new CompileException(node);
        }
    }

    /**
     * Code function to translates a statement of a PaMa program.
     */
    private class CodeSt extends Code {
        /**
         * Create a new CodeSt instance.
         */
        public CodeSt() {
            super("_", new HashMap<NamedNode, Position>(), 1, PaMa.FRAME_SIZE);
        }

        /**
         * Create a new CodeSt instance with the same settings as the given code walker.
         *
         * @param code code walker
         */
        private CodeSt(final Code code) {
            super(code);
        }

        /**
         * Create a new CodeSt instance with the same settings as the given code walker, but a different current procedure and stacking depth.
         *
         * @param code code walker
         * @param currentProcedure current procedure
         * @param depthModifier stacking depth modifier
         */
        private CodeSt(final Code code, final String currentProcedure, final int depthModifier) {
            super(code, currentProcedure, depthModifier);
        }

        @Override
        public void process(final ArrayType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final AssignmentStatement node) throws CompileException {
            codeL(node.getDesignator());
            codeR(node.getExpression());
            emit(new StoInstruction(node, node.getDesignator().getFullIdentifier()));
        }

        @Override
        public void process(final BooleanExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final BooleanType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CallExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CallStatement node) throws CompileException {
            emitCall(node, node.getIdentifier(), node.getExpressionList());
        }

        @Override
        public void process(final CaseLimb node) throws CompileException {
            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }
        }

        @Override
        public void process(final CaseStatement node) throws CompileException {
            final Label jumpTable = new Label("jump table");
            final HashMap<CaseLimb, Label> limbMap = new HashMap<CaseLimb, Label>();

            codeR(node.getExpression());
            emit(new NegInstruction(node));
            emit(new IxjInstruction(node, jumpTable));

            for (final CaseLimb caseLimb : node.getCaseLimbList()) {
                limbMap.put(caseLimb, new Label("case " + caseLimb.getValue(), getCurrentPosition()));
                caseLimb.process(this);
                emit(new UjpInstruction(caseLimb, jumpTable));
            }

            for (int i = node.getCaseLimbList().size() - 1; i >= 0; --i) {
                final CaseLimb caseLimb = node.getCaseLimbList().get(i);
                emit(new UjpInstruction(caseLimb, limbMap.get(caseLimb)));
            }
            jumpTable.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final ConjunctionExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final CustomType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Declarations node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Designator node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final DisjunctionExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final FormalParameter node) throws CompileException {
            if ((node.getType().getResolvedType() instanceof ArrayType) && !node.isReference()) {
                emit(new MovdInstruction(node, getPositionOfParameterValue(node.getIdentifier()).getAddress().getAddress(), node.getIdentifier()));
            }
        }

        @Override
        public void process(final ForStatement node) throws CompileException {
            /* counter := from_expression */
            codeL(node.getDesignator());
            codeR(node.getFromExpression());
            emit(new StoInstruction(node, node.getDesignator().getFullIdentifier()));

            /* put to_expression on the stack */
            codeR(node.getToExpression());

            final Label begin = new Label("beginfor", getCurrentPosition());
            final Label end = new Label("endfor");

            /* if to_expression == counter then goto end_of_for */
            emit(new DplInstruction(node));
            codeR(node.getDesignator());
            emit(new GeqInstruction(node));
            emit(new FjpInstruction(node, end));

            /* code statements */
            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }

            /* increment counter */
            codeL(node.getDesignator());
            codeR(node.getDesignator());
            emit(new LdcInstruction(node, 1, null, null));
            emit(new AddInstruction(node));
            emit(new StoInstruction(node, node.getDesignator().getFullIdentifier()));

            /* continue from start */
            emit(new UjpInstruction(node, begin));
            end.setAddress(getCurrentPosition());

            /* remove to_expression from the stack */
            emit(new PopInstruction(node));
        }

        @Override
        public void process(final FuncDecl node) throws CompileException {
            final Label l = new Label(node.getIdentifier());
            elabSpecs(node.getFormalParameterList());
            final int maxElabStack = Math.max(elabVdecls(node.getDeclarations().getVarDeclList()), node.getMaxStackSize());
            emit(new SspInstruction(node, getNextAddress(), node.getIdentifier()));
            emit(new SepInstruction(node, maxElabStack));

            /* codeS */
            for (final FormalParameter formalParameter : node.getFormalParameterList()) {
                formalParameter.process(this);
            }
            /* codeP */
            for (final VarDecl varDecl : node.getDeclarations().getVarDeclList()) {
                varDecl.process(this);
            }

            emit(new UjpInstruction(node, l));
            elabPdecls(node.getDeclarations().getProcDeclList());

            l.setAddress(getCurrentPosition());
            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }

            if (node.getReturnType() == null) {
                emit(new RetpInstruction(node));
            } else {
                emit(new RetfInstruction(node));
            }
        }

        @Override
        public void process(final FunctionType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final HighExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IfStatement node) throws CompileException {
            final Label labelEndIf = new Label("endif");

            codeR(node.getCondition());
            if (node.getElseStatementList().isEmpty()) {
                emit(new FjpInstruction(node, labelEndIf));
                for (final Statement statement : node.getThenStatementList()) {
                    statement.process(this);
                }
            } else {
                final Label labelElse = new Label("else");
                emit(new FjpInstruction(node, labelElse));
                for (final Statement statement : node.getThenStatementList()) {
                    statement.process(this);
                }
                emit(new UjpInstruction(node, labelEndIf));
                labelElse.setAddress(getCurrentPosition());
                for (final Statement statement : node.getElseStatementList()) {
                    statement.process(this);
                }
            }
            labelEndIf.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final IndirectionDesignatorPart node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IntegerExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final IntegerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final ListAccessDesignatorPart node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final LowExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final NewStatement node) throws CompileException {
            codeL(node.getDesignator());
            final Type type = ((PointerType) node.getDesignator().getType().getResolvedType()).getType().getResolvedType();
            final String identifier = node.getDesignator().getIdentifier();

            if (type instanceof ArrayType) {
                final ArrayType arrayType = (ArrayType) type;
                emit(new LdcInstruction(node, arrayType.getDescriptorSize() + arrayType.getSize(), "size", "size of array " + node.getDesignator().getIdentifier()));
                emit(new NewInstruction(node, identifier));

                int subtractor = 0;
                for (int i = 0; i < arrayType.getDimensions().size(); ++i) {
                    int size = 1;
                    for (int j = i + 1; j < arrayType.getDimensions().size(); ++j) {
                        final Range range = arrayType.getDimensions().get(j);
                        size *= (range.getHigh() - range.getLow()) + 1;
                    }
                    subtractor += arrayType.getDimensions().get(i).getLow() * size;
                }

                codeR(node.getDesignator());
                emit(new SofInstruction(arrayType, 1, arrayType.getSize(), "size", "Size of " + identifier));
                emit(new SofInstruction(arrayType, 2, subtractor, "subtractor", "Subtractor of " + identifier));
                for (int i = 0; i < arrayType.getDimensions().size(); ++i) {
                    final Range range = arrayType.getDimensions().get(i);
                    emit(new SofInstruction(arrayType, (2 * (i + 1)) + 1, range.getLow(), "u" + (i + 1), "Lower bound of dimension " + (i + 1)));
                    emit(new SofInstruction(arrayType, (2 * (i + 1)) + 2, range.getHigh(), "o" + (i + 1), "Upper bound of dimension " + (i + 1)));
                }
                for (int i = 1; i < arrayType.getDimensions().size(); ++i) {
                    final Range range = arrayType.getDimensions().get(i);
                    final int rangeSize = 1 + (range.getHigh() - range.getLow());
                    emit(new SofInstruction(arrayType, (arrayType.getDimensions().size() * 2) + 2 + i, rangeSize, "d" + (i + 1), "span of dimension " + (i + 1)));
                }

                emit(new DplInstruction(arrayType));
                emit(new IncInstruction(arrayType, arrayType.getDescriptorSize()));
                emit(new IncInstruction(arrayType, -subtractor));
                emit(new StoInstruction(arrayType, identifier));

            } else {
                emit(new LdcInstruction(node, type.getSize(), "size", "Size of " + identifier));
                emit(new NewInstruction(node, identifier));
            }
        }

        @Override
        public void process(final NilExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final PointerType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Program node) throws CompileException {
            final Label l = new Label("main");
            final int maxElabStack = Math.max(elabVdecls(node.getDeclarations().getVarDeclList()), node.getMaxStackSize());
            emit(new SspInstruction(node, getNextAddress(), "main"));
            emit(new SepInstruction(node, maxElabStack));

            /* codeP */
            for (final VarDecl varDecl : node.getDeclarations().getVarDeclList()) {
                varDecl.process(this);
            }

            emit(new UjpInstruction(node, l));
            elabPdecls(node.getDeclarations().getProcDeclList());

            l.setAddress(getCurrentPosition());
            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }

            emit(new StpInstruction(node));
        }

        @Override
        public void process(final ReadlnStatement node) throws CompileException {
            codeL(node.getDesignator());
            emit(new ReadlnInstruction(node));
            emit(new StoInstruction(node, node.getDesignator().getFullIdentifier()));
        }

        @Override
        public void process(final RecordType node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RelationalExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final RepeatStatement node) throws CompileException {
            final Label label = new Label("repeat", getCurrentPosition());
            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }
            codeR(node.getCondition());
            emit(new FjpInstruction(node, label));
        }

        @Override
        public void process(final SelectorDesignatorPart node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final TypeDecl node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final UnaryExpression node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final VarDecl node) throws CompileException {
            if (node.getSource() != Source.LOCAL_DECLARATION) {
                return;
            }

            if (!(node.getType().getResolvedType() instanceof ArrayType)) {
                return;
            }

            final String identifier = node.getIdentifier();
            final ArrayType type = (ArrayType) node.getType().getResolvedType();
            final int address = getPositionOfVariable(identifier).getAddress().getAddress();

            int subtractor = 0;
            for (int i = 0; i < type.getDimensions().size(); ++i) {
                int size = 1;
                for (int j = i + 1; j < type.getDimensions().size(); ++j) {
                    final Range range = type.getDimensions().get(j);
                    size *= (range.getHigh() - range.getLow()) + 1;
                }
                subtractor += type.getDimensions().get(i).getLow() * size;
            }

            emit(new LdaInstruction(type, 0, address, identifier));
            emit(new SofInstruction(type, 1, type.getSize(), "size", "Size of " + identifier));
            emit(new SofInstruction(type, 2, subtractor, "subtractor", "Subtractor of " + identifier));
            for (int i = 0; i < type.getDimensions().size(); ++i) {
                final Range range = type.getDimensions().get(i);
                emit(new SofInstruction(type, (2 * (i + 1)) + 1, range.getLow(), "u" + (i + 1), "Lower bound of dimension " + (i + 1)));
                emit(new SofInstruction(type, (2 * (i + 1)) + 2, range.getHigh(), "o" + (i + 1), "Upper bound of dimension " + (i + 1)));
            }
            for (int i = 1; i < type.getDimensions().size(); ++i) {
                final Range range = type.getDimensions().get(i);
                final int rangeSize = 1 + (range.getHigh() - range.getLow());
                emit(new SofInstruction(type, (type.getDimensions().size() * 2) + 2 + i, rangeSize, "d" + (i + 1), "span of dimension " + (i + 1)));
            }

            emit(new LdaInstruction(type, 0, address + type.getDescriptorSize(), "descriptor of " + identifier));
            emit(new IncInstruction(type, -subtractor));
            emit(new StoInstruction(type, identifier));
        }

        @Override
        public void process(final WhileStatement node) throws CompileException {
            final Label labelWhile = new Label("while");
            final Label labelEndWhile = new Label("endwhile");

            labelWhile.setAddress(getCurrentPosition());
            codeR(node.getCondition());
            emit(new FjpInstruction(node, labelEndWhile));

            for (final Statement statement : node.getStatementList()) {
                statement.process(this);
            }

            emit(new UjpInstruction(node, labelWhile));
            labelEndWhile.setAddress(getCurrentPosition());
        }

        @Override
        public void process(final WritelnStatement node) throws CompileException {
            codeR(node.getExpression());
            emit(new WritelnInstruction(node));
        }
    }

    /**
     * Create a new PaMaCompiler.
     */
    public PaMaCompiler() {
        super();
    }

    @Override
    public final void processProgram(final AstNode<?> programNode) throws CompileException {
        final Program program = (Program) programNode;
        program.process(new TypeCheck());
        program.process(new CodeSt());
    }
}
