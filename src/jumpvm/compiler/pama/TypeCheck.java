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

import jumpvm.ast.pama.CaseLimb;
import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.DesignatorPart;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.ast.pama.Program;
import jumpvm.ast.pama.Range;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.Type;
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
import jumpvm.ast.pama.types.NilPointerType;
import jumpvm.ast.pama.types.PointerType;
import jumpvm.ast.pama.types.ProcedureType;
import jumpvm.ast.pama.types.RecordType;
import jumpvm.exception.CompileException;

/**
 * PaMachine type checker.
 *
 * Ensures that expressions evaluate to the expected type i.e. in assignments. Also resolve types.
 */
public class TypeCheck implements PaMaAstWalker {
    /** All currently known declarations. */
    private final Declarations declarations;

    /**
     * Create a new TypeCheck.
     */
    public TypeCheck() {
        declarations = new Declarations(null, null, new ArrayList<TypeDecl>(), new ArrayList<VarDecl>(), new ArrayList<FuncDecl>());
    }

    /**
     * Create a copy of a TypeCheck instance.
     *
     * @param old old TypeCheck instance
     */
    private TypeCheck(final TypeCheck old) {
        declarations = new Declarations(null, null, new ArrayList<TypeDecl>(), new ArrayList<VarDecl>(), new ArrayList<FuncDecl>());
        declarations.getTypeDeclList().addAll(old.declarations.getTypeDeclList());
        declarations.getVarDeclList().addAll(old.declarations.getVarDeclList());
        declarations.getProcDeclList().addAll(old.declarations.getProcDeclList());
    }

    /**
     * Check a call statement or expression.
     *
     * @param node PaMaAstNode
     * @param identifier function name
     * @param expressionList list of argument expressions
     * @param isFunction is a return value expected
     * @return the return type of the function or {@code null} if the target is a procedure
     * @throws CompileException on failure
     */
    private Type checkCall(final PaMaAstNode node, final String identifier, final ArrayList<Expression> expressionList, final boolean isFunction) throws CompileException {
        /* is a variable or parameter name */
        final VarDecl varDecl = declarations.getVarDecl(identifier);
        if ((varDecl != null) && (varDecl.getType().getResolvedType() instanceof FunctionType)) {
            final FunctionType functionType = (FunctionType) varDecl.getType().getResolvedType();
            if (expressionList.size() != functionType.getParameterList().size()) {
                throw new CompileException(node, "wrong number of arguments");
            }

            for (int i = 0; i < expressionList.size(); ++i) {
                final Expression expression = expressionList.get(i);
                expression.process(this);
                expression.setReference(functionType.getParameterList().get(i).isReference());
                if (!isSameType(expression, functionType.getParameterList().get(i).getType())) {
                    throw new CompileException(expressionList.get(i), "wrong type");
                }
            }

            if (isFunction) {
                if (functionType.getReturnType() == null) {
                    throw new CompileException(node, "can not call procedure in expression");
                }
            } else {
                if (functionType.getReturnType() != null) {
                    throw new CompileException(node, "can not call function in statement");
                }
            }
            return functionType.getReturnType();
        }

        /* is a real function / procedure */
        final FuncDecl procDecl = declarations.getProcDecl(identifier);
        if (procDecl != null) {
            if (expressionList.size() != procDecl.getFormalParameterList().size()) {
                throw new CompileException(node, "wrong number of arguments");
            }

            for (int i = 0; i < expressionList.size(); ++i) {
                final Expression expression = expressionList.get(i);
                expression.process(this);
                expression.setReference(procDecl.getFormalParameterList().get(i).isReference());
                if (!isSameType(expression, procDecl.getFormalParameterList().get(i).getType())) {
                    throw new CompileException(expressionList.get(i), "wrong type");
                }
            }

            if (isFunction) {
                if (procDecl.getReturnType() == null) {
                    throw new CompileException(node, "can not call procedure in expression");
                }
            } else {
                if (procDecl.getReturnType() != null) {
                    throw new CompileException(node, "can not call function in statement");
                }
            }
            return procDecl.getReturnType();
        }

        throw new CompileException(node, "no such function");
    }

    /**
     * Alias for {@link #isSameType(Type, Type)}.
     *
     * @param expression1 expression 1
     * @param expression2 expression 1
     * @return {@code true} if the expressions evaluate to types that are considered equal
     * @throws CompileException on failure
     */
    private boolean isSameType(final Expression expression1, final Expression expression2) throws CompileException {
        return isSameType(expression1.getType(), expression2.getType());
    }

    /**
     * Alias for {@link #isSameType(Expression, Expression)}.
     *
     * @param expression expression 1
     * @param type type
     * @return {@code true} if the expression evaluates to a type that is considered equal to the given type
     * @throws CompileException on failure
     */
    private boolean isSameType(final Expression expression, final Type type) throws CompileException {
        return isSameType(expression.getType(), type);
    }

    /**
     * Check if two types can be considered equal.
     * 
     * @param type1 type 1
     * @param type2 type 2
     * @return {@code true} if the two types can be considered equal
     * @throws CompileException on failure
     */
    private boolean isSameType(final Type type1, final Type type2) throws CompileException {
        if (type1 == type2) {
            return true;
        } else if (type1 instanceof CustomType) {
            return isSameType(type1.getResolvedType(), type2);
        } else if (type2 instanceof CustomType) {
            return isSameType(type1, type2.getResolvedType());
        } else if ((type1 instanceof ArrayType) && (type2 instanceof ArrayType)) {
            final ArrayType array1 = (ArrayType) type1;
            final ArrayType array2 = (ArrayType) type2;
            if (array1.getDimensions().size() != array2.getDimensions().size()) {
                return false;
            }
            for (int i = 0; i < array1.getDimensions().size(); ++i) {
                final Range range1 = array1.getDimensions().get(i);
                final Range range2 = array2.getDimensions().get(i);
                if ((range1 == null) || (range2 == null)) {
                    continue;
                }
                if ((range1.getLow() != range2.getLow()) || (range1.getHigh() != range2.getHigh())) {
                    return false;
                }
            }
            return isSameType(array1.getBaseType(), array2.getBaseType());
        } else if ((type1 instanceof BooleanType) && (type2 instanceof BooleanType)) {
            return true;
        } else if ((type1 instanceof FunctionType) && (type2 instanceof FunctionType)) {
            final FunctionType function1 = (FunctionType) type1;
            final FunctionType function2 = (FunctionType) type2;
            if (function1.getParameterList().size() != function2.getParameterList().size()) {
                return false;
            }
            for (int i = 0; i < function1.getParameterList().size(); ++i) {
                final FormalParameter parameter1 = function1.getParameterList().get(i);
                final FormalParameter parameter2 = function2.getParameterList().get(i);
                if (parameter1.isReference() != parameter2.isReference()) {
                    return false;
                }
                if (!isSameType(parameter1.getType(), parameter2.getType())) {
                    return false;
                }
            }
            final Type returnType1 = function1.getReturnType();
            final Type returnType2 = function2.getReturnType();
            if ((returnType1 == null) && (returnType2 == null)) {
                return true;
            }
            if ((returnType1 != null) && (returnType2 != null)) {
                return isSameType(returnType1, returnType2);
            }
            return false;
        } else if ((type1 instanceof IntegerType) && (type2 instanceof IntegerType)) {
            return true;
        } else if ((type1 instanceof PointerType) && (type2 instanceof PointerType)) {
            if ((type1 instanceof NilPointerType) || (type2 instanceof NilPointerType)) {
                return true;
            }
            return isSameType(((PointerType) type1).getType(), ((PointerType) type2).getType());
        } else if ((type1 instanceof RecordType) && (type2 instanceof RecordType)) {
            final ArrayList<VarDecl> varDeclList1 = ((RecordType) type1).getVarDeclList();
            final ArrayList<VarDecl> varDeclList2 = ((RecordType) type2).getVarDeclList();
            if (varDeclList1.size() != varDeclList2.size()) {
                return false;
            }

            for (int i = 0; i < varDeclList1.size(); ++i) {
                if (!isSameType(varDeclList1.get(i).getType(), varDeclList2.get(i).getType())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public final void process(final ArrayType node) throws CompileException {
        for (final Range range : node.getDimensions()) {
            if (range == null) {
                continue;
            }

            if (range.getLow() > range.getHigh()) {
                throw new CompileException(range, "lower bound greater than upper bound");
            }
        }

        node.getBaseType().process(this);
    }

    @Override
    public final void process(final AssignmentStatement node) throws CompileException {
        final Designator designator = node.getDesignator();
        final Expression expression = node.getExpression();

        designator.process(this);
        expression.process(this);

        final Type type = designator.getType().getResolvedType();
        if (type instanceof FunctionType) {
            designator.setType(((FunctionType) type).getReturnType());
        }

        if (!isSameType(expression, designator.getType())) {
            throw new CompileException(node, "designator and expression not of the same type: " + designator.getType() + ", " + expression.getType());
        }
    }

    @Override
    public final void process(final BooleanExpression node) throws CompileException {
        return;
    }

    @Override
    public final void process(final BooleanType node) throws CompileException {
        return;
    }

    @Override
    public final void process(final CallExpression node) throws CompileException {
        node.setType(checkCall(node, node.getIdentifier(), node.getExpressionList(), true));
    }

    @Override
    public final void process(final CallStatement node) throws CompileException {
        checkCall(node, node.getIdentifier(), node.getExpressionList(), false);
    }

    @Override
    public final void process(final CaseLimb node) throws CompileException {
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
        }
    }

    @Override
    public final void process(final CaseStatement node) throws CompileException {
        node.getExpression().process(this);
        if (!isSameType(node.getExpression().getType(), new IntegerType(null, null))) {
            throw new CompileException(node, "case expression must be of type integer");
        }
        for (int i = 0; i < node.getCaseLimbList().size(); ++i) {
            final CaseLimb caseLimb = node.getCaseLimbList().get(i);
            if (caseLimb.getValue() != i) {
                throw new CompileException(caseLimb, "case values must start at 0 and be continuous");
            }
            caseLimb.process(this);
        }
    }

    @Override
    public final void process(final ConjunctionExpression node) throws CompileException {
        final Expression lhs = node.getLhs();
        final Expression rhs = node.getRhs();

        lhs.process(this);
        rhs.process(this);

        if (!isSameType(lhs, rhs)) {
            throw new CompileException(node, "Expressions are not of the same type");
        }

        switch (node.getOperator()) {
        case ADD:
        case SUB:
            if (!isSameType(lhs, new IntegerType(null, null))) {
                throw new CompileException(node, "expressions must be of type integer");
            }
            node.setType(node.getLhs().getType());
            return;

        case OR:
            if (!isSameType(lhs, new BooleanType(null, null))) {
                throw new CompileException(node, "expressions must be of type boolean");
            }
            node.setType(node.getLhs().getType());
            return;

        default:
            throw new CompileException(node, "Unknown operator");
        }
    }

    @Override
    public final void process(final CustomType node) throws CompileException {
        final TypeDecl typeDecl = declarations.getTypeDecl(node.getIdentifier());
        if (typeDecl == null) {
            throw new CompileException(node, "Unknown type");
        }

        node.setResolvedType(typeDecl.getType());
    }

    @Override
    public final void process(final Declarations node) throws CompileException {
        /* add to currently known type declarations */
        for (final TypeDecl typeDecl : node.getTypeDeclList()) {
            declarations.addTypeDecl(typeDecl);
        }

        /* resolve type declarations */
        for (final TypeDecl typeDecl : node.getTypeDeclList()) {
            typeDecl.process(this);
        }

        /* add to currently known variables */
        for (final VarDecl varDecl : node.getVarDeclList()) {
            declarations.addVarDecl(varDecl);
        }

        /* resolve variable type declarations */
        for (final VarDecl varDecl : node.getVarDeclList()) {
            varDecl.process(this);
        }

        /* add to currently known procedure declarations */
        for (final FuncDecl procDecl : node.getProcDeclList()) {
            declarations.addProcDecl(procDecl);
        }

        /* resolve procedure type declarations */
        for (final FuncDecl funcDecl : node.getProcDeclList()) {
            funcDecl.process(new TypeCheck(this));
        }
    }

    @Override
    public final void process(final Designator node) throws CompileException {
        final FuncDecl procDecl = declarations.getProcDecl(node.getIdentifier());
        if (procDecl != null) {
            if (procDecl.getReturnType() == null) {
                node.setType(new ProcedureType(procDecl.getBegin(), procDecl.getEnd(), procDecl.getFormalParameterList()));
            } else {
                node.setType(new FunctionType(procDecl.getBegin(), procDecl.getEnd(), procDecl.getFormalParameterList(), procDecl.getReturnType()));
            }
            return;
        }

        final VarDecl varDecl = declarations.getVarDecl(node.getIdentifier());
        if (varDecl == null) {
            throw new CompileException(node, "no such variable");
        }

        Type type = varDecl.getType();
        for (final DesignatorPart designatorPart : node.getDesignatorPartList()) {
            while (type instanceof CustomType) {
                type = type.getResolvedType();
            }

            if (designatorPart instanceof IndirectionDesignatorPart) {
                if (!(type instanceof PointerType)) {
                    throw new CompileException(designatorPart, "not a pointer");
                }
                final PointerType pointer = (PointerType) type;
                ((IndirectionDesignatorPart) designatorPart).setType(type);
                type = pointer.getType();
            } else if (designatorPart instanceof ListAccessDesignatorPart) {
                if (!(type instanceof ArrayType)) {
                    throw new CompileException(designatorPart, "not an array");
                }
                final ArrayType array = (ArrayType) type;
                ((ListAccessDesignatorPart) designatorPart).setType(type);
                type = array.getBaseType();

                if (array.getDimensions().size() != ((ListAccessDesignatorPart) designatorPart).getExpressionList().size()) {
                    throw new CompileException(designatorPart, "wrong number of dimensions");
                }
                for (final Expression expression : ((ListAccessDesignatorPart) designatorPart).getExpressionList()) {
                    expression.process(this);
                }
            } else if (designatorPart instanceof SelectorDesignatorPart) {
                if (!(type instanceof RecordType)) {
                    throw new CompileException(designatorPart, "not a record");
                }
                final VarDecl recordVarDecl = ((RecordType) type).getVarDecl(((SelectorDesignatorPart) designatorPart).getIdentifier());
                if (recordVarDecl == null) {
                    throw new CompileException(designatorPart, "no such element in record");
                }
                ((SelectorDesignatorPart) designatorPart).setType(type);
                type = recordVarDecl.getType();
            } else {
                throw new CompileException(designatorPart, "unknown designator");
            }
        }
        node.setType(type);
    }

    @Override
    public final void process(final DisjunctionExpression node) throws CompileException {
        final Expression lhs = node.getLhs();
        final Expression rhs = node.getRhs();

        lhs.process(this);
        rhs.process(this);

        if (!isSameType(lhs, rhs)) {
            throw new CompileException(node, "Expressions are not of the same type");
        }

        switch (node.getOperator()) {
        case MUL:
        case DIV:
            if (!isSameType(lhs, new IntegerType(null, null))) {
                throw new CompileException(node, "expressions must be of type integer");
            }
            node.setType(node.getLhs().getType());
            return;

        case AND:
            if (!isSameType(lhs, new BooleanType(null, null))) {
                throw new CompileException(node, "expressions must be of type boolean");
            }
            node.setType(node.getLhs().getType());
            return;

        default:
            throw new CompileException(node, "Unknown operator");
        }
    }

    @Override
    public final void process(final FormalParameter node) throws CompileException {
        node.getType().process(this);
        if (node.isReference()) {
            declarations.addVarDecl(new VarDecl(node.getBegin(), node.getEnd(), node.getIdentifier(), node.getType(), VarDecl.Source.REFERENCE_PARAMETER));
        } else {
            declarations.addVarDecl(new VarDecl(node.getBegin(), node.getEnd(), node.getIdentifier(), node.getType(), VarDecl.Source.VALUE_PARAMETER));
        }
    }

    @Override
    public final void process(final ForStatement node) throws CompileException {
        node.getDesignator().process(this);
        if (!isSameType(node.getDesignator().getType(), new IntegerType(null, null))) {
            throw new CompileException(node.getDesignator(), "'for' designator must be of type integer");
        }

        node.getFromExpression().process(this);
        if (!isSameType(node.getFromExpression(), new IntegerType(null, null))) {
            throw new CompileException(node.getFromExpression(), "'for' range must be of type integer");
        }

        node.getToExpression().process(this);
        if (!isSameType(node.getToExpression(), new IntegerType(null, null))) {
            throw new CompileException(node.getFromExpression(), "'for' range must be of type integer");
        }

        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
        }
    }

    @Override
    public final void process(final FuncDecl node) throws CompileException {
        if (node.getReturnType() != null) {
            node.getReturnType().process(this);
        }

        for (final FormalParameter formalParameter : node.getFormalParameterList()) {
            formalParameter.process(this);
        }

        final TypeCheck typeCheck = new TypeCheck(this);
        node.getDeclarations().process(typeCheck);
        for (final Statement statement : node.getStatementList()) {
            statement.process(typeCheck);
        }
    }

    @Override
    public final void process(final FunctionType node) throws CompileException {
        for (final FormalParameter parameter : node.getParameterList()) {
            parameter.getType().process(this);
        }

        if (node.getReturnType() != null) {
            node.getReturnType().process(this);
        }
    }

    @Override
    public final void process(final HighExpression node) throws CompileException {
        final Designator designator = node.getDesignator();
        designator.process(this);
        final Type type = designator.getType().getResolvedType();

        if (!(type instanceof ArrayType)) {
            throw new CompileException(node, "not an array");
        }

        final int d = node.getDimension() - 1;
        if ((d < 0) || (d >= ((ArrayType) type).getDimensions().size())) {
            throw new CompileException(node, "no such dimension");
        }
        node.setType(new IntegerType(node.getBegin(), node.getEnd()));
    }

    @Override
    public final void process(final IfStatement node) throws CompileException {
        node.getCondition().process(this);
        if (!isSameType(node.getCondition(), new BooleanType(null, null))) {
            throw new CompileException(node, "condition must be of type boolean");
        }

        for (final Statement statement : node.getThenStatementList()) {
            statement.process(this);
        }

        for (final Statement statement : node.getElseStatementList()) {
            statement.process(this);
        }
    }

    @Override
    public final void process(final IndirectionDesignatorPart node) throws CompileException {
        /* handled in process(DesignatorPart) */
        return;
    }

    @Override
    public final void process(final IntegerExpression node) throws CompileException {
        return;
    }

    @Override
    public final void process(final IntegerType node) throws CompileException {
        return;
    }

    @Override
    public final void process(final ListAccessDesignatorPart node) throws CompileException {
        /* handled in process(DesignatorPart) */
        return;
    }

    @Override
    public final void process(final LowExpression node) throws CompileException {
        final Designator designator = node.getDesignator();
        designator.process(this);
        final Type type = designator.getType().getResolvedType();

        if (!(type instanceof ArrayType)) {
            throw new CompileException(node, "not an array");
        }

        final int d = node.getDimension() - 1;
        if ((d < 0) || (d >= ((ArrayType) type).getDimensions().size())) {
            throw new CompileException(node, "no such dimension");
        }
        node.setType(new IntegerType(node.getBegin(), node.getEnd()));
    }

    @Override
    public final void process(final NewStatement node) throws CompileException {
        node.getDesignator().process(this);
    }

    @Override
    public final void process(final NilExpression node) throws CompileException {
        return;
    }

    @Override
    public final void process(final PointerType node) throws CompileException {
        node.getType().process(this);
    }

    @Override
    public final void process(final Program node) throws CompileException {
        node.getDeclarations().process(this);
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
        }
    }

    @Override
    public final void process(final ReadlnStatement node) throws CompileException {
        node.getDesignator().process(this);
        final Type type = node.getDesignator().getType().getResolvedType();
        if (!(type instanceof BooleanType) && !(type instanceof IntegerType) && !(type instanceof PointerType)) {
            throw new CompileException(node, "can not readln this type");
        }
    }

    @Override
    public final void process(final RecordType node) throws CompileException {
        for (final VarDecl varDecl : node.getVarDeclList()) {
            varDecl.process(this);
        }
    }

    @Override
    public final void process(final RelationalExpression node) throws CompileException {
        final Expression lhs = node.getLhs();
        final Expression rhs = node.getRhs();

        lhs.process(this);
        rhs.process(this);

        if (!isSameType(lhs, rhs)) {
            throw new CompileException(node, "Expressions are not of the same type");
        }

        switch (node.getOperator()) {
        case EQL:
        case NEQ:
            if (!isSameType(lhs, new IntegerType(null, null)) && !isSameType(lhs, new NilPointerType(null, null)) && !isSameType(lhs, new BooleanType(null, null))) {
                throw new CompileException(node, "expressions must be of type integer, boolean, or pointer");
            }
            break;

        case GT:
        case GTE:
        case LT:
        case LTE:
            if (!isSameType(lhs, new IntegerType(null, null))) {
                throw new CompileException(node, "expressions must be of type integer");
            }
            break;

        default:
            throw new CompileException(node, "unknown operator");
        }

        node.setType(new BooleanType(node.getBegin(), node.getEnd()));
    }

    @Override
    public final void process(final RepeatStatement node) throws CompileException {
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
        }
        node.getCondition().process(this);
        if (!isSameType(node.getCondition(), new BooleanType(null, null))) {
            throw new CompileException(node.getCondition(), "condition must be of type boolean");
        }
    }

    @Override
    public final void process(final SelectorDesignatorPart node) throws CompileException {
        /* handled in process(DesignatorPart) */
        return;
    }

    @Override
    public final void process(final TypeDecl node) throws CompileException {
        node.getType().process(this);
    }

    @Override
    public final void process(final UnaryExpression node) throws CompileException {
        final Expression expression = node.getExpression();
        expression.process(this);

        switch (node.getOperator()) {
        case NEG:
            if (!isSameType(expression, new IntegerType(null, null))) {
                throw new CompileException(node, "expression must be of type integer");
            }
            node.setType(expression.getType());
            return;

        case NOT:
            if (!isSameType(expression, new BooleanType(null, null))) {
                throw new CompileException(node, "expression must be of type boolean");
            }
            node.setType(expression.getType());
            return;

        default:
            throw new CompileException(node, "unknown unary operator");
        }
    }

    @Override
    public final void process(final VarDecl node) throws CompileException {
        node.getType().process(this);
    }

    @Override
    public final void process(final WhileStatement node) throws CompileException {
        node.getCondition().process(this);
        if (!isSameType(node.getCondition(), new BooleanType(null, null))) {
            throw new CompileException(node.getCondition(), "condition must be of type boolean");
        }
        for (final Statement statement : node.getStatementList()) {
            statement.process(this);
        }
    }

    @Override
    public final void process(final WritelnStatement node) throws CompileException {
        node.getExpression().process(this);
        final Type type = node.getExpression().getType().getResolvedType();
        if (!(type instanceof BooleanType) && !(type instanceof IntegerType) && !(type instanceof PointerType)) {
            throw new CompileException(node, "can not writeln this type");
        }
    }
}
