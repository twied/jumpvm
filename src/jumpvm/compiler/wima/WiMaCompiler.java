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

package jumpvm.compiler.wima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import jumpvm.ast.AstNode;
import jumpvm.ast.wima.Atom;
import jumpvm.ast.wima.Clause;
import jumpvm.ast.wima.Numeral;
import jumpvm.ast.wima.Predicate;
import jumpvm.ast.wima.Program;
import jumpvm.ast.wima.Query;
import jumpvm.ast.wima.Structure;
import jumpvm.ast.wima.Term;
import jumpvm.ast.wima.Variable;
import jumpvm.ast.wima.WiMaAstNode;
import jumpvm.code.wima.BrotherInstruction;
import jumpvm.code.wima.CallInstruction;
import jumpvm.code.wima.DelBtpInstruction;
import jumpvm.code.wima.DownInstruction;
import jumpvm.code.wima.EnterInstruction;
import jumpvm.code.wima.HaltInstruction;
import jumpvm.code.wima.InitInstruction;
import jumpvm.code.wima.NextAltInstruction;
import jumpvm.code.wima.PopEnvInstruction;
import jumpvm.code.wima.PushArgInstruction;
import jumpvm.code.wima.PushEnvInstruction;
import jumpvm.code.wima.PutAtomInstruction;
import jumpvm.code.wima.PutRefInstruction;
import jumpvm.code.wima.PutStructInstruction;
import jumpvm.code.wima.PutVarInstruction;
import jumpvm.code.wima.RestoreInstruction;
import jumpvm.code.wima.SetBtpInstruction;
import jumpvm.code.wima.UAtomInstruction;
import jumpvm.code.wima.URefInstruction;
import jumpvm.code.wima.UStructInstruction;
import jumpvm.code.wima.UVarInstruction;
import jumpvm.code.wima.UpInstruction;
import jumpvm.compiler.Compiler;
import jumpvm.exception.CompileException;
import jumpvm.memory.Label;
import jumpvm.vm.WiMa;

/**
 * WiMachine {@link Compiler}.
 */
public class WiMaCompiler extends Compiler {
    /**
     * Parameter for {@link WiMaCompiler#btInit(BtParam, Label, WiMaAstNode)} and {@link WiMaCompiler#btFin(BtParam, WiMaAstNode)}.
     */
    static enum BtParam {
        /** First alternative. */
        FIRST,

        /** Middle alternative. */
        MIDDLE,

        /** Last alternative. */
        LAST,

        /** Only alternative. */
        SINGLE
    }

    /**
     * WiMachine Code walker.
     */
    public abstract class Code implements WiMaAstWalker {
        /** Variable context. */
        private final HashMap<String, Integer> rho;

        /**
         * Create a new code walker.
         * 
         * @param rho variable context
         */
        public Code(final HashMap<String, Integer> rho) {
            this.rho = rho;
        }

        /**
         * Returns the variable context.
         * 
         * @return the variable context
         */
        public final HashMap<String, Integer> getRho() {
            return rho;
        }
    }

    /**
     * Translates a term into a stream of instructions that will create an instance of {@code t} on the heap.
     */
    class CodeA extends Code {
        /**
         * Create a new CodeA.
         * 
         * @param rho variable context
         */
        public CodeA(final HashMap<String, Integer> rho) {
            super(rho);
        }

        @Override
        public void process(final Atom node) {
            emit(new PutAtomInstruction(node, node.getIdentifier()));
        }

        @Override
        public void process(final Clause node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Numeral node) {
            emit(new PutAtomInstruction(node, node.getIdentifier()));
        }

        @Override
        public void process(final Program node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Query node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Structure node) throws CompileException {
            for (final Term term : node.getTermList()) {
                term.process(new CodeA(getRho()));
            }
            emit(new PutStructInstruction(node, node.getAtom().getIdentifier(), node.getTermList().size()));
        }

        @Override
        public void process(final Variable node) {
            final String identifier = node.getIdentifier();
            final int position = getRho().get(identifier);

            if (knownVariables.contains(identifier)) {
                emit(new PutRefInstruction(node, position, identifier));
            } else {
                emit(new PutVarInstruction(node, position, identifier));
                knownVariables.add(identifier);
            }
        }
    }

    /**
     * Translates a predicate. Create a new frame on the stack ({@code enter}), create arguments ({@code CodeA}), then execute this predicate ( {@code call}).
     */
    class CodeG extends Code {
        /**
         * Create a new CodeG.
         * 
         * @param rho variable context
         */
        public CodeG(final HashMap<String, Integer> rho) {
            super(rho);
        }

        @Override
        public void process(final Atom node) throws CompileException {
            emit(new EnterInstruction(node));
            emit(new CallInstruction(node, getLabel(node.getIdentifier(), 0), 0));
        }

        @Override
        public void process(final Clause node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Numeral node) throws CompileException {
            return;
        }

        @Override
        public void process(final Program node) throws CompileException {
            return;
        }

        @Override
        public void process(final Query node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Structure node) throws CompileException {
            emit(new EnterInstruction(node));

            for (final Term term : node.getTermList()) {
                term.process(new CodeA(getRho()));
            }

            emit(new CallInstruction(node, getLabel(node.getIdentifier(), node.getTermList().size()), node.getTermList().size()));
        }

        @Override
        public void process(final Variable node) throws CompileException {
            return;
        }

    }

    /**
     * Create a stream of instructions that unifies variables with structures and atoms.
     */
    class CodeU extends Code {
        /**
         * Create a new CodeU.
         * 
         * @param rho variable context
         */
        public CodeU(final HashMap<String, Integer> rho) {
            super(rho);
        }

        @Override
        public void process(final Atom node) throws CompileException {
            emit(new UAtomInstruction(node, node.getIdentifier()));
        }

        @Override
        public void process(final Clause node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Numeral node) throws CompileException {
            emit(new UAtomInstruction(node, node.getIdentifier()));
        }

        @Override
        public void process(final Program node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Query node) throws CompileException {
            throw new CompileException(node);
        }

        @Override
        public void process(final Structure node) throws CompileException {
            final String name = node.getIdentifier() + "/" + node.getArity();
            emit(new UStructInstruction(node, node.getAtom().getIdentifier(), node.getTermList().size()));
            emit(new DownInstruction(node, name));

            node.getTermList().get(0).process(this);
            for (int i = 1; i < node.getTermList().size(); ++i) {
                emit(new BrotherInstruction(node, i + 1, name));
                node.getTermList().get(i).process(this);
            }

            emit(new UpInstruction(node));
        }

        @Override
        public void process(final Variable node) throws CompileException {
            final String identifier = node.getIdentifier();
            final int position = getRho().get(identifier);

            if (knownVariables.contains(identifier)) {
                emit(new URefInstruction(node, position, identifier));
            } else {
                emit(new UVarInstruction(node, position, identifier));
                knownVariables.add(identifier);
            }
        }
    }

    /**
     * Maps clauses to their unification code in the PROGRAM memory.
     */
    private final HashMap<String, Label> clauseMap;

    /**
     * Set of variables which already have space on the heap allocated for them.
     */
    private final HashSet<String> knownVariables;

    /**
     * Create a new WiMaCompiler.
     */
    public WiMaCompiler() {
        this.clauseMap = new HashMap<String, Label>();
        this.knownVariables = new HashSet<String>();
    }

    /**
     * Finish backtracking.
     * 
     * @param btParam back tracking mode
     * @param node current node
     */
    private void btFin(final BtParam btParam, final WiMaAstNode node) {
        switch (btParam) {
        case SINGLE: /* fallthrough */
        case LAST:
            emit(new PopEnvInstruction(node));
            break;

        default:
            emit(new RestoreInstruction(node));
        }
    }

    /**
     * Prepare backtracking.
     * 
     * @param btParam back tracking mode
     * @param btReturn return address
     * @param node current node
     */
    private void btInit(final BtParam btParam, final Label btReturn, final Clause node) {
        switch (btParam) {
        case FIRST:
            emit(new SetBtpInstruction(node, btReturn));
            break;

        case LAST:
            emit(new DelBtpInstruction(node));
            break;

        case MIDDLE:
            emit(new NextAltInstruction(node, btReturn));
            break;

        default:
            break;
        }
    }

    /**
     * Translate a clause that may or may not be part of a set of clauses with same name and arity.
     * 
     * @param clause clause to translate
     * @param btParam back tracking mode
     * @param btReturn return address
     * @throws CompileException on failure
     */
    private void codeC(final Clause clause, final BtParam btParam, final Label btReturn) throws CompileException {
        final Predicate head = clause.getHead();
        final ArrayList<String> variables = new Vars(clause);
        final HashMap<String, Integer> rho = createRho(variables, clause.getHead().getArity());
        knownVariables.clear();

        if (head instanceof Atom) {
            emit(new PushEnvInstruction(clause, (0 + variables.size() + WiMa.FRAME_SIZE) - 1));
            btInit(btParam, btReturn, clause);
        } else {
            final Structure structure = (Structure) head;
            emit(new PushEnvInstruction(clause, (structure.getTermList().size() + variables.size() + WiMa.FRAME_SIZE) - 1));
            btInit(btParam, btReturn, clause);
            for (int i = 0; i < structure.getTermList().size(); ++i) {
                emit(new PushArgInstruction(clause, i + 1, structure.getTermList().get(i).toString()));
                structure.getTermList().get(i).process(new CodeU(rho));
            }
        }

        for (final Predicate predicate : clause.getBody()) {
            predicate.process(new CodeG(rho));
        }

        btFin(btParam, clause);
    }

    /**
     * Translate the given program.
     * 
     * @param clauseList clauses in this program
     * @throws CompileException on failure
     */
    private void codeP(final ArrayList<Clause> clauseList) throws CompileException {
        final HashSet<Clause> done = new HashSet<Clause>();

        for (final Clause clause : clauseList) {
            if (done.contains(clause)) {
                continue;
            }

            final String identifier = clause.getHead().getIdentifier();
            final ArrayList<Clause> currentClauseList = new ArrayList<Clause>();
            for (final Clause curretClause : clauseList) {
                if (curretClause.getHead().getIdentifier().equals(identifier)) {
                    currentClauseList.add(curretClause);
                }
            }

            getLabel(clause.getHead().getIdentifier(), clause.getHead().getArity()).setAddress(getCurrentPosition());

            if (currentClauseList.size() == 1) {
                codePR(clause);
            } else {
                codePR(currentClauseList);
            }

            for (final Clause currentClause : currentClauseList) {
                done.add(currentClause);
            }
        }
    }

    /**
     * Translate a list of clauses with same identifier and arity in the program.
     * 
     * @param clauseList list of clauses
     * @throws CompileException on failure
     */
    private void codePR(final ArrayList<Clause> clauseList) throws CompileException {
        final String clauseName = clauseList.get(0).getHead().getIdentifier() + "/" + clauseList.get(0).getHead().getArity();
        Label label = new Label(clauseName + " (" + 2 + ")");

        /* first clause */
        codeC(clauseList.get(0), BtParam.FIRST, label);

        /* middle clauses */
        for (int i = 1; i < (clauseList.size() - 1); ++i) {
            label.setAddress(getCurrentPosition());
            label = new Label(clauseName + " (" + (i + 2) + ")");
            codeC(clauseList.get(i), BtParam.MIDDLE, label);
        }

        /* last clauses */
        label.setAddress(getCurrentPosition());
        codeC(clauseList.get(clauseList.size() - 1), BtParam.LAST, null);
    }

    /**
     * Translate a unique clause in the program.
     * 
     * @param clause clause
     * @throws CompileException on failure
     */
    private void codePR(final Clause clause) throws CompileException {
        codeC(clause, BtParam.SINGLE, null);
    }

    /**
     * Translate the given query.
     * 
     * @param q query to translate
     * @throws CompileException on failure
     */
    private void codeQ(final Query q) throws CompileException {
        final ArrayList<String> variables = new Vars(q);

        emit(new InitInstruction(q));
        emit(new PushEnvInstruction(q, (variables.size() + WiMa.FRAME_SIZE) - 1));
        for (final Predicate predicate : q.getPredicateList()) {
            predicate.process(new CodeG(createRho(variables, 0)));
        }
        emit(new HaltInstruction(q));
    }

    /**
     * Create a variable context given a list of variables and an offset for the stack frame.
     * 
     * @param variables list of variables
     * @param n offset
     * @return variable context for the current stack frame
     */
    private HashMap<String, Integer> createRho(final ArrayList<String> variables, final int n) {
        final HashMap<String, Integer> rho = new HashMap<String, Integer>();
        for (int i = 0; i < variables.size(); ++i) {
            rho.put(variables.get(i), n + i + WiMa.FRAME_SIZE);
        }
        return rho;
    }

    /**
     * Returns the address of the unification code for the given clause.
     * 
     * @param identifier name of the clause
     * @param arity arity of the clause
     * @return the address of the unification code for the given clause
     */
    private Label getLabel(final String identifier, final int arity) {
        final String key = identifier + "/" + arity;
        final Label label;

        if (clauseMap.containsKey(key)) {
            label = clauseMap.get(key);
        } else {
            label = new Label(key);
            clauseMap.put(key, label);
        }

        return label;
    }

    @Override
    public final void processProgram(final AstNode<?> programNode) throws CompileException {
        try {
            final Program program = (Program) programNode;
            codeQ(program.getQuery());
            codeP(program.getClauseList());
        } catch (final RuntimeException e) {
            throw new CompileException(programNode, e);
        }

        for (final Entry<String, Label> entry : clauseMap.entrySet()) {
            if (entry.getValue().getAddress() == -1) {
                throw new CompileException(programNode, "unknown identifier or wrong arity: " + entry.getKey());
            }
        }
    }
}
