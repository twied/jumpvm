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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/**
 * JumpSourcePanel.
 */
public class JumpSourcePanel extends JPanel implements Runnable {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Maximum line number. */
    private static final int MAXIMUM_LINE_NUMBER = 1000;

    /** Parent JumpTab. */
    private final JumpTab tab;

    /** The editor pane. */
    private final JTextArea editorPane;

    /** The highlighter for the editor pane. */
    private final DefaultHighlighter highlighter;

    /**
     * Create a new JumpSourcePanel.
     * 
     * @param tab parent JumpTab
     */
    public JumpSourcePanel(final JumpTab tab) {
        this.tab = tab;
        this.editorPane = new JTextArea();
        this.highlighter = new DefaultHighlighter();
        highlighter.setDrawsLayeredHighlights(false);

        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Source"));
        editorPane.setFont(JumpGui.FONT_MONOSPACED);
        editorPane.setHighlighter(highlighter);
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i < MAXIMUM_LINE_NUMBER; ++i) {
            sb.append(String.format("%3d %n", i));
        }

        final JTextArea lineNumbers = new JTextArea(sb.toString());
        lineNumbers.setFont(editorPane.getFont());
        lineNumbers.setBackground(getBackground());
        lineNumbers.setEditable(false);
        lineNumbers.setFocusable(false);

        final JScrollPane scrollPane = new JScrollPane(editorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setRowHeaderView(lineNumbers);
        add(scrollPane);
        SwingUtilities.invokeLater(this);
    }

    /**
     * Return the source code as {@link Reader}.
     * 
     * @return the source code
     */
    public final Reader getSource() {
        return new StringReader(editorPane.getText());
    }

    @Override
    public final void run() {
        editorPane.requestFocusInWindow();
    }

    /**
     * Fill the text area with input from a given source.
     * 
     * @param stream input source
     */
    public final void setSource(final InputStream stream) {
        final InputStreamReader reader = new InputStreamReader(stream);
        final StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                int character;
                character = reader.read();
                if (character < 0) {
                    break;
                }

                sb.appendCodePoint(character);
            }
        } catch (final IOException e) {
            JumpGui.showExceptionDialog(this, e, "File could not be read.", "JumpVM error");
        }

        editorPane.setText(sb.toString());
        editorPane.setSelectionEnd(0);
    }

    /**
     * Update this panel.
     */
    public final void update() {
        highlighter.removeAllHighlights();

        final String text = editorPane.getText();
        for (int i = 0; i < text.length(); ++i) {
            final Color color = tab.getColorSourceCode(i);
            if (color == null) {
                continue;
            }

            try {
                highlighter.addHighlight(i, i + 1, new DefaultHighlightPainter(color));
            } catch (final BadLocationException e) {
                JumpGui.showExceptionDialog(this, e, "Highlighting went wrong.", "JumpVM error");
            }
        }
    }
}
