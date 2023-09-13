package Pack;

import java.util.LinkedList;

public class Nodo {
	private double cordenadaX, cordenandaY;
	private String osmid;
	private LinkedList<Nodo> conexiones;
	
	public Nodo(double cordenadaX, double cordenandaY, String osmid) {
		super();
		this.cordenadaX = cordenadaX;
		this.cordenandaY = cordenandaY;
		this.osmid = osmid;
	}

	public double getCordenadaX() {
		return cordenadaX;
	}

	public double getCordenandaY() {
		return cordenandaY;
	}

	public LinkedList<Nodo> getConexiones() {
		return conexiones;
	}
	

	public String getOsmid() {
		return osmid;
	}
	
		public void Conectar(Nodo n) {
		conexiones.add(n);
	}
		
}
