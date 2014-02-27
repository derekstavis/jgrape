package InterfaceGrafica;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;

public class Vertex extends JButton {
	private int			x, y;
	private String		label;
	private int			id;
	private Font		theFont;
	private int			numberOfConnections;
	private Boolean		highlighted;
	
	
	public Vertex(String text) {
		super();
		highlighted=false;
		x=y=0;
		label=text;
		
		theFont =  new Font("Comic Sans", Font.BOLD, 16);

		setPreferredSize(new Dimension(50, 50));
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		
	}
	
	public void highlight() {
		highlighted=true;
		repaint();
	}
	
	public void shutTheFuckUp() {
		highlighted=false;
		repaint();
	}
	
	public void addConnection() {
		numberOfConnections++;
	}
	
	public void removeConnection() {
		if (numberOfConnections>0) {
			numberOfConnections--;
		} else {
			System.out.println("[Vertex.java] Numero de conexões já é 0. Quer remover oquê?");
		}

	}
	
	public int getNumberOfConnections() {
		return numberOfConnections;
	}
	
	public void setTheID(int number) {
		id=number;
		System.out.println("[Vertex.java] setID("+id+");");
	}
	
	public int getTheID() {
		System.out.println("[Vertex.java] getID()="+id);
		return this.id;
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		ImageIcon img;
		if (highlighted) {
			img=new ImageIcon("vertice_highlight.png");
		} else {
			img=new ImageIcon("vertice.png");
		}
		
		Image theImage = img.getImage();
		
		System.out.println("[Vertex.java] paintComponent(Graphics g);");
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(theImage, 0, 0, this);
		
		g2d.setFont(theFont);
		g2d.setColor(new Color(40,40,40));
		g2d.drawString(label,20,33);
		
		super.paintComponent(g2d);
		
		g2d.dispose();
	}
	
}