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

package jumpvm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeNode;

import jumpvm.ast.AstNode;
import jumpvm.code.Instruction;
import jumpvm.compiler.Compiler;
import jumpvm.compiler.DotBackend;
import jumpvm.compiler.LocatedReader;
import jumpvm.compiler.Parser;
import jumpvm.exception.CompileException;
import jumpvm.exception.ExecutionException;
import jumpvm.exception.ParseException;
import jumpvm.memory.Memory;
import jumpvm.memory.Program;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.vm.JumpVM;
import jumpvm.vm.VmState;

/**
 * JumpVM IDE.
 */
public abstract class JumpTab extends JPanel {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Highlighting colors. */
    private static final Color[] HIGHLIGHT_COLORS = new Color[] {new Color(0xfce94f), new Color(0x8ae234), new Color(0xfcaf3e), new Color(0x729fcf), new Color(0xad7fa8), new Color(0xe9b96e), new Color(0xd3d7cf), new Color(0xef2929)};

    /** This tab's vm. */
    private final JumpVM vm;

    /** Backlog of execution steps. */
    private final ArrayDeque<VmState> stateStack;

    /** Associated source code file. */
    private File associatedFile;

    /** Precreated dot representation of current program. */
    private ArrayList<String> dotRepresentation;

    /* GUI elements. */

    /** GUI element: Register panel. */
    private final JumpRegisterPanel registerPanel;

    /** GUI element: Memory panels. */
    private final ArrayList<JumpMemoryPanel> memoryPanelList;

    /** GUI element: Source panel. */
    private final JumpSourcePanel sourcePanel;

    /** GUI element: Tree panel. */
    private final JumpTreePanel treePanel;

    /** GUI element: Output panel. */
    private final JumpOutputPanel outputPanel;

    /* Highlighting. */

    /** Map of all highlighted {@link MemoryObject}s. */
    private final HashMap<MemoryObject, Color> colorMemoryObjects;

    /** Map of all highlighted characters in the source code. */
    private final HashMap<Integer, Color> colorSourceCode;

    /** Map of all highlighted AST nodes. */
    private final HashMap<TreeNode, Color> colorTreeNode;

    /**
     * Create a new JumpTab.
     * 
     * @param vm JumpVM for this tab
     */
    public JumpTab(final JumpVM vm) {
        this.vm = vm;
        this.stateStack = new ArrayDeque<VmState>();
        this.dotRepresentation = new ArrayList<String>();

        this.registerPanel = new JumpRegisterPanel(this);
        this.memoryPanelList = new ArrayList<JumpMemoryPanel>();
        this.sourcePanel = new JumpSourcePanel(this);
        this.treePanel = new JumpTreePanel(this);
        this.outputPanel = new JumpOutputPanel();

        this.colorMemoryObjects = new HashMap<MemoryObject, Color>();
        this.colorSourceCode = new HashMap<Integer, Color>();
        this.colorTreeNode = new HashMap<TreeNode, Color>();

        final JPanel memoriesPanel = new JPanel(new GridLayout(1, 0));
        for (final Memory<?> memory : vm.getDisplayMemories()) {
            final JumpMemoryPanel panel = new JumpMemoryPanel(memory, this);
            memoryPanelList.add(panel);
            memoriesPanel.add(panel);
        }

        final JPanel vmPanel = new JPanel(new BorderLayout());
        vmPanel.add(registerPanel, BorderLayout.WEST);
        vmPanel.add(memoriesPanel, BorderLayout.CENTER);

        final JPanel inputPanel = new JPanel(new GridLayout(1, 0));
        inputPanel.add(sourcePanel);
        inputPanel.add(treePanel);

        vm.setWriter(outputPanel);

        setLayout(new BorderLayout());
        add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, vmPanel, inputPanel), BorderLayout.CENTER);
        add(outputPanel.getPanel(), BorderLayout.SOUTH);
    }

    /**
     * Action "compile".
     */
    public final void actionCompile() {
        try {
            /* Create an AST representation of the current source code. */
            final AstNode<?> program = createParser().parse();

            /* Compile AST into mnemonics. */
            final Compiler compiler = createCompiler();
            compiler.processProgram(program);

            /* Prepare dot representation. */
            final DotBackend dotBackend = createDotBackend();
            dotBackend.processProgram(program);

            /* Reset VM. */
            stateStack.clear();
            vm.reset(compiler.getInstructions());
            treePanel.setTree(program);

            dotRepresentation = dotBackend.getContent();

            setHighlight();
        } catch (final ParseException e) {
            JumpGui.showExceptionDialog(this, e.getCause() == null ? e : e.getCause(), "At " + e.getLocation() + ":\n" + e.getLocalizedMessage(), "JumpVM compile failure");
        } catch (final CompileException e) {
            JumpGui.showExceptionDialog(this, e.getCause() == null ? e : e.getCause(), "At " + e.getNode().getBegin() + ":\n" + e.getLocalizedMessage(), "JumpVM compile failure");
        }
        update();
    }

    /**
     * Action "export as asm".
     * 
     * @param fileChooser file chooser
     */
    public final void actionExportAsm(final JFileChooser fileChooser) {
        final Program program = vm.getProgram();

        if (program.getSize() == 0) {
            actionCompile();
        }

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));

            for (int i = 0; i < program.getSize(); ++i) {
                final Instruction instruction = program.getElementAt(i);
                writer.write(instruction.getMnemonic());

                if (instruction.getParameter() != null) {
                    writer.write("\t");
                    writer.write(instruction.getParameter());
                }

                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (final IOException e) {
            JumpGui.showExceptionDialog(this, e, "File could not be written.", "JumpVM error");
        }
    }

    /**
     * Action "export as dot".
     * 
     * @param fileChooser file chooser
     */
    public final void actionExportDot(final JFileChooser fileChooser) {
        if (dotRepresentation.isEmpty()) {
            actionCompile();
        }

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));

            for (int i = 0; i < dotRepresentation.size(); ++i) {
                writer.write(dotRepresentation.get(i));
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (final IOException e) {
            JumpGui.showExceptionDialog(this, e, "File could not be written.", "JumpVM error");
        }
    }

    /**
     * Action "reset".
     */
    public final void actionReset() {
        vm.reset();
        update();
    }

    /**
     * Action "save".
     */
    public final void actionSave() {
        try {
            final Reader reader = sourcePanel.getSource();
            final BufferedWriter writer = new BufferedWriter(new FileWriter(associatedFile));

            while (true) {
                final int i = reader.read();
                if (i < 0) {
                    break;
                }
                writer.write(i);
            }

            reader.close();
            writer.close();
        } catch (final IOException e) {
            JumpGui.showExceptionDialog(this, e, "File could not be written.", "JumpVM error");
        }
    }

    /**
     * Action "save as".
     * 
     * @param fileChooser file chooser
     */
    public final void actionSaveAs(final JFileChooser fileChooser) {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        associatedFile = fileChooser.getSelectedFile();
        actionSave();
    }

    /**
     * Action "step backward".
     */
    public final void actionStepBackward() {
        if (stateStack.isEmpty()) {
            return;
        }

        stateStack.pop().set();
        update();
    }

    /**
     * Action "step forward".
     * 
     * @throws ExecutionException on VM failure
     */
    public final void actionStepForward() throws ExecutionException {
        if (!vm.isRunning()) {
            return;
        }

        if (vm.getProgram().getSize() == 0) {
            return;
        }

        stateStack.push(vm.step());
        update();
    }

    /**
     * Create a new {@link Compiler} for this tab.
     * 
     * @return a new compiler
     */
    protected abstract Compiler createCompiler();

    /**
     * Create a new {@link DotBackend} for this tab.
     * 
     * @return a new dotbackend
     */
    protected abstract DotBackend createDotBackend();

    /**
     * Create a new {@link Parser} for this tab.
     * 
     * @return a new parser
     */
    protected abstract Parser<?, ?> createParser();

    /**
     * Create a new {@link LocatedReader} for the source in the source panel.
     * 
     * @return a new located reader
     */
    protected final LocatedReader createReader() {
        return new LocatedReader(sourcePanel.getSource(), getName());
    }

    /**
     * Returns the source code file that is associated with this tab.
     * 
     * @return the source code file or {@code null}
     */
    public final File getAssociatedFile() {
        return associatedFile;
    }

    /**
     * Returns the highlighting color for the given {@link MemoryObject}.
     * 
     * @param object a MemoryObject
     * @return a {@link Color} object or {@code null}
     */
    public final Color getColorMemoryObject(final Object object) {
        return colorMemoryObjects.get(object);
    }

    /**
     * Returns the highlighting color for the character at the given source code location.
     * 
     * @param index location in the source code
     * @return a {@link Color} object or {@code null}
     */
    public final Color getColorSourceCode(final int index) {
        return colorSourceCode.get(index);
    }

    /**
     * Returns the highlighting color for a given {@link TreeNode} object.
     * 
     * @param node a TreeNode
     * @return a {@link Color} object or {@code null}
     */
    public final Color getColorTreeNode(final Object node) {
        return colorTreeNode.get(node);
    }

    /**
     * Returns a 32x32 px icon for this tab.
     * 
     * @return a 32x32 px icon for this tab
     */
    public abstract ImageIcon getIconBig();

    /**
     * Returns a 16x16 px icon for this tab.
     * 
     * @return a 16x16 px icon for this tab
     */
    public abstract ImageIcon getIconSmall();

    /**
     * Returns this tab's VM.
     * 
     * @return this tab's VM
     */
    public final JumpVM getVm() {
        return vm;
    }

    /**
     * Sets the source code file that is associated with this tab.
     * 
     * @param file new associated source code file
     */
    public final void setAssociatedFile(final File file) {
        associatedFile = file;
    }

    /**
     * Remove all highlights.
     */
    public final void setHighlight() {
        colorMemoryObjects.clear();
        colorSourceCode.clear();
        colorTreeNode.clear();
        update();
    }

    /**
     * Highlight an AstNode.
     * 
     * @param node node to highlight
     */
    public final void setHighlight(final TreeNode node) {
        colorMemoryObjects.clear();
        colorSourceCode.clear();
        colorTreeNode.clear();

        setHighlight(node, HIGHLIGHT_COLORS[0]);
        for (int i = 0; i < node.getChildCount(); ++i) {
            setHighlight(node.getChildAt(i), HIGHLIGHT_COLORS[1 + (i % (HIGHLIGHT_COLORS.length - 1))]);
        }

        update();
    }

    /**
     * Set the highlight for a {@link TreeNode} to a given color.
     * 
     * @param treeNode tree node
     * @param color color
     */
    private void setHighlight(final TreeNode treeNode, final Color color) {
        if (treeNode instanceof AstNode<?>) {
            final AstNode<?> astNode = (AstNode<?>) treeNode;
            /* highlight sourcecode */
            for (int i = astNode.getBegin().getCharacter(); i < astNode.getEnd().getCharacter(); ++i) {
                colorSourceCode.put(i, color);
            }

            /* highlight program memory */
            for (final Instruction instruction : vm.getProgram().getContent()) {
                if (astNode == instruction.getSourceNode()) {
                    colorMemoryObjects.put(instruction, color);
                }
            }
        }
        /* highlight ast */
        colorTreeNode.put(treeNode, color);
        for (int i = 0; i < treeNode.getChildCount(); ++i) {
            setHighlight(treeNode.getChildAt(i), color);
        }
    }

    /**
     * Fill the source panel with the content of this input stream.
     * 
     * @param stream new source code
     */
    public final void setSource(final InputStream stream) {
        sourcePanel.setSource(stream);
    }

    /** Update GUI elements. */
    private void update() {
        sourcePanel.update();
        treePanel.update();
        for (final JumpMemoryPanel memoryPanel : memoryPanelList) {
            memoryPanel.update();
        }
    }
}
