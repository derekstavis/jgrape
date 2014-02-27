import java.awt.*;
import java.awt.Color;
import javax.swing.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import InterfaceGrafica.GraphPanel;

public class JGrape extends JFrame {
	private JFrame				mainWindow;
	private GraphPanel			graphDrawingArea;
	private Toolbar				theToolbar;
	
	private Container			thisPane;
	private FileOutputStream	fos = null;
	private ObjectOutputStream	out = null;
	
	private JFileChooser		fc;
	
	private String				theLogo="LogoProvisoria.png";
	
	public JGrape() {
        super("JGrape");
		thisPane = getContentPane();
		
		setLayout(null);
		setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		graphDrawingArea=new GraphPanel();
		graphDrawingArea.setVisible(true);
		thisPane.add(graphDrawingArea);
		
		theToolbar=new Toolbar();
		theToolbar.setVisible(true);
		thisPane.add(theToolbar);
		
        setVisible(true);
	}
	
	public static void main(String args[]) {
		new JGrape();
	}
	
	class Toolbar extends JPanel {
		private String		openLabel, saveLabel, clearLabel;
		private String		dijkstraLabel, depthLabel, widthLabel;
		private String		centerLabel, goodManLabel, fleuryLabel;
		
		private JButton		openFile, saveFile, clearAll;
		private JButton		dijkstra, depth, width;
		private JButton		center, goodMan, fleury;
		
		public Toolbar() {	
			super(null);
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception e) {
				System.out.println("[JGrape.java] Erro ao setar LookAndFeel nativo: \n" + e);
			}
			
			openLabel="Abrir";
			saveLabel="Salvar";
			clearLabel="Limpar";

			dijkstraLabel="Dijkstra";
			depthLabel="Profundidade";
			widthLabel="Largura";
			centerLabel="Centro";
			goodManLabel="Componentes Conexas";
			fleuryLabel="Fleury";

			
			int marginLeft=5;
			
			setSize(800, 100);
			
			setBackground(new Color(15,40,90));
			setVisible(true);
			setLocation(0, 500);
			
			openFile=new JButton(((Icon)new ImageIcon("open.png")));
			saveFile=new JButton(((Icon)new ImageIcon("save.png"))); 
			clearAll=new JButton(((Icon)new ImageIcon("clear.png")));

			dijkstra=new JButton(dijkstraLabel);
			depth=new JButton(depthLabel);
			width=new JButton(widthLabel);
			center=new JButton(centerLabel);
			center.setEnabled(false);
			goodMan=new JButton(goodManLabel);
			fleury=new JButton(fleuryLabel);
			fleury.setEnabled(false);
			
			openFile.setLabel(openLabel);
			openFile.setSize(130, 65);
			openFile.setFocusPainted(false);
			openFile.setLocation(marginLeft, 5);
			openFile.setVisible(true);
			openFile.addMouseListener(new handleOpenFile());
			
			saveFile.setLabel(saveLabel);
			saveFile.setSize(130, 65);
			saveFile.setFocusPainted(false);
			saveFile.setLocation(130+marginLeft, 5);
			saveFile.setVisible(true);
			saveFile.addMouseListener(new handleSaveFile());
			
			clearAll.setLabel(clearLabel);
			clearAll.setSize(140, 65);
			clearAll.setFocusPainted(false);
			clearAll.setLocation(260+marginLeft, 5);
			clearAll.setVisible(true);
			clearAll.addMouseListener(new handleClearAll());		
			
			int buttonSizeX=130;
			int buttonSizeY=30;
			
			int buttonRowSpacing=2;
			
			int buttonsMarginX=410;
			int buttonsMarginY=5;
			int shift=0;
			
			depth.setSize(buttonSizeX, buttonSizeY);
			depth.setFocusPainted(false);
			depth.setLocation(buttonsMarginX+shift, buttonsMarginY);
			depth.setVisible(true);
			depth.addMouseListener(new handleDepth());
			
			shift+=buttonSizeX;
			
			width.setSize(buttonSizeX, buttonSizeY);
			width.setFocusPainted(false);
			width.setLocation(buttonsMarginX+shift, buttonsMarginY);
			width.setVisible(true);
			width.addMouseListener(new handleWidth());
			
			shift+=buttonSizeX;
			
			center.setSize(buttonSizeX, buttonSizeY);
			center.setFocusPainted(false);
			center.setLocation(buttonsMarginX+shift, buttonsMarginY);
			center.setVisible(true);
			//center.addMouseListener(new handleCenter());
			
			buttonsMarginY+=buttonSizeY+buttonRowSpacing;
			shift=0;
			
			dijkstra.setSize(buttonSizeX, buttonSizeY);
			dijkstra.setFocusPainted(false);
			dijkstra.setLocation(buttonsMarginX+shift, buttonsMarginY);
			dijkstra.setVisible(true);
			dijkstra.addMouseListener(new handleDijkstra());
			
			shift+=buttonSizeX;
			
			goodMan.setSize(buttonSizeX, buttonSizeY);
			goodMan.setFocusPainted(false);
			goodMan.setLocation(buttonsMarginX+shift, buttonsMarginY);
			goodMan.setVisible(true);
			goodMan.addMouseListener(new handleGoodman());
			
			shift+=buttonSizeX;
			
			fleury.setSize(buttonSizeX, buttonSizeY);
			fleury.setFocusPainted(false);
			fleury.setLocation(buttonsMarginX+shift, buttonsMarginY);
			fleury.setVisible(true);
			//fleury.addMouseListener(new handleFleury());
			
			add(openFile);
			add(saveFile);
			add(clearAll);
			add(dijkstra);
			add(depth);
			add(width);
			add(center);
			add(goodMan);
			add(fleury);
			
			setVisible(true);
			
			repaint();
		}
		
		class handleOpenFile implements MouseListener  {
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleOpenFile::mouseClicked()");
				if(openFile()) {
					System.out.println("Arquivo Aberto!");
				} else {
					System.out.println("Não rolou!");
				}
				
			}
			
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleSaveFile implements MouseListener {
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleSaveFile::mouseClicked()");
				if (saveFile()) {
					System.out.println("ROLOOOOOOOU FILHO DA PUTA");
				} else {
					System.out.println("Não ROLOOOOOOOU FILHO DA PUTA");
				}
			}
			
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleClearAll implements MouseListener {
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleClearAll::mouseClicked()");
				thisPane.remove(graphDrawingArea);
				graphDrawingArea=null;
				graphDrawingArea=new GraphPanel();
				thisPane.add(graphDrawingArea);
				thisPane.repaint();
			}
			
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleDepth implements MouseListener { 
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleDepth::mouseClicked()");
				graphDrawingArea.setConsoleMessage("Percurso em Profundidade em Execução");
				ArrayList theIDS=new ArrayList();
				theIDS = graphDrawingArea.getVertexAL();
				graphDrawingArea.doAlgorithm(GraphPanel.TRAVERSEDEPTH);
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleWidth implements MouseListener { 
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleWidth::mouseClicked()");
				graphDrawingArea.setConsoleMessage("Pesquisa em Largura em Execução");
				ArrayList theIDS=new ArrayList();
				theIDS = graphDrawingArea.getVertexAL();
				
				graphDrawingArea.doAlgorithm(GraphPanel.TRAVERSEWIDTH);
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleCenter implements MouseListener { 
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleCenter::mouseClicked()");
				graphDrawingArea.doCenter();
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleDijkstra implements MouseListener { 
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleDijkstra::mouseClicked()");
				
				graphDrawingArea.doAlgorithm(GraphPanel.DIJKSTRA);
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleGoodman implements MouseListener { 
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleHighTeller::mouseClicked()");
				String theMessage = "Número de componentes conexas: "+Integer.toString(graphDrawingArea.theGoodMan());
				JOptionPane.showMessageDialog(null, theMessage, "Algoritmo de Goodman", JOptionPane.WARNING_MESSAGE, null);
				graphDrawingArea.setConsoleMessage(theMessage);
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
		
		class handleFleury implements MouseListener {
			public void mouseClicked (MouseEvent me) {
				System.out.println("[JGrape.java] handleFleury::mouseClicked()");
			}
			public void mouseEntered (MouseEvent me) {} 
			public void mousePressed (MouseEvent me) {}
			public void mouseReleased (MouseEvent me) {}  
			public void mouseExited (MouseEvent me) {} 
		}
	}	
	
	private Boolean saveFile() {
		String	fileName;
		fc = new JFileChooser();
		
		if (graphDrawingArea.wasModified()) {
			int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileName=fc.getSelectedFile().getAbsolutePath();
				fileName+=".grape";
				System.out.println("[JGrape.java] Arquivo escolhido: "+fileName);
				try {
					//ObjectOutputStream.reset();
					fos = new FileOutputStream(fileName);
					out = new ObjectOutputStream(fos);
					
					out.writeObject(graphDrawingArea);
					out.close();
					graphDrawingArea.setConsoleMessage("Arquivo salvo em: "+fileName);
					return true;
					
				} catch(IOException ex) {
					graphDrawingArea.setConsoleMessage("Erro ao salvar arquivo.");
					ex.printStackTrace();
					return false;
				} 
            } else {
                System.out.print("[JGrape.java] Operação cancelada pelo usuário.");
				return false;
            }
			
		} else {
			System.out.println("[JGrape.java] Tentativa de salvar arquivo em branco. FAIL!");
			graphDrawingArea.setConsoleMessage("Você deve desenhar algo antes de tentar salvar ;)");
			return false;
		}
	}
	
	private Boolean openFile() {
		if (graphDrawingArea.wasModified()) {
			switch (JOptionPane.showConfirmDialog(this, "O projeto atual foi alterado.\nAbrir um novo arquivo fará com que o arquivo atual seja perdido.\nDeseja continuar?", "Atenção!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, ((Icon)new ImageIcon(theLogo)))) {
				case JOptionPane.YES_OPTION :
					String filename = "";
					JFileChooser chooser = new JFileChooser();
					int itsOkOrNot;
					itsOkOrNot = chooser.showOpenDialog(this);
					if(itsOkOrNot == JFileChooser.APPROVE_OPTION) {
						filename = chooser.getSelectedFile().getAbsolutePath();//+chooser.getSelectedFile().getName();
						System.out.println("[JGrape.java] Arquivo selecionado: "+filename);
						FileInputStream fis = null;
						ObjectInputStream in = null;
						try {
							fis = new FileInputStream(filename);
							in = new ObjectInputStream(fis);
							thisPane.remove(graphDrawingArea);
							graphDrawingArea=((GraphPanel)in.readObject());
							thisPane.add(graphDrawingArea);
							thisPane.repaint();
							graphDrawingArea.repaint();
							repaint();
							
							in.close();
							graphDrawingArea.setConsoleMessage("Arquivo aberto: "+filename);
							return true;
						} catch(IOException ex) {
							ex.printStackTrace();
							graphDrawingArea.setConsoleMessage("Erro ao abrir arquivo.");
							return false;
						} catch(ClassNotFoundException ex) {
							ex.printStackTrace();
							graphDrawingArea.setConsoleMessage("Erro ao abrir arquivo.");
							return false;
						}
					}
					
				case JOptionPane.NO_OPTION:
					//nothing
					return false;
					
				default:
					//nothing
					return false;
			}
		} else {
			String filename = "";
			JFileChooser chooser = new JFileChooser();
			int itsOkOrNot;
			itsOkOrNot = chooser.showOpenDialog(this);
			if(itsOkOrNot == JFileChooser.APPROVE_OPTION) {
				filename = chooser.getSelectedFile().getAbsolutePath();
				FileInputStream fis = null;
				ObjectInputStream in = null;
				try {
					fis = new FileInputStream(filename);
					in = new ObjectInputStream(fis);
					thisPane.remove(graphDrawingArea);
					graphDrawingArea=((GraphPanel)in.readObject());
					thisPane.add(graphDrawingArea);
					thisPane.repaint();
					graphDrawingArea.repaint();
					repaint();
					
					in.close();
					return true;
				} catch(IOException ex) {
					ex.printStackTrace();
					return false;
				} catch(ClassNotFoundException ex) {
					ex.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
}