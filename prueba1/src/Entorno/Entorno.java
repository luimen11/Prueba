package Entorno;


import com.sun.source.tree.NewArrayTree;

import java.util.HashMap;
import java.util.Locale;

public class Entorno {

    public HashMap<String, Simbolo> TablaSimbolo;
    public Entorno anterior;

    public Entorno(Entorno anterior) {
        this.anterior = anterior;
        TablaSimbolo = new HashMap<String, Simbolo>();
    }


    public void nuevoSimbolo(String nombre, Simbolo nuevo){
        if (TablaSimbolo.containsKey(nombre.toUpperCase())){
            //agregar a la lista de errores
            System.out.println("La variable " + nombre + " ya existe");
        }
        else{
            TablaSimbolo.put(nombre.toUpperCase(), nuevo);
        }
    }
}
