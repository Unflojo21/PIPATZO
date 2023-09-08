package a;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class Ventana extends JFrame {
    private JPanel contentPane;
    private JButton btnLecturaArchivo;
    private JButton btnDetener;
    private ExecutorService executor;
    private boolean stopRequested = false; //Detener lectura de archivo 
    private JPanel PanelDibujo; //Funcion utilizado para dibujar 

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Ventana frame = new Ventana();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ventana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        PanelDibujo = new JPanel() {
        	protected void paintComponent(Graphics g){
        		super.paintComponent(g);
        		//Dibujar grafo
        	}
        };
        
        //Botones de Ventana
        JLabel lblTitulo = new JLabel("LECTURA DE ARCHIVO");
        lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(124, 29, 186, 40);
        contentPane.add(lblTitulo);

        btnLecturaArchivo = new JButton("Leer archivo");
        btnLecturaArchivo.setBounds(137, 123, 171, 40);
        contentPane.add(btnLecturaArchivo);

        btnDetener = new JButton("Detener lectura");
        btnDetener.setBounds(137, 180, 171, 40);
        contentPane.add(btnDetener);

        executor = Executors.newSingleThreadExecutor();

        //Boton de lectura de archivo
        btnLecturaArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //Leer y analizar archivo xml
                    lecturaXML(selectedFile);
                    stopRequested = false; // Reiniciar la bandera de detener
                    //processFileInBackground(selectedFile);
                }
            }
        });

        //Boton de detener lectura de archivo
        btnDetener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRequested = true; // Solicitar detener la lectura
            }
        });
    }

    //Crear funcion xml que entienda JFrame
    private void lecturaXML(File archivoXML) {
        try {
            // Crea una fábrica de constructores de documentos
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Analiza el archivo XML
            Document doc = dBuilder.parse(archivoXML);

            // Normaliza el documento
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("row");
            
            for(int i = 0;i<nodeList.getLength();i++){
            		Element elementoRow = (Element) nodeList.item(i);
            		
            		//Obtener valores del nodo
            		String y = elementoRow.getElementsByTagName("y").item(0).getTextContent();
            		String x = elementoRow.getElementsByTagName("x").item(0).getTextContent();
            		
            		//Dibujar
            		setTitle("Archivo xml");
            		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	    setBounds(100, 100, 800, 600);
            	    
            	    PanelDibujo = new JPanel() {
            	    	protected void paintComponent(Graphics g){
            	    		super.paintComponent(g);
            	    		// Convierte las coordenadas String en números
                            double yCoord = Double.parseDouble(y);
                            double xCoord = Double.parseDouble(x);

                            // Escala las coordenadas para ajustarlas al JPanel
                            int panelX = (int) (xCoord * getWidth()); // Escala en el ancho
                            int panelY = (int) (yCoord * getHeight()); // Escala en la altura

                            // Dibuja un punto en las coordenadas escaladas
                            g.setColor(Color.RED);
                            g.fillOval(panelX - 2, panelY - 2, 4, 4); // Dibuja un pequeño círculo en el punto
            	    	}
            	    };//Panel dibujo
            		
            }//Termino for
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    //Procesamiento de multihilos del archivo
    private void processFileInBackground(File file) {
        executor.submit(() -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                
                //Detener lectura
                while ((line = reader.readLine()) != null && !stopRequested) {
                    System.out.println(line);
                }

                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
