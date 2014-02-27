/*	
	GraphPanel.java
	Interface gráfica eye-candy para a operação de grafos.
	Trabalho desenvolvido para a matéria de 
	Algoritmos e Estruturas de Dados 2, do curso de Ciência da Computação
	da Unioeste, campus Paraná
 
	@author: Derek Willian Stavis
*/
package InterfaceGrafica;

import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import InterfaceGrafica.Edge;
import InterfaceGrafica.Vertex;
import Graph.Graph;

public class GraphPanel extends JPanel implements MouseListener, Serializable {
	private Container			content = this;
	private ArrayList			Vertices = new ArrayList();
	private ArrayList			Arestas = new ArrayList();
	private Boolean				firstSelected=false;
	private Boolean				inSelectionMode=false;
	private int					selectedVertexID=0;
	private Edge				followPointer = new Edge();
	private Vertex				firstVertex, secondVertex;
	private Graph				theGraph;
	private vertexHandler		theVertexHandler = new vertexHandler();
	
	public final console		Console=new console(800,20);
	
	public static final int		DIJKSTRA=1;
	public static final int		DEPTHSEACH=2;
	public static final int		WIDTHSEARCH=3;
	public static final int		CENTERSEARCH=4;
	public static final int		TRAVERSEWIDTH=5;
	public static final int		TRAVERSEDEPTH=6;
	public static final int		GOODMAN=7;
	private int					selectedAlgorithm;
	private final int			blinkTimes=6;
	
	public GraphPanel() {
		super();
		
		addMouseListener(this);
		setSize(800,500);
		setBackground(new Color(27,51,101));
		setLayout(null);
		setVisible(true);
		repaint();
		
		theGraph=new Graph();
		
		Console.setLocation(0,480);
		add(Console);
		
	}
	
	class animationHandler implements ActionListener, Serializable {
		private int			actualAnimationVertexID;
		private Vector<Integer>		idList;//=new Vector<Integer>();
		private Timer		theTimer;
		private Boolean		isBlink;
		
		public animationHandler() {
			isBlink=false;
			actualAnimationVertexID = 0;
		}
		
		public void setList(Vector<Integer> theList) {
			idList=theList;
			
			actualAnimationVertexID = 0;
			
			if (!isBlink) {
				startSequentialAnimation();
			} else {
				startBlinkAnimation();
			}
		}
		
		public void setBlinkAnimation(Boolean status) {
			isBlink=status;
		}
		
		public void actionPerformed(ActionEvent evt) {
			System.out.println("[GraphPanel.java] animationHandler::actionPerformed()\nactualAnimationVertexID="+actualAnimationVertexID);
			if (!isBlink) {
				if (actualAnimationVertexID<idList.size()+1) {
					if (actualAnimationVertexID>0) 
						getVertexById(idList.get(actualAnimationVertexID-1)).shutTheFuckUp();
					if (actualAnimationVertexID<idList.size())
						getVertexById(idList.get(actualAnimationVertexID)).highlight();
					actualAnimationVertexID++;
				} else {
					theTimer.stop();
					setConsoleMessage("JGrape. Aguardando comando.");
					actualAnimationVertexID = 0;
				}
				
			} else {
				if (actualAnimationVertexID<blinkTimes) {
					if ((actualAnimationVertexID%2)==1) {
						for (int i=0; i<idList.size(); i++) {
							((Vertex)getVertexById(idList.get(i))).shutTheFuckUp();
						}
					} else {
						for (int i=0; i<idList.size(); i++) {
							((Vertex)getVertexById(idList.get(i))).highlight();
						}
					}
					actualAnimationVertexID++;
				} else {
					theTimer.stop();
					setConsoleMessage("JGrape. Aguardando comando.");
					actualAnimationVertexID = 0;
				}

				
			}

		}
		
		private void startSequentialAnimation() {
			System.out.println("[GraphPanel.java] Instanciado objeto Timer");
			theTimer = new Timer(1000, this);
			theTimer.start();
		}
		
		private void startBlinkAnimation() {
			System.out.println("[GraphPanel.java] Instanciado objeto Timer");
			theTimer = new Timer(500, this);
			theTimer.start();
		}
		
	}
	
	class vertexHandler implements MouseListener, MouseMotionListener, Serializable {	
		
		public Boolean thereIsAnExistingEdge() {
			if (firstVertex!=secondVertex) {
				for (int i=0; i<Arestas.size(); i++) {
					if (((Edge)Arestas.get(i)).getFrom()==firstVertex.getTheID() && 
						((Edge)Arestas.get(i)).getTo()==secondVertex.getTheID()) {
						return true;
					}
				}
				
			}
			return false;
		}
		
		public void mouseMoved(MouseEvent me) {
			if (firstSelected) {
				System.out.println("[GraphPanel.java] vertexHandler.mouseMoved()");
				
				followPointer.setPosition(firstVertex, me.getX(), me.getY());
				
				followPointer.setTheIDS(firstVertex.getTheID(), -1);
				
				followPointer.setBounds(0,0,800,480);
				
				content.add(followPointer);
				
				repaint();
			}
		}
		
		public void mouseDragged(MouseEvent me) {}		
		
		public void mouseClicked (MouseEvent me) {
			switch(me.getButton()) {
				case 1:
					System.out.println("[GraphPanel.java] vertexHandler::mouseClicked()");
					
					if (!inSelectionMode) {
						if (!firstSelected) {
							firstVertex=((Vertex)me.getComponent());
							content.addMouseMotionListener(theVertexHandler);
							firstSelected=true;
						} else {
							secondVertex=((Vertex)me.getComponent());
							if (thereIsAnExistingEdge()) {
								System.out.println("[GraphPanel.java] Esta aresta já ecziste\n");
								followPointer.setVisible(false);
								repaint();
								content.removeMouseMotionListener(theVertexHandler);
								content.remove(followPointer);
								firstSelected=false;
							} else {
								if (firstVertex!=secondVertex) {
									int thatFuckingLabel = -1;
									
									thatFuckingLabel = Integer.parseInt(JOptionPane.showInputDialog("Qual o custo do percurso desta Aresta?"));
									
									while (!(thatFuckingLabel>0)) {
										thatFuckingLabel = Integer.parseInt(JOptionPane.showInputDialog("Custo nao pode ser menor ou igual a 0.\nQual o custo do percutso desta Aresta?"));
									}
									
									if (thatFuckingLabel==-1) {
										thatFuckingLabel=1;
									}
									
									Edge thisEdge = new Edge(firstVertex, secondVertex, thatFuckingLabel);
									Edge thisEdge2 = new Edge(secondVertex, firstVertex, thatFuckingLabel);
									
									thisEdge.setBounds(0,0,800,480);
									
									Arestas.add(thisEdge);
									Arestas.add(thisEdge2);
									content.add(thisEdge);
									
									firstVertex.addConnection();
									secondVertex.addConnection();
									
									theGraph.insertEdge(firstVertex.getTheID(), secondVertex.getTheID(), ((float)thatFuckingLabel));
									theGraph.insertEdge(secondVertex.getTheID(), firstVertex.getTheID(), ((float)thatFuckingLabel));
									
									followPointer.setVisible(false);
									repaint();
									content.removeMouseMotionListener(theVertexHandler);
									content.remove(followPointer);
									
									System.out.println("[GraphPanel.java] Finalizado o desenho da Aresta!\n");
									firstSelected=false;
									firstVertex=secondVertex=null;
									
									repaint();
									repaint();
								} else {
									System.out.println("[GraphPanel.java] Criação de laço desativado");
								}
								
							}
						}
					} else {
						selectVertex(((Vertex)me.getComponent()).getTheID());
					}
					
					break;
					
				case 3:
					int whatTheFuck = JOptionPane.showConfirmDialog(null, 
																	"Esta ação removerá o vértices e todas as arestas conectadas. Tem certeza que deseja prosseguir?", 
																	"Remoção de Aresta", JOptionPane.YES_NO_OPTION);
					
					if (whatTheFuck==JOptionPane.YES_OPTION) {
						content.remove(((Vertex)me.getComponent()));
						theGraph.removeVertex(((Vertex)me.getComponent()).getTheID());
						for (int i=0; i<Arestas.size(); i++) {
							System.out.println("[GraphPanel.java] Arestas.get(i)).getTo()="+((Edge)Arestas.get(i)).getTo());
							System.out.println("[GraphPanel.java] Arestas.get(i)).getFrom()="+((Edge)Arestas.get(i)).getFrom());
							if ((((Edge)Arestas.get(i)).getTo()==((Vertex)me.getComponent()).getTheID()) || 
								(((Edge)Arestas.get(i)).getFrom()==((Vertex)me.getComponent()).getTheID())) {
								content.remove(((Edge)Arestas.get(i)));
								((Vertex)Vertices.get(((Edge)Arestas.get(i)).getTo())).removeConnection();
							}
							while (((Vertex)me.getComponent()).getNumberOfConnections()!=0) {
								((Vertex)me.getComponent()).removeConnection();
							}
						}
						
						repaint();
					} else {
						System.out.println("[GraphPanel.java] Usuário cagão!!");
					}
					
					break;
			}
		}
		
		public void mouseEntered (MouseEvent me) {
			if (firstSelected) {
				followPointer.setPosition(firstVertex, 
										  ((Vertex)me.getComponent()).getX()+30, 
										  ((Vertex)me.getComponent()).getY()+30);
				
				content.add(followPointer);
				
				repaint();
				
			}
		} 
		
		public void mousePressed (MouseEvent me) {}
		public void mouseReleased (MouseEvent me) {}  
		public void mouseExited (MouseEvent me) {}
	};
	
	class console extends JPanel implements Serializable {
		private String	message="JGrape pronto!";
		private JLabel	theLabel;
		private int		width, height;
		
		public console(int width, int height) {
			super(null);
			setSize(width,height);
			this.width=width;
			this.height=height;
			setBackground(new Color(37,61,111));
			theLabel=new JLabel(message);
			theLabel.setSize(width-20, height);
			theLabel.setLocation(10,0);
			theLabel.setForeground(Color.white);
			setVisible(true);
			add(theLabel);
			repaint();
			repaint();
		}
		
		public void setMessage(String theMessage) {
			setBackground(new Color(37,61,111));
			message=theMessage;
			theLabel.setText(message);
			repaint();
			repaint();
		}
	}
	
	public void setConsoleMessage(String theMsg) {
		Console.setMessage(theMsg);
	}
	
	public void animateVertexList(final Vector<Integer> theListOfIDs, Boolean blinkIt) {
		setConsoleMessage("Em execução...");
		animationHandler theAnimation = new animationHandler();
		theAnimation.setBlinkAnimation(blinkIt);
		theAnimation.setList(theListOfIDs);
		theAnimation=null;
	}
	
	public ArrayList getVertexAL() {
		ArrayList temp = new ArrayList();
		for (int i=0; i<Vertices.size(); i++) {
			temp.add(i, ((Vertex)Vertices.get(i)).getTheID());
		}
		
		return (temp);
	}
	
	public Vertex getVertexById(int id) {
		for (int i=0; i<Vertices.size(); i++) {
			if (((Vertex)Vertices.get(i)).getTheID()==id) {
				System.out.println("[GraphPanel.java] getElementById("+i+")="+((Vertex)Vertices.get(i)).getTheID());
				return(((Vertex)Vertices.get(i)));
			}
		}
		return null;
	}
	
	public Boolean wasModified() {
		if ((Vertices.size()>=1) || (Arestas.size()>=1)) {
			return true;
		} else {
			return false;
		}

	}
	
	public void mouseClicked (MouseEvent me) {
		if ((me.getY()<460) && (me.getY()>50) && (me.getX()>50) && (me.getX()<750)) {
			switch (me.getButton()) {
				case MouseEvent.BUTTON1:
					String thisLabel = Integer.toString(Vertices.size()+1);
					System.out.println("[GraphPanel.java] Evento mouseClicked. Posição:\nx="+me.getX()+"\ny="+me.getY());
					
					Vertex thisVertex=new Vertex(thisLabel);
					thisVertex.setBounds(me.getX()-25, me.getY()-25 , 50, 50);
					System.out.println("[GraphPanel.java] Vertices.size()="+Vertices.size());
					
					thisVertex.setTheID(theGraph.insertVertex());
					
					thisVertex.addMouseListener(theVertexHandler);
					
					Vertices.add(thisVertex);
					
					content.add(thisVertex);
					content.setVisible(true);
					
					repaint();
					break;
					
				case MouseEvent.BUTTON3:
					if (firstSelected) {
						System.out.println("[GraphPanel.java] Cancelando ação de criar nova aresta...");
						content.remove(followPointer);
						followPointer.removeMouseMotionListener(theVertexHandler);
						//followPointer=null;
						firstSelected=false;
						repaint();
					}
					break;
			}
		}
	}
		
	public void mouseEntered (MouseEvent me) {}
	public void mousePressed (MouseEvent me) {}
	public void mouseReleased (MouseEvent me) {}  
	public void mouseExited (MouseEvent me) {} 
	
	
/* Métodos que executam os algoritmos de percurso */
	
	// Chamando quando clicado.
	public void selectVertex(int theId) {
		selectedVertexID=((Integer)theId);
		inSelectionMode=false;
		
		switch (selectedAlgorithm) {
			case DIJKSTRA:
				doDijkstra();
				break;
				
			case DEPTHSEACH:
				searchDepth();
				break;
				
			case WIDTHSEARCH:
				searchWidth();
				break;
				
			case TRAVERSEDEPTH:
				traverseDepth();
				break;
			
			case TRAVERSEWIDTH:
				traverseWidth();
				break;

			default:
				break;
		}
	}
	
	public void doAlgorithm(int theAlgorithm) {
		JOptionPane.showMessageDialog(this, "Selecione um vértice existente na área de desenho.", "Selecionar vértice", JOptionPane.WARNING_MESSAGE, null);
		inSelectionMode=true;
		setConsoleMessage("Selecione um vértice existente na área de desenho.");
		selectedAlgorithm=theAlgorithm;
	}
	
	public void doDijkstra() {
		if (theGraph.goodMan()==1) {
			int temporarioDoInferno=-1;
			while (temporarioDoInferno<0) {
				temporarioDoInferno=Integer.parseInt(JOptionPane.showInputDialog("Digite o label do nó:"))-1;
			}
			Console.setMessage("Caminho atual: "+theGraph.Dijkstra(temporarioDoInferno, selectedVertexID));
			animateVertexList(theGraph.Dijkstra(temporarioDoInferno, selectedVertexID), false);
		} else {
			Console.setMessage("Grafo desconexo!");
		}

	}
	
	public void searchWidth() {
		animateVertexList(theGraph.searchWidth(selectedVertexID), false);
	}
	
	public void traverseWidth() {
		animateVertexList(theGraph.searchWidth(selectedVertexID), false);
	}
	
	public void searchDepth() {
		animateVertexList(theGraph.searchDepth(selectedVertexID), false);
	}
	
	public void traverseDepth() {
		animateVertexList(theGraph.searchDepth(selectedVertexID), false);
	}
	
	public int theGoodMan() {
		return(theGraph.goodMan());
	}
	
	public void doCenter() {
		try {
			animateVertexList(theGraph.getCenter(), true);
		} catch (java.lang.CloneNotSupportedException e) {
			//nothing
		}
	}
}