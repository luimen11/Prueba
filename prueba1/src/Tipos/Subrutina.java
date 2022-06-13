package Tipos;

import java.util.ArrayList;
import Entorno.Simbolo;
import Gramatica.GramaticaParser;


public class Subrutina {

    public String nombre;
    public ArrayList<Simbolo> lparametros;
    public Object linstrucciones;
    public GramaticaParser.LdeclPContext ldeclaracionParam;

    public Subrutina(String nombre, ArrayList<Simbolo> lparametros, Object linstrucciones, GramaticaParser.LdeclPContext ldeclaracionParam) {
        this.nombre = nombre;
        this.lparametros = lparametros;
        this.linstrucciones = linstrucciones;
        this.ldeclaracionParam = ldeclaracionParam;
    }
}
