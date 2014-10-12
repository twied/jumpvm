import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

public class JumpGrammarTest {
    static enum Type {
        MAMA, WIMA, PAMA
    }

    private static CommonTree getTree(final Type type, final ANTLRReaderStream stream) throws Exception {
        switch (type) {
        case MAMA:
            return new MaMaGrammarParser(new CommonTokenStream(new MaMaGrammarLexer(stream))).program().getTree();
        case WIMA:
            return new WiMaGrammarParser(new CommonTokenStream(new WiMaGrammarLexer(stream))).program().getTree();
        case PAMA:
            return new PaMaGrammarParser(new CommonTokenStream(new PaMaGrammarLexer(stream))).program().getTree();
        default:
            throw new IllegalArgumentException();
        }
    }

    public static void main(final String... args) throws IOException {
        final PrintStream err = System.err;
        final PrintStream out = System.out;
        boolean ok = true;

        for (final Type type : Type.values()) {
            final BufferedReader fileListReader = new BufferedReader(new FileReader("../../res/source/" + type));
            while (true) {
                final String fileName = fileListReader.readLine();
                if (fileName == null) {
                    break;
                }
                try {
                    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    System.setOut(new PrintStream(stream));
                    System.setErr(new PrintStream(stream));

                    final CommonTree result = getTree(type, new ANTLRReaderStream(new FileReader("../../res/source/" + fileName)));
                    if (stream.size() != 0) {
                        ok = false;

                        err.flush();
                        out.flush();

                        err.println(fileName);
                        err.flush();
                        out.flush();

                        out.println(stream.toString());
                        out.println(result.toStringTree());
                        err.flush();
                        out.flush();
                    }
                } catch (final Exception e) {
                    e.printStackTrace(err);
                }
            }
            fileListReader.close();
        }

        if (ok) {
            out.println("Check OK");
        }
    }
}
