package Pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dialog;

public class Menú extends JFrame {
	private static final long serialVersionUID = 1L;
	static Map<String, Nodo> nodosMap = new HashMap<>();
		private LinkedList<Nodo> Nodos = new LinkedList<>();
		private LinkedList<Edge> Edges = new LinkedList<>();
		private LinkedList<Nodo> NodosOriginales = new LinkedList<>();
		private LinkedList<Edge> EdgesOriginales = new LinkedList<>();
		double xv = 0,xv2 = 0,yv = 0,yv2 = 0;
		
	private JPanel contentPane;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menú frame = new Menú();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public Menú() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 652, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(40, 40, 40, 40));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel Titulo = new JLabel("Mapa Por XML");
		Titulo.setForeground(new Color(255, 128, 0));
		Titulo.setFont(new Font("Arial", Font.BOLD, 16));
		Titulo.setBounds(182, 23, 134, 29);
		contentPane.add(Titulo);
		
		
		JButton LeerNodo = new JButton("Leer Nodos");
		
		LeerNodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File("C:\\Users\\LabCivil5-Pc8\\eclipse-workspace\\Proyecto_MapavCasiFinal");
				JFileChooser fileChooser = new 	JFileChooser();
				fileChooser.setCurrentDirectory(f);
				 int returnValue = fileChooser.showOpenDialog(null);

	                if (returnValue == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileChooser.getSelectedFile();
	                    leerNodoXML(selectedFile);
	                }
			}

			private LinkedList<Nodo> leerNodoXML(File selectedFile) {
				
		        try {
		            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder builder = factory.newDocumentBuilder();
		            Document doc = builder.parse(selectedFile);

		            NodeList nodeList = doc.getElementsByTagName("row");
		            for (int i = 0; i < nodeList.getLength(); i++) {
		                Element elemento = (Element) nodeList.item(i);
		                double x = Double.parseDouble(elemento.getElementsByTagName("x").item(0).getTextContent());
		                double y = Double.parseDouble(elemento.getElementsByTagName("y").item(0).getTextContent());
		                String osmid = elemento.getElementsByTagName("osmid").item(0).getTextContent();
		                Nodo nodo = new Nodo(x, y, osmid);
		                Nodos.add(nodo);
		                if(xv2 == 0) {
		                	xv2 = x;
		                }else {
		                	if(xv2 < x) {
		                		xv2 = x;
		                	}
		                }
		                if(xv > x) {
		                	xv = x;
		                }
		                if(yv2 == 0) {
		                	yv2 = y;
		                }else {
		                	if(yv2 < y) {
		                		yv2 = y;
		                	}
		                }
		                if(yv > y) {
		                	yv = y;
		                }
		                
		            }
		           
		            for(Nodo nodo: Nodos) {
		            	nodo.setXv(xv);
		            	nodo.setXv2(xv2);
		            	nodo.setYv(yv);
		            	nodo.setYv2(yv2);
		            }
		            System.out.println("X: "+xv+" X2: "+xv2);
		            System.out.println("Y: "+yv+" Y2: "+yv2);
		            NodosOriginales.addAll(Nodos);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        return Nodos;
				
			}
		});
		LeerNodo.setFont(new Font("Arial Black", Font.PLAIN, 14));
		LeerNodo.setBounds(234, 200, 144, 21);
		contentPane.add(LeerNodo);
		
		JButton LeerEdges = new JButton("Leer Edges");
		LeerEdges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File("C:\\Users\\LabCivil5-Pc8\\eclipse-workspace\\Proyecto_MapavCasiFinal");
				JFileChooser fileChooser = new 	JFileChooser();
				fileChooser.setCurrentDirectory(f);
				 int returnValue = fileChooser.showOpenDialog(null);

	                if (returnValue == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileChooser.getSelectedFile();
	                    leerEdgeXML(selectedFile);
	                }
			}
			//u y v son osim de nodos
			private LinkedList<Edge> leerEdgeXML(File selectedFile) {
		        try {
		            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder builder = factory.newDocumentBuilder();
		            Document doc = builder.parse(selectedFile);

		            NodeList nodeList = doc.getElementsByTagName("edge");
		            for (int i = 0; i < nodeList.getLength(); i++) {
		                Element elemento = (Element) nodeList.item(i);
		                String u = elemento.getElementsByTagName("u").item(0).getTextContent();
		                String v = elemento.getElementsByTagName("v").item(0).getTextContent();
		                int k = Integer.parseInt(elemento.getElementsByTagName("k").item(0).getTextContent());
		                String osmid = elemento.getElementsByTagName("osmid").item(0).getTextContent();
		                String name = elemento.getElementsByTagName("name").item(0).getTextContent();

		                //Busca y asocia los nodos usando el mapa
		                Nodo nodoFuente = nodosMap.get(u);
		                Nodo nodoDestino = nodosMap.get(v);
		                Edge edge = new Edge(u, v, k, osmid, name, nodoFuente, nodoDestino);
		                //Edge edge = new Edge(u, v, k, osmid, name);
		                Edges.add(edge);
		            }
		            EdgesOriginales.addAll(Edges);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        return Edges;
			}
		});
		LeerEdges.setFont(new Font("Arial Black", Font.PLAIN, 14));
		LeerEdges.setBounds(234, 241, 144, 21);
		contentPane.add(LeerEdges);
		
		JButton Mostrar_Mapa = new JButton("Mostrar Mapa");
		Mostrar_Mapa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, Nodo> nodosMap = crearDiccionarioNodos(Nodos);
		        for (Edge Edge : Edges) {
		            Nodo nodoFuente = nodosMap.get(Edge.getU());
		            Nodo nodoDestino = nodosMap.get(Edge.getV());
		            Edge.setNodoFuente(nodoFuente);
		            Edge.setNodoDestino(nodoDestino);
		        }
		        
	    	    Ventana panel = new Ventana(Nodos, Edges, xv,yv,xv2,yv2);

	    	    // Crear un JScrollPane que contenga el panel App
	    	    JScrollPane scrollPane = new JScrollPane(panel);

	    	    // Configurar el comportamiento de las barras de desplazamiento
	    	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	    	    // Crear el JFrame y configurarlo
	    	    JFrame frame = new JFrame("Dibujar Nodos y Bordes desde XML");
	    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	    frame.setSize(800, 600);

	    	    // Agregar el JScrollPane al contenido del JFrame
	    	    frame.getContentPane().add(scrollPane);

	    	    // Hacer visible el JFrame
	    	    frame.setVisible(true);
			}
		});
		Mostrar_Mapa.setFont(new Font("Arial Black", Font.PLAIN, 14));
		Mostrar_Mapa.setBounds(234, 282, 144, 21);
		contentPane.add(Mostrar_Mapa);
		
		JLabel BackGroundImage = new JLabel("");
		BackGroundImage.setFont(new Font("Arial Black", Font.PLAIN, 14));
		BackGroundImage.setLabelFor(BackGroundImage);
		BackGroundImage.setIcon(new ImageIcon("Image.png"));
		BackGroundImage.setBounds(0, 0, 638, 356);
		contentPane.add(BackGroundImage);
		
		
	}
	
    private static Map<String, Nodo> crearDiccionarioNodos(List<Nodo> nodos) {
        Map<String, Nodo> nodosMap = new HashMap<>();
        for (Nodo nodo : nodos) {
            nodosMap.put(nodo.getOsmid(), nodo);
        }
        return nodosMap;
    }
    


    
}
