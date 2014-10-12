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

package jumpvm.ast.pama;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.ast.pama.declarations.FuncDecl;
import jumpvm.ast.pama.declarations.TypeDecl;
import jumpvm.ast.pama.declarations.VarDecl;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Declaration part of a PaMa program or procedure / function. */
public class Declarations extends PaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** List of type declarations. */
    private final ArrayList<TypeDecl> typeDeclList;

    /** List of variable declarations. */
    private final ArrayList<VarDecl> varDeclList;

    /** List of procedure / function declarations. */
    private final ArrayList<FuncDecl> procDeclList;

    /**
     * Create a new Declarations object.
     *
     * @param begin begin
     * @param end end
     * @param typeDeclList list of type declarations
     * @param varDeclList list of variable declarations
     * @param procDeclList list of procedure declarations
     */
    public Declarations(final Location begin, final Location end, final ArrayList<TypeDecl> typeDeclList, final ArrayList<VarDecl> varDeclList, final ArrayList<FuncDecl> procDeclList) {
        super(begin, end);
        this.typeDeclList = typeDeclList;
        this.varDeclList = varDeclList;
        this.procDeclList = procDeclList;

        final DefaultMutableTreeNode types = new DefaultMutableTreeNode("Types");
        final DefaultMutableTreeNode variables = new DefaultMutableTreeNode("Variables");
        final DefaultMutableTreeNode procedures = new DefaultMutableTreeNode("Procedures");

        if (!typeDeclList.isEmpty()) {
            add(types);
        }
        if (!varDeclList.isEmpty()) {
            add(variables);
        }
        if (!procDeclList.isEmpty()) {
            add(procedures);
        }

        for (final TypeDecl typeDecl : typeDeclList) {
            types.add(typeDecl);
        }
        for (final VarDecl varDecl : varDeclList) {
            variables.add(varDecl);
        }
        for (final FuncDecl funcDecl : procDeclList) {
            procedures.add(funcDecl);
        }
    }

    /**
     * Add a procedure to the declarations.
     *
     * @param newProcDecl new procedure declaration
     */
    public final void addProcDecl(final FuncDecl newProcDecl) {
        final ArrayList<FuncDecl> remove = new ArrayList<FuncDecl>();
        for (final FuncDecl procDecl : procDeclList) {
            if (procDecl.getIdentifier().equals(newProcDecl.getIdentifier())) {
                remove.add(procDecl);
            }
        }

        procDeclList.removeAll(remove);
        procDeclList.add(newProcDecl);
    }

    /**
     * Add a type to the declarations.
     *
     * @param newTypeDecl new type declaration
     */
    public final void addTypeDecl(final TypeDecl newTypeDecl) {
        final ArrayList<TypeDecl> remove = new ArrayList<TypeDecl>();
        for (final TypeDecl typeDecl : typeDeclList) {
            if (typeDecl.getIdentifier().equals(newTypeDecl.getIdentifier())) {
                remove.add(typeDecl);
            }
        }

        typeDeclList.removeAll(remove);
        typeDeclList.add(newTypeDecl);
    }

    /**
     * Add a variable to the declarations.
     *
     * @param newVarDecl new variable declaration
     */
    public final void addVarDecl(final VarDecl newVarDecl) {
        final ArrayList<VarDecl> remove = new ArrayList<VarDecl>();
        for (final VarDecl varDecl : varDeclList) {
            if (varDecl.getIdentifier().equals(newVarDecl.getIdentifier())) {
                remove.add(varDecl);
            }
        }

        varDeclList.removeAll(remove);
        varDeclList.add(newVarDecl);
    }

    /**
     * Returns the procedure declaration with the given name.
     *
     * @param identifier name of the declaration
     * @return the declaration or {@code null} if no such declaration exists
     */
    public final FuncDecl getProcDecl(final String identifier) {
        for (final FuncDecl procDecl : procDeclList) {
            if (procDecl.getIdentifier().equals(identifier)) {
                return procDecl;
            }
        }
        return null;
    }

    /**
     * Returns the list of procedure / function declarations.
     *
     * @return the list of procedure / function declarations
     */
    public final ArrayList<FuncDecl> getProcDeclList() {
        return procDeclList;
    }

    /**
     * Returns the type declaration with the given name.
     *
     * @param identifier name of the declaration
     * @return the declaration or {@code null} if no such declaration exists
     */
    public final TypeDecl getTypeDecl(final String identifier) {
        for (final TypeDecl typeDecl : typeDeclList) {
            if (typeDecl.getIdentifier().equals(identifier)) {
                return typeDecl;
            }
        }
        return null;
    }

    /**
     * Returns the list of type declarations.
     *
     * @return the list of type declarations
     */
    public final ArrayList<TypeDecl> getTypeDeclList() {
        return typeDeclList;
    }

    /**
     * Returns the variable declaration with the given name.
     *
     * @param identifier name of the declaration
     * @return the declaration or {@code null} if no such declaration exists
     */
    public final VarDecl getVarDecl(final String identifier) {
        for (final VarDecl varDecl : varDeclList) {
            if (varDecl.getIdentifier().equals(identifier)) {
                return varDecl;
            }
        }
        return null;
    }

    /**
     * Returns the list of variable declarations.
     *
     * @return the list of variable declarations
     */
    public final ArrayList<VarDecl> getVarDeclList() {
        return varDeclList;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Declarations";
    }
}
