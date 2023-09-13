import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class App extends JFrame {

    private JPanel contentPane;
    private DrawingPanel drawingPanel; // Panel personalizado para dibujar
    private List<Node> nodesList;
    private List<Edge> edgesList;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App frame = new App();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public App() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        drawingPanel = new DrawingPanel();
        drawingPanel.setBounds(10, 10, 760, 540); // Ajusta el tamaño y la posición del panel según tus necesidades
        contentPane.add(drawingPanel);

        JButton btnLecturaArchivo = new JButton("Leer archivo");
        btnLecturaArchivo.setBounds(300, 560, 150, 40); // Ajusta la posición y el tamaño del botón según tus necesidades
        contentPane.add(btnLecturaArchivo);

        // Boton de lectura de archivo
        btnLecturaArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        lecturaXML(selectedFile);
                        drawingPanel.repaint(); // Vuelve a dibujar el panel después de cargar los datos
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void lecturaXML(File selectedFile) throws Exception, IOException {
        String archivo = selectedFile.getName();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(selectedFile);

        if (archivo.equals("nodes.xml")) {
            // Obtener la lista de nodos
            NodeList nodeList = doc.getElementsByTagName("row");
            // Iterar a través de los nodos
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elementoRow = (Element) nodeList.item(i);
                String osmid = elementoRow.getElementsByTagName("osmid").item(0).getTextContent();
                String latitud = elementoRow.getElementsByTagName("y").item(0).getTextContent();
                String longitud = elementoRow.getElementsByTagName("x").item(0).getTextContent();
                String streetCount = elementoRow.getElementsByTagName("street_count").item(0).getTextContent();
                String highway = elementoRow.getElementsByTagName("highway").item(0).getTextContent();
                String geometry = elementoRow.getElementsByTagName("geometry").item(0).getTextContent();

                // Crear un objeto Nodo y agregarlo a la lista de nodos
                Node node = new Node(osmid, latitud, longitud, streetCount, highway, geometry);
                nodesList.add(node);
            }
        } else if (archivo.equals("edges.xml")) {
            // Procesar aristas
            NodeList nodeList = doc.getElementsByTagName("edge");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elementoEdge = (Element) nodeList.item(i);

                String u = elementoEdge.getElementsByTagName("u").item(0).getTextContent();
                String v = elementoEdge.getElementsByTagName("v").item(0).getTextContent();
                String k = elementoEdge.getElementsByTagName("k").item(0).getTextContent();
                String osmid = elementoEdge.getElementsByTagName("osmid").item(0).getTextContent();
                String name = elementoEdge.getElementsByTagName("name").item(0).getTextContent();

                // Crear un objeto Arista y agregarlo a la lista de aristas
                Edge edge = new Edge(u, v, k, osmid, name);
                edgesList.add(edge);
            }
        }
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibuja los nodos
            for (Node node : nodesList) {
                int x = Integer.parseInt(node.latitud); // Convierte latitud a entero (ajusta según tus necesidades)
                int y = Integer.parseInt(node.longitud); // Convierte longitud a entero (ajusta según tus necesidades)

                // Dibuja un círculo para representar el nodo
                g.setColor(Color.BLUE);
                g.fillOval(x - 10, y - 10, 20, 20);

                // Dibuja la etiqueta del nodo
                g.setColor(Color.BLACK);
                g.drawString(node.osmid, x - 10, y - 20);
            }

            // Dibuja las aristas
            for (Edge edge : edgesList) {
                // Obtén las coordenadas de los nodos u y v (ajusta según tus necesidades)
                int x1 = Integer.parseInt(edge.u.latitud);
                int y1 = Integer.parseInt(edge.u.longitud);
                int x2 = Integer.parseInt(edge.v.latitud);
                int y2 = Integer.parseInt(edge.v.longitud);

                // Dibuja una línea para representar la arista
                g.setColor(Color.RED);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
    
    // Clase para representar un nodo
    private class Node {
        private String osmid;
        private String latitud;
        private String longitud;
        private String streetCount;
        private String highway;
        private String geometry;

        public Node(String osmid, String latitud, String longitud, String streetCount, String highway, String geometry) {
            this.osmid = osmid;
            this.latitud = latitud;
            this.longitud = longitud;
            this.streetCount = streetCount;
            this.highway = highway;
            this.geometry = geometry;
        }
    }

    // Clase para representar una arista
    private class Edge {
        private Node u; // Nodo inicial
        private Node v; // Nodo final
        private String k;
        private String osmid;
        private String name;

        public Edge(Node u, Node v, String k, String osmid, String name) {
            this.u = u;
            this.v = v;
            this.k = k;
            this.osmid = osmid;
            this.name = name;
        }

		public Edge(String u2, String v2, String k2, String osmid2, String name2) {
			// TODO Auto-generated constructor stub
		}
    }
}
