package Pack;
import java.awt.Color;
import java.awt.Graphics;

class Nodo {
    private double x;
    private double y;
    private String osmid;
    private Color color = Color.blue; // Color original
    private int tamañoOriginal = 4;  // Tamaño original
    private boolean marcado = false;  // Indica si el nodo está marcado

    public Nodo(double x, double y, String osmid) {
        this.x = x;
        this.y = y;
        this.osmid = osmid;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTamañoOriginal() {
        return tamañoOriginal;
    }

    public void setTamañoOriginal(int tamañoOriginal) {
        this.tamañoOriginal = tamañoOriginal;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getOsmid() {
        return osmid;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
        
     // Cambiar el tamaño del nodo cuando se marca
        if (marcado) {
            tamañoOriginal += 10; // Aumentar el tamaño original (por ejemplo, x3)
        } else {
            tamañoOriginal = 4; // Restaurar el tamaño original
        }
    }

    public void dibujar(Graphics g, int panelWidth, int panelHeight, double zoom) {
        g.setColor(color);
        int scaledX = escalarCoordenada(x, -71.7, -71.1, 0, panelWidth);
        int scaledY = escalarCoordenada(y, -30.5, -29.9, 0, panelHeight);
        scaledX = (int) (scaledX * zoom);
        scaledY = (int) (scaledY * zoom);
        g.fillOval(scaledX - 1, scaledY - 1, tamañoOriginal, tamañoOriginal);
        g.setColor(Color.black);
    }

    private int escalarCoordenada(double valor, double rangoMinEntrada, double rangoMaxEntrada, int rangoMinSalida, int rangoMaxSalida) {
        return (int) (((valor - rangoMinEntrada) * (rangoMaxSalida - rangoMinSalida)) / (rangoMaxEntrada - rangoMinEntrada) + rangoMinSalida);
    }
}
