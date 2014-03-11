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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * JumpOutputPanel.
 */
public class JumpOutputPanel extends Writer implements ActionListener {
    /** Panel height in text lines. */
    private static final int SIZE_IN_LINES = 6;

    /** Writer that writes to this panel. */
    private final StringWriter writer;

    /** This JPanel. */
    private final JPanel panel;

    /** The text area. */
    private final JTextArea textArea;

    /** The "clear" button. */
    private final JButton button;

    /**
     * Create a new JumpOutputPanel.
     */
    public JumpOutputPanel() {
        this.writer = new StringWriter();
        this.panel = new JPanel();
        this.textArea = new JTextArea(SIZE_IN_LINES, SIZE_IN_LINES);
        this.button = new JButton("clear");

        textArea.setEditable(false);
        textArea.setFont(JumpGui.FONT_MONOSPACED);

        button.addActionListener(this);

        panel.setLayout(new BorderLayout(JumpGui.DEFAULT_SPACING, JumpGui.DEFAULT_SPACING));
        panel.setBorder(new TitledBorder("Output"));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(button, BorderLayout.LINE_END);
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        if (e.getSource() != button) {
            return;
        }
        writer.getBuffer().setLength(0);
        textArea.setText(writer.toString());
    }

    @Override
    public final void close() throws IOException {
        writer.close();
    }

    @Override
    public final void flush() throws IOException {
        writer.close();
    }

    /**
     * Returns this panel.
     * 
     * @return this panel
     */
    public final JPanel getPanel() {
        return panel;
    }

    @Override
    public final void write(final char[] cbuf, final int off, final int len) throws IOException {
        writer.write(cbuf, off, len);
        textArea.setText(writer.toString());
    }
}
