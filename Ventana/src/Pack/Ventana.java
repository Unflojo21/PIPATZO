package Pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ExecutorService executor;
    private boolean stopRequested = false; //Detener lectura de archivo 
    private JFrame FrameDibujo; //Funcion utilizado para dibujar 
    private LinkedList<Nodo> Nodos = new LinkedList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana frame = new Ventana();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ventana() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
        executor = Executors.newSingleThreadExecutor();
		
		JButton LeerArchivo = new JButton("Leer");
		LeerArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File selected = fileChooser.getSelectedFile();
					lecturaXML(selected);
					
				}
			}
		});
		LeerArchivo.setFont(new Font("Tahoma", Font.BOLD, 15));
		LeerArchivo.setBounds(257, 138, 125, 26);
		contentPane.add(LeerArchivo);
		
		JButton Detener = new JButton("Detener");
		Detener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                stopRequested = true; // Solicitar detener la lectura
			}
		});
		Detener.setFont(new Font("Tahoma", Font.BOLD, 15));
		Detener.setBounds(257, 187, 125, 26);
		contentPane.add(Detener);
		
		JLabel Fondo = new JLabel("");
		Fondo.setHorizontalAlignment(SwingConstants.CENTER);
		Fondo.setIcon(new ImageIcon("C:\\Users\\Natanael\\Desktop\\escritorio literal\\img\\starcraft-terran-race-rule-j8l4y3zr11bppk5v.jpg"));
		Fondo.setBounds(0, 0, 635, 351);
		contentPane.add(Fondo);
		
	}
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
    
    private void lecturaXML(File archivoXML) {
    	FrameDibujo = new JFrame();
    	FrameDibujo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	FrameDibujo.setLocationRelativeTo(null);
    	FrameDibujo.getContentPane().add(new JPanel(), BorderLayout.CENTER);
    	FrameDibujo.setSize(500, 500);
    	FrameDibujo.setVisible(true);
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
            		String osmid = elementoRow.getElementsByTagName("osmid").item(0).getTextContent();
            		
                    double yCoord = Double.parseDouble(y)*(-1);
                    double xCoord = Double.parseDouble(x)*(-1);
                    
                    // Escala las coordenadas para ajustarlas al JPanel
                    //int panelX = (int) (xCoord * getWidth()); // Escala en el ancho
                    //int panelY = (int) (yCoord * getHeight()); // Escala en la altura
                    System.out.println(xCoord+" "+yCoord);
                    Nodo n = new Nodo(xCoord,yCoord,osmid);
                    Nodos.add(n);
            }//Termino for
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Nodo n : Nodos) {
        	
        }
        
    }
}
