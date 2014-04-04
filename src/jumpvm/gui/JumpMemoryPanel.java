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

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jumpvm.code.Instruction;
import jumpvm.memory.Memory;
import jumpvm.memory.Program;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.MemoryObject;

/**
 * Jump memory panel.
 */
public class JumpMemoryPanel extends JPanel implements ListCellRenderer<MemoryObject>, ListSelectionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Default cell color. */
    private static final Color CELL_COLOR_DEFAULT = Color.WHITE;

    /** Organizational cell color. */
    private static final Color CELL_COLOR_ORGANIZATIONAL = new Color(0xd3d7cf);

    /** Parent JumpTab. */
    private final JumpTab tab;

    /** GUI element: Memory content. */
    private final JList<MemoryObject> list;

    /** Underlying memory. */
    private final Memory<?> memory;

    /**
     * Create a new JumpMemoryPanel.
     * 
     * @param memory memory
     * @param tab parent JumpTab
     */
    public JumpMemoryPanel(final Memory<? extends MemoryObject> memory, final JumpTab tab) {
        this.list = new JList<MemoryObject>(memory);
        this.tab = tab;
        this.memory = memory;

        list.setCellRenderer(this);
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        setBorder(new TitledBorder(memory.getName()));

        add(new JScrollPane(list));
    }

    @Override
    public final Component getListCellRendererComponent(final JList<? extends MemoryObject> jlist, final MemoryObject object, final int index, final boolean isSelected, final boolean cellHasFocus) {
        if (object == null) {
            final JLabel l = new JLabel("JumpMemoryPanel.getListCellRendererComponent() value = null!");
            l.setBackground(Color.RED);
            l.setOpaque(true);
            return l;
        }

        final String displayType = object.getDisplayType();
        final String displayValue = object.getDisplayValue();
        final String displayDescription = object.getDisplayDescription();
        final String displayHoverText = object.getDisplayHoverText();

        final Box box = Box.createHorizontalBox();
        box.setOpaque(true);

        final Color color = tab.getColorMemoryObject(object) == null ? CELL_COLOR_DEFAULT : tab.getColorMemoryObject(object);
        box.setBackground(color);

        final JLabel label = new JLabel(String.format("<html><b>%04d</b> %s%s</html>", index, displayType == null ? "" : "[" + displayType + "] ", displayValue));
        label.setFont(JumpGui.FONT_MONOSPACED);
        box.add(label);

        if (displayDescription != null) {
            box.add(Box.createHorizontalGlue());
            box.add(new JLabel(displayDescription));
        }

        if (displayHoverText != null) {
            box.setToolTipText(displayHoverText);
        }

        if (memory instanceof Program) {
            if (tab.getVm().getProgram().getContent().indexOf(object) == tab.getVm().getProgramCounter().getValue()) {
                box.setBorder(new MatteBorder(0, JumpGui.DEFAULT_SPACING, 0, 0, Color.BLACK));
            } else {
                box.setBorder(new MatteBorder(0, JumpGui.DEFAULT_SPACING, 0, 0, color));
            }
        } else if (memory instanceof Stack) {
            final Stack stack = (Stack) memory;
            for (final int start : stack.getFrames().keySet()) {
                if (start == index) {
                    box.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
                }

                if ((start <= index) && (index < stack.getFrames().get(start))) {
                    box.setBackground(CELL_COLOR_ORGANIZATIONAL);
                }
            }
        }

        return box;
    }

    /**
     * Update this panel.
     */
    public final void update() {
        final int selection = list.getSelectedIndex();
        if (selection >= 0) {
            list.ensureIndexIsVisible(selection);
            list.setSelectedIndices(new int[] {});
            return;
        }

        if (memory instanceof Program) {
            list.ensureIndexIsVisible(tab.getVm().getProgramCounter().getValue());
        }

        repaint();
    }

    @Override
    public final void valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        final int selection = list.getSelectedIndex();

        if (selection < 0) {
            /* no selection. */
            return;
        }

        final Object object = list.getModel().getElementAt(selection);
        if (object instanceof Instruction) {
            tab.setHighlight(((Instruction) object).getSourceNode());
        }

        repaint();
    }
}
