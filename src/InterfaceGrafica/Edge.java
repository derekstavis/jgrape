/*	
	Objeto que irá fazer o desenho da Aresta. Basicamente, extende a classe 
	JComponent da AWT, e pode então ser considerado um Componente.
 
	Utiliza-se da classe QuadCurve2D para desenhar a aresta comum e da Arc2D 
	para desenhar o loop.
*/
package InterfaceGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Arc2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Container;
import java.awt.RenderingHints;
import InterfaceGrafica.Vertex;

public class Edge extends JComponent {
	// "Ponteiro" para o QuadCurve2D
	private QuadCurve2D thisEdge;
	// "Ponteiro" para o Arc2D
	private Arc2D thisLoop;
	
	// Armazenará a cor com a qual a linha será desenhada
	private Color theColor;
	// Armazenará a label da aresta, qual representa o custo
	private int theLabel;
	// Armazenará a fonte com a qual a theLabel será desenhada
	private Font theFont;
	// Armazena os IDs dos vértices de onde sai e para onde vai
	private int to ,from;
	// Variáveis de uso geral durante o programa. Auto explicáveis ;)
	private int x1,x2,y1,y2, pontoControleX, pontoControleY;
	
	private Boolean isLoop=false;
	
	public Edge(Vertex firstVertex, Vertex secondVertex, int label) {
		super();
		
		x1=firstVertex.getX();
		y1=firstVertex.getY();
		
		x2=secondVertex.getX();
		y2=secondVertex.getY();	
		
		pontoControleX=((x1+x2));
		pontoControleY=((y1+y2));
		
		from=firstVertex.getTheID();
		to=secondVertex.getTheID();
		
		theLabel = label;
		theFont =  new Font("Comic Sans", Font.BOLD, 16);
		
		if (from!=to) {
			System.out.println("[Edge.java] thisEdge é uma QuadCurve2D");
			
			thisEdge=new QuadCurve2D.Double(x1+30,
											y1+30,
											(pontoControleX/2),
											(pontoControleY/2),
											x2+30,
											y2+30);
			
			isLoop=false;
		} else {
			System.out.println("[Edge.java] thisLoop é um Arc2D");
			thisLoop=new Arc2D.Double(x1-10, y1-12, 40, 40, 0, 270, Arc2D.OPEN) ;			
			isLoop=true;
		}
		
		theColor=new Color(255,215,0);
		
		//this.addMouseMotionListener(this);
		repaint();
	}
	
	public Edge() {
		super();
		System.out.println("[Edge.java] Construído Edge para evento de mouseMove");
		
		theLabel = -1;
		theFont =  new Font("Comic Sans", Font.BOLD, 16);		
		
		theColor=new Color(255,215,0);
		repaint();
	}
	
	public void setPosition(Vertex firstVertex, int x, int y) {
		System.out.println("[Edge.java] Setando posição:\nx="+x+", y="+y);
		if (!isLoop) {
			x1=firstVertex.getX();
			y1=firstVertex.getY();
			
			x2=x;
			y2=y;
			
			pontoControleX=((x1+x2));
			pontoControleY=((y1+y2));
			
			from=firstVertex.getTheID();
			to=-1;
			
			thisEdge=new QuadCurve2D.Double(x1+30,
											y1+30,
											(((x1+x2))/2),
											(((y1+y2))/2),
											x2,
											y2);
			
			setVisible(true);
			
			repaint();
		} else {
			System.out.println("[Edge.java] Não é possível setar a posição de um Arc2D!");
		}

		
	}
	
	public void setTheIDS (int from, int to){
		this.to = to;
		this.from = from;
	}
	
	public int getFrom() {
		return from;
	}
	
	public int getTo() {
		return to;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("[Edge.java] Edge.paintComponent()");
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (from!=to) {
			System.out.println("[Edge.java] QuadCurve2D Desenhada!");
			g2d.setPaint(new Color(255,70,0));
			g2d.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(thisEdge);
			g2d.setPaint(theColor);
			g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(thisEdge);
		} else {
			System.out.println("[Edge.java] Arc2D Desenhado!");
			g2d.setPaint(new Color(255,70,0));
			g2d.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(thisLoop);
			g2d.setPaint(theColor);
			g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(thisLoop);
		}

		
		g2d.setFont(theFont);
		g2d.setPaint(Color.WHITE);
		
		if (theLabel>0) {
			if (from!=to) {
				g2d.drawString(Integer.toString(theLabel),
							   Math.round(pontoControleX/2),
							   Math.round(pontoControleY/2));	
			} else {
				g2d.drawString(Integer.toString(theLabel),
							   Math.round(pontoControleX/2),
							   Math.round(pontoControleY/2));
				
			}			
		}
		
		super.paintComponent(g2d);
		g2d.dispose();
	}
}
