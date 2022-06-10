import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

import Gramatica.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import static org.antlr.v4.runtime.CharStreams.fromString;

public class Principal implements ActionListener {
    JButton btnInterpretar, btnTraducir, btnReportes;//creando variables globales de los botones
    JScrollPane scrollpane1, scrollpane2, scrollpane3;
    JTextArea jta1, jta2, jta3;
    JFrame jf = new JFrame("Proyecto 1);//creacion de ventana con el titulo");

    public Principal() {//constructor de la clase

        jf.setLayout(new FlowLayout());//Configurar como se dispondra el espacio del jframe
        Dimension d = new Dimension();//objeto para obtener el ancho de la pantalla

        //Areas de texto
        jta1     = new JTextArea(25,30);
        jta2     = new JTextArea(25,30);
        jta3     = new JTextArea(10,60);

        scrollpane1 = new JScrollPane(jta1);
        scrollpane2 = new JScrollPane(jta2);
        scrollpane3 = new JScrollPane(jta3);
        //Instanciando boton con texto
        btnInterpretar = new JButton("Interpretar");
        btnTraducir = new JButton("Traducir");
        btnReportes = new JButton("Reportes");

        //añadiendo objetos a la ventana

        jf.add(scrollpane1);
        jf.add(scrollpane2);
        jf.add(scrollpane3);
        jf.add(btnInterpretar);
        jf.add(btnTraducir);
        jf.add(btnReportes);

        //margenes para texto en boton
        btnInterpretar.setMargin(new Insets(1, 5, 1, 5));
        btnTraducir.setMargin(new Insets(1, 5, 1, 5));
        btnReportes.setMargin(new Insets(1, 5, 1, 5));

        //añadiendo el listener a los botones para manipular los eventos del click
        btnInterpretar.addActionListener(this);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//finaliza el programa cuando se da click en la X
        jf.setResizable(false);//para configurar si se redimensiona la ventana
        jf.setLocation((int) ((d.getWidth() / 2) + 290), 50);//para ubicar inicialmente donde se muestra la ventana (x, y)
        jf.setSize(700, 650);//configurando tamaño de la ventana (ancho, alto)
        jf.setVisible(true);//configurando visualización de la venta
    }

    @Override
    public void actionPerformed(ActionEvent e) {//sobreescribimos el metodo del listener

        String input, area2, consola;

        if(e.getSource()==btnInterpretar){//podemos comparar por el contenido del boton
            //int var1 = 54+17*78;
            input = jta1.getText();
            area2 = jta2.getText();
            consola = jta3.getText();

            CharStream cs = fromString(input);
            GramaticaLexer lexico = new GramaticaLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexico);
            GramaticaParser sintactico = new GramaticaParser(tokens);
            GramaticaParser.StartContext startCtx = sintactico.start();

            Visitor visitor = new Visitor();
            Object a;
            a = visitor.visit(startCtx);
            jta2.setText(a.toString());
        }

    }
}
