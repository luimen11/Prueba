import Entorno.Entorno;
import Entorno.Simbolo;
import Entorno.Simbolo.*;
import Gramatica.*;
import Tipos.Subrutina;
import org.antlr.v4.runtime.tree.pattern.RuleTagToken;

import java.util.ArrayList;
import java.util.Stack;

public class Visitor extends GramaticaBaseVisitor<Object> {


    Stack<Entorno> pilaEnt = new Stack<Entorno>();
    protected String printable= "";

    public Visitor(Entorno ent){
        this.pilaEnt.push(ent);
    }

    public void setPrint(String texto){
        printable = printable + texto;
    }
    public String getPrint(){
        return printable;
    }

    public Object visitStart(GramaticaParser.StartContext ctx){
        return visitLinstrucciones(ctx.linstrucciones());
    }

    public Object visitLinstrucciones(GramaticaParser.LinstruccionesContext ctx)
    {
        for (GramaticaParser.InstruccionesContext ictx: ctx.instrucciones())
            visitInstrucciones(ictx);
        return true;
    }

    public Object visitInstrucciones(GramaticaParser.InstruccionesContext ctx)
    {
        if(ctx.block() != null)
            visitBlock(ctx.block());
        else if (ctx.declaration() != null)
            visitDeclaration(ctx.declaration());
        else if (ctx.print() != null)
            visitPrint(ctx.print());
        else if (ctx.subroutine() != null)
            visitSubroutine(ctx.subroutine());
        else if(ctx.call() != null)
            visitCall(ctx.call());

        return true;
    }

    public Object visitSubroutine(GramaticaParser.SubroutineContext ctx)
    {
        if(ctx.id1.getText().equals(ctx.id2.getText()))
        {

            if(pilaEnt.peek().TablaSimbolo.containsKey((ctx.id1.getText()+TipoSimbolo.Subrutina.name()).toUpperCase()))
            {
                ArrayList<Simbolo> parametros = new ArrayList<Simbolo>();
                for(GramaticaParser.ExprContext expCtx : ctx.lexpr().expr()){
                    parametros.add(new Simbolo(expCtx.getText(), "", null, TipoSimbolo.Parametro));
                }
                Subrutina subr = new Subrutina(ctx.id1.getText(), parametros, ctx.linstrucciones(), ctx.ldeclP());
                pilaEnt.peek().nuevoSimbolo(ctx.id1.getText() + TipoSimbolo.Subrutina.name(), new Simbolo(ctx.id1.getText(), "Subrutina", subr, TipoSimbolo.Subrutina ));
                return true;
            }
        }
        throw new RuntimeException("No coinciden los id");
    }

    public Object visitCall(GramaticaParser.CallContext ctx)
    {
        Entorno ent = pilaEnt.peek();
        Simbolo simbRutina = ent.Buscar(ctx.IDEN().getText() + TipoSimbolo.Subrutina.name());
        if(simbRutina == null) throw new RuntimeException("La subrutina no existe");
        else
        {
            Entorno entSubr = new Entorno(ent);
            Subrutina subr = (Subrutina) simbRutina.valor;
            //verificar cantidad de parametros de call igual a la declarada
            if(subr.lparametros.size() == ctx.lexpr().expr().size() && subr.lparametros.size() == subr.ldeclaracionParam.getChildCount())
            {
                for(int i = 0; i < ctx.lexpr().expr().size(); i++){
                    //asignando el valor del parametro
                    subr.lparametros.get(i).valor = visit(ctx.lexpr().expr().get(i));
                    //asumiendo que vienen en el mismo orden para asignar el tipo
                    subr.lparametros.get(i).tipo = subr.ldeclaracionParam.declParameters(i).type().getText();
                    //guardamos el parametro en el enorno de la subrutina
                    entSubr.nuevoSimbolo(subr.lparametros.get(i).identificador + TipoSimbolo.Variable.name(), subr.lparametros.get(i));
                }
                
                //ejecutar las instrucciones que esten adentro de la subrutina
                pilaEnt.push(entSubr);
                visitLinstrucciones((GramaticaParser.LinstruccionesContext)subr.linstrucciones);
                pilaEnt.pop();

            }
            else throw new RuntimeException("La cantidad de parametros no coincide");

        }
        return true;
    }

    public Object visitLexpr(GramaticaParser.LexprContext ctx){
        for(GramaticaParser.ExprContext ectx : ctx.expr())
            visit(ectx);
        return true;
    }

    public Object visitPrint(GramaticaParser.PrintContext ctx) {

        System.out.println(visit(ctx.expr()));
        setPrint(visit(ctx.expr()).toString() + '\n');
        return visit(ctx.expr());
    }


    public Object visitBlock(GramaticaParser.BlockContext ctx)
    {
        //creacion del nuevo entorno
        pilaEnt.push(new Entorno(pilaEnt.peek()));
        
        //se visita la produccion del bloque
        visitLinstrucciones(ctx.linstrucciones());
        
        //luego se remueve el entorno
        pilaEnt.pop();

        return true;
    }

    public Object visitDeclaration(GramaticaParser.DeclarationContext ctx)
    {
        Entorno ent= pilaEnt.peek();
        if(!ent.TablaSimbolo.containsKey((ctx.IDEN().getText() + TipoSimbolo.Variable.name()).toUpperCase())){
            Simbolo nuevo = new Simbolo(ctx.IDEN().getText(), ctx.type().getText(), visit(ctx.expr()), TipoSimbolo.Variable);
            ent.nuevoSimbolo(ctx.IDEN().getText() + TipoSimbolo.Variable.name(), nuevo);
            return true;
        }
        else{
            throw new RuntimeException("La variable existe en el entorno actual");
        }
        //return "Variable de tipo " + visit(ctx.type()).toString() + " nombre: " + ctx.IDEN().getText() + " con valor " + visit(ctx.expr()).toString();
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
        //return new Simbolo("", "string", String.valueOf(ctx.str.getText()), TipoSimbolo.Nativo);
    }

    public Object visitIdExpr(GramaticaParser.IdExprContext ctx)
    {
        Entorno ent = pilaEnt.peek();
        Simbolo id = ent.Buscar(ctx.IDEN().getText() + TipoSimbolo.Variable.name());
        if(id == null){
            throw new RuntimeException("La variable " + ctx.IDEN().getText() + " no fue encontrada");
        }
        else{
            //validar aqui el simbolo y los tipos
            return id.valor.toString();
        }
    }

    public Integer visitAtomExpr(GramaticaParser.AtomExprContext ctx){
        return Integer.valueOf(ctx.getText());
        //return new Simbolo("", "int", Integer.valueOf(ctx.getText()), TipoSimbolo.Nativa);
    }


}
