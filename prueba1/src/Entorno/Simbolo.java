package Entorno;

public class Simbolo {

    public String tipo;
    public Object valor;
    public String identificador;
    public TipoSimbolo tipoSimbolo;

    public enum TipoSimbolo
    {
        Variable,
        Funcion,
        Subrutina,
        Parametro,
        Nativa
    }


    public Simbolo(String id, String tipo, Object valor, TipoSimbolo tipoS) {
        this.identificador = id;
        this.tipo = tipo;
        this.valor = valor;
        this.tipoSimbolo = tipoS;
    }
}
