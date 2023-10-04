// App.java
package Pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.List;
//import java.util.Map;
import java.util.ArrayList;
//import java.util.HashMap;
import java.text.DecimalFormat;

public class Ventana extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<Nodo> nodos;
    private List<Edge> bordes;
    private double zoom = 1.0;
    private int prevMouseX;
    private int prevMouseY;
    private int panX;
    private int panY;
    private boolean panning = false;
    private double xv,yv,xv2,yv2;
    private List<Nodo> nodosSeleccionados = new ArrayList<>();

    public Ventana(List<Nodo> nodos, List<Edge> bordes, double xv, double yv, double xv2, double yv2) {
        this.nodos = nodos;
        this.bordes = bordes;
        this.xv = xv;
        this.xv2 = xv2;
        this.yv = yv;
        this.yv2 = yv2;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    prevMouseX = e.getX();
                    prevMouseY = e.getY();
                    panning = true;
                    DecimalFormat f = new DecimalFormat("#.####");
                    // Buscar el nodo más cercano al punto de clic
                    Nodo nodoClic = encontrarNodoMasCercano(e.getX(), e.getY());

                    if (nodoClic != null) {
                        // Si ya hay 2 nodos seleccionados, borrar la lista
                        if (nodosSeleccionados.size() == 2) {
                        	for(Nodo n: nodosSeleccionados) {
                        		n.setDesmarcar();
                        	}
                            nodosSeleccionados.clear();
                        }
                        nodosSeleccionados.add(nodoClic);
                        nodoClic.setMarcado(panning);

                        // Mostrar los datos de los nodos seleccionados
                        if (nodosSeleccionados.size() == 2) {
                            System.out.println("Nodos seleccionados:\n");
                            for (Nodo nodo : nodosSeleccionados) {
                                System.out.println(nodo.toString());
                            }
                            
                            // Calcular y mostrar la distancia entre los nodos seleccionados
                            double distancia = calcularDistanciaEntreNodos(nodosSeleccionados.get(0), nodosSeleccionados.get(1));
                            String dist = f.format(distancia);
                            System.out.println("Distancia entre los nodos seleccionados: " + distancia);
                            JOptionPane.showMessageDialog(null, "Distancia entre los nodos seleccionados: \n" + dist+" Km\nNodo 1: "+nodosSeleccionados.get(0).getOsmid()+" \nX: "+f.format(nodosSeleccionados.get(0).getX())+"\nY: "+f.format(nodosSeleccionados.get(0).getY())+"\nNodo 2: "+nodosSeleccionados.get(1).getOsmid()+" \nX: "+f.format(nodosSeleccionados.get(1).getX())+"\nY: "+f.format(nodosSeleccionados.get(1).getY()));
                        }
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    panning = false;
                }
            }
        });


        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (panning) {
                    int deltaX = e.getX() - prevMouseX;
                    int deltaY = e.getY() - prevMouseY;

                    panX += deltaX;
                    panY += deltaY;

                    prevMouseX = e.getX();
                    prevMouseY = e.getY();

                    repaint();
                }
            }
        });
        
        
        
     // Agregar un MouseWheelListener para el zoom con la rueda del mouse
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    // Zoom in
                    setZoom(getZoom() * 1.1);
                } else {
                    // Zoom out
                    setZoom(getZoom() / 1.1);
                }
                repaint();
            }
        });
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        repaint();
    }

    public double getZoom() {
        return zoom;
    }

    private int escalarCoordenada(double valor, double rangoMinEntrada, double rangoMaxEntrada, int rangoMinSalida, int rangoMaxSalida) {
        return (int) (((valor - rangoMinEntrada) * (rangoMaxSalida - rangoMinSalida)) / (rangoMaxEntrada - rangoMinEntrada) + rangoMinSalida);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setTransform(new AffineTransform());

        g2d.translate(panX, panY);

        for (Edge borde : bordes) {
            Nodo nodoFuente = borde.getNodoFuente();
            Nodo nodoDestino = borde.getNodoDestino();
            if (nodoFuente != null && nodoDestino != null) {
                int x1 = escalarCoordenada(nodoFuente.getX(), xv, xv2, 0, getWidth());
                int y1 = escalarCoordenada(nodoFuente.getY(), yv, yv2, 0, getHeight());
                int x2 = escalarCoordenada(nodoDestino.getX(), xv, xv2, 0, getWidth());
                int y2 = escalarCoordenada(nodoDestino.getY(), yv, yv2, 0, getHeight());

                x1 = (int) (x1 * zoom);
                y1 = (int) (y1 * zoom);
                x2 = (int) (x2 * zoom);
                y2 = (int) (y2 * zoom);

                g2d.setColor(Color.red);
                g2d.drawLine(x1, y1, x2, y2);
                g2d.setColor(Color.black);
            }
        }

        for (Nodo nodo : nodos) {
            nodo.dibujar(g2d, getWidth(), getHeight(), zoom);
        }
    }
    
    // Agregar un método para encontrar el nodo más cercano
    public Nodo encontrarNodoMasCercano(double mouseX, double mouseY) {
        Nodo nodoMasCercano = null;
        //double distanciaMasCercana = Double.MAX_VALUE;

        for (Nodo nodo : nodos) {
            //double distancia = Math.sqrt(Math.pow(nodo.getX() - mouseX, 2) + Math.pow(nodo.getY() - mouseY, 2));
            //if (distancia < distanciaMasCercana) {
                //distanciaMasCercana = distancia;
        	if(escalarCoordenada( nodo.getX(), xv, xv2, 0, getWidth()) - mouseX < 10 && escalarCoordenada(nodo.getY(), yv, yv2, 0, getHeight()) - mouseY < 10 && escalarCoordenada( nodo.getX(), xv, xv2, 0, getWidth()) - mouseX > 0 && escalarCoordenada(nodo.getY(), yv, yv2, 0, getHeight()) - mouseY > 0) {
                System.out.println(escalarCoordenada(nodo.getX(), xv, xv2, 0, getWidth()) - mouseX);
                System.out.println(escalarCoordenada(nodo.getY(), yv, yv2, 0, getHeight()) - mouseY);
        		nodoMasCercano = nodo;
        		System.out.println(nodoMasCercano);
        		return nodoMasCercano;
            }
        }
        //System.out.println(nodoMasCercano);

        return null;
    }
    
    private double calcularDistanciaEntreNodos(Nodo nodo1, Nodo nodo2) {
        double deltaX = nodo2.getX() - nodo1.getX();
        double deltaY = nodo2.getY() - nodo1.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

 
}


