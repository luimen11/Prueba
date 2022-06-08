import Gramatica.*;

public class Visitor extends GramaticaBaseVisitor<Object> {


    public Object visitStart(GramaticaParser.StartContext ctx){
        System.out.print(visit(ctx.linstrucciones()));
        return visit(ctx.linstrucciones());
    }

    public Object visitBlock(GramaticaParser.BlockContext ctx){
        return visit(ctx.linstrucciones());
    }

    public Object visitDecl(GramaticaParser.DeclContext ctx){
        return visit(ctx.declaration());
    }

    public String visitDeclaration(GramaticaParser.DeclarationContext ctx){
        return "Variable de tipo " + visit(ctx.type()).toString() + " nombre: " + ctx.IDEN().getText() + " con valor " + visit(ctx.expr()).toString();
    }

    public String visitType(GramaticaParser.TypeContext ctx){
        return ctx.getText();
    }

    public Object visitOpExpr(GramaticaParser.OpExprContext ctx){
        int izq = (int)visit(ctx.left);
        int der = (int)visit(ctx.right);
        String op = ctx.op.getText();

        switch (op.charAt(0)){

            case '*': return izq*der;
            case '/': return izq/der;
            case '+': return izq+der;
            case '-': return izq-der;
            default: throw new IllegalArgumentException("Operacion no valida");
        }
    }


    public String visitStrExpr(GramaticaParser.StrExprContext ctx){
        return String.valueOf(ctx.str.getText());
    }

    public Integer visitAtomExpr(GramaticaParser.AtomExprContext ctx){
        return Integer.valueOf(ctx.getText());
    }
}
