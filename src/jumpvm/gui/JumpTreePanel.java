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
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jumpvm.ast.AstNode;

/**
 * Jump tree panel.
 */
public class JumpTreePanel extends JPanel implements MouseListener, TreeCellRenderer, TreeSelectionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Parent JumpTab. */
    private final JumpTab tab;

    /** GUI element: tree view. */
    private final JTree tree;

    /**
     * Create a new JumpTreePanel.
     * 
     * @param tab parent JumpTab
     */
    public JumpTreePanel(final JumpTab tab) {
        this.tab = tab;
        this.tree = new JTree(new Object[] {});

        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Tree"));

        tree.setBackground(Color.WHITE);
        tree.setOpaque(true);
        tree.setCellRenderer(this);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
        add(new JScrollPane(tree));
    }

    @Override
    public final Component getTreeCellRendererComponent(final JTree jtree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        final JLabel label = new JLabel(String.valueOf(value));
        final Color color = tab.getColorTreeNode(value);

        if (color == null) {
            label.setBackground(Color.WHITE);
        } else {
            label.setBackground(color);
        }

        label.setOpaque(true);
        return label;
    }

    @Override
    public final void mouseClicked(final MouseEvent e) {
        final int row = tree.getRowForLocation(e.getX(), e.getY());
        if (row == -1) {
            tree.clearSelection();
            tab.setHighlight();
        }
    }

    @Override
    public final void mouseEntered(final MouseEvent e) {
        /* ignore */
        return;
    }

    @Override
    public final void mouseExited(final MouseEvent e) {
        /* ignore */
        return;
    }

    @Override
    public final void mousePressed(final MouseEvent e) {
        /* ignore */
        return;
    }

    @Override
    public final void mouseReleased(final MouseEvent e) {
        /* ignore */
        return;
    }

    /**
     * Set the tree's root node.
     * 
     * @param root new root node
     */
    public final void setTree(final AstNode<?> root) {
        tree.setModel(new DefaultTreeModel(root));
        for (int i = 0; i < tree.getRowCount(); ++i) {
            tree.expandRow(i);
        }
    }

    /**
     * Update this panel.
     */
    public final void update() {
        tree.repaint();
    }

    @Override
    public final void valueChanged(final TreeSelectionEvent e) {
        final TreePath path = e.getNewLeadSelectionPath();
        if (path == null) {
            return;
        }

        final Object object = path.getLastPathComponent();
        if (object == null) {
            return;
        }

        if (object instanceof TreeNode) {
            tab.setHighlight((TreeNode) path.getLastPathComponent());
        }
    }
}
