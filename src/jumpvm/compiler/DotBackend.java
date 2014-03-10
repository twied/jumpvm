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

package jumpvm.compiler;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import jumpvm.ast.AstNode;
import jumpvm.exception.CompileException;

/**
 * Treewalker that turns an abstract syntax tree into a dot file.
 * 
 * @see {@code dot -Tpng -o output.png input.dot}
 * @see <a href="http://www.graphviz.org/">graphviz</a>.
 */
public abstract class DotBackend {
    /** Root node. */
    public static final String ROOT = "root";

    /** Map Object -> ID. */
    private final IdentityHashMap<Object, String> idMap;

    /** Resulting dot file content. */
    private final ArrayList<String> content;

    /** Creates the graph from left to right instead of top to bottom if set to {@code true}. */
    private final boolean rankDirLR;

    /** Next free unique id. */
    private int nextId;

    /**
     * Create a new DotBackend.
     * 
     * @param rankDirLR if the graph shall be created from left to right instead of top to bottom
     */
    public DotBackend(final boolean rankDirLR) {
        this.idMap = new IdentityHashMap<Object, String>();
        this.content = new ArrayList<String>();
        this.rankDirLR = rankDirLR;
        idMap.put(ROOT, ROOT);
    }

    /**
     * Begin the creation of the graph.
     */
    protected final void begin() {
        content.add("digraph G {");
        if (rankDirLR) {
            content.add("rankdir = LR;");
        }
        emitNode(ROOT, "Program");
    }

    /**
     * Emit an directed edge from one object to another.
     * 
     * @param fromObject starting object
     * @param toObject target object
     */
    protected final void emitEdge(final Object fromObject, final Object toObject) {
        content.add(getId(fromObject) + " -> " + getId(toObject) + ";");
    }

    /**
     * Emit an directed edge with a label from one object to another.
     * 
     * @param fromObject starting object
     * @param toObject target object
     * @param label label
     */
    protected final void emitEdge(final Object fromObject, final Object toObject, final String label) {
        content.add(getId(fromObject) + " -> " + getId(toObject) + " [label=\"" + label + "\"];");
    }

    /**
     * Emit a node for a given object.
     * 
     * @param object object
     * @param name label
     */
    protected final void emitNode(final Object object, final String name) {
        content.add(getId(object) + " [label=\"" + name + "\"];");
    }

    /**
     * Finish the creation of the graph.
     */
    protected final void end() {
        content.add("}");
    }

    /**
     * Returns the content of the dot file.
     * 
     * @return the content of the dot file
     */
    public final ArrayList<String> getContent() {
        return content;
    }

    /**
     * Returns the unique id of the object.
     * 
     * @param object object
     * @return the unique id of the object.
     */
    protected final String getId(final Object object) {
        if (idMap.containsKey(object)) {
            return idMap.get(object);
        } else {
            final String id = "id_" + nextId++;
            idMap.put(object, id);
            return id;
        }
    }

    /**
     * Start processing an abstract syntax tree.
     * 
     * @param program root node of the abstract syntax tree
     * @throws CompileException on failure
     */
    public abstract void processProgram(AstNode<?> program) throws CompileException;
}
