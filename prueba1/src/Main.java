import Entorno.Entorno;
import Gramatica.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import static org.antlr.v4.runtime.CharStreams.fromString;

public class Main {

    public static void main(String[] args) {

        Principal principal = new Principal();
/*
        String input = "int var1 = 10; imprimir(var1); {int var1 = 12; imprimir(var1);} ";
        CharStream cs = fromString(input);
        GramaticaLexer lexico = new GramaticaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexico);
        GramaticaParser sintactico = new GramaticaParser(tokens);
        GramaticaParser.StartContext startCtx = sintactico.start();

        Visitor visitor = new Visitor(new Entorno(null));
        //Object a;
        //a = visitor.visit(startCtx);
        visitor.visit(startCtx);
*/

    }
}
