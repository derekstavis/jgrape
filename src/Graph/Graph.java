package Graph;

import Graph.Aresta;
import Graph.Vertice;

import java.util.Vector;
import java.util.Vector;
import java.io.Serializable;


public class Graph implements Serializable {
	public float [][]mat; 
	Vector<Vertice> thisVert = new  Vector <Vertice>();
	Vector<Aresta> thisArest = new Vector <Aresta>();
	int cont=0;
	
	public Graph (){
		
	}
	
	public Graph (Graph G){
		this.mat = new float [G.thisVert.size()][G.thisVert.size()];
		for(int i=0; i<G.thisVert.size();i++)
			this.thisVert.add(new Vertice (G.thisVert.get(i)));
		
		for(int i=0; i<G.thisArest.size();i++)
			this.thisArest.add(new Aresta (G.thisArest.get(i)));
		
		for(int i=0; i<G.thisVert.size();i++)
		{
			for(int j=0; j<G.thisVert.size();j++)
			{
				this.mat[i][j]=G.mat[i][j];
			}
		}
		this.cont = G.cont;
	}
	
	public int insertVertex (){
		System.out.println("[Graph.java] insertVertex()="+cont);
		Vertice novo = new Vertice();
		novo.SetId(cont);
		cont++;
		thisVert.add(novo);
		getGraph();
		Degrees();
		return (novo.GetId());
		
	}
	
	public int getNumberOfVertices() {
		return thisVert.size();
	}
	
	public void insertEdge(int from, int to, float cost){
		System.out.println ("[Graph.java] insertEdge("+from+", "+to+", "+cost+")");
		Aresta aux = new Aresta();
		aux.setTheFrom(from);
		aux.setTheTo(to);
		aux.setCoast(cost);
		thisArest.add(aux);
		Degrees();
		getGraph();
	}
	
	public void removeVertex (int id){
		int pos=-1;
		for (int i=0; i<thisVert.size(); i++)
		{
			if (thisVert.get(i).GetId()==id)
			{
				//System.out.println("Removeu! o id ->" + id);
				while (isArest(id))
				{
					for (int j=0;j<thisArest.size(); j++)
					{
						if ((thisArest.get(j).getTheFrom()==id) || (thisArest.get(j).getTheTo()==id))
						{
							//System.out.println ("achou!!");
							pos=j;
							break;
						}
						else
						{
							//System.out.println("Não achou!");
						}
					}
					if (pos>=0)
					{
						
						//System.out.println("Removeu! e a aresta -> (" + thisArest.get(pos).getTheFrom() + "," + thisArest.get(pos).getTheTo() + ")");
						removeEdge(thisArest.get(pos).getTheFrom(), thisArest.get(pos).getTheTo());
						
					}
				}
				
				thisVert.remove(i);
				Degrees();
				getGraph();
				break;
			}
			else
				System.out.print("");
		}
		
	}
	
	public void removeEdge (int from, int to){
		for (int i=0; i<thisArest.size(); i++){
			if ((thisArest.get(i).getTheFrom()==from) && (thisArest.get(i).getTheTo()==to))
				thisArest.remove(i);
		}
	}
	
	public void getGraph(){
		mat = new float [thisVert.size()][thisVert.size()];
		for (int i=0;i<thisVert.size();i++){
			for (int j=0;j<thisVert.size();j++){
				mat[i][j] = 0;
			}
		}
		
		for (int i=0;i<thisArest.size();i++){
			mat[getVertice(thisArest.get(i).getTheTo())][getVertice(thisArest.get(i).getTheFrom())] = thisArest.get(i).getTheCoast();
		}
		
		/*for (int i=0;i<thisVert.size();i++){
		 for (int j=0;j<thisVert.size();j++){
		 System.out.print (mat[i][j] + " ");
		 }
		 System.out.println();
		 }*/
		
	}
	
	public void Degrees(){
		for (int i=0; i<thisVert.size(); i++)
			thisVert.get(i).setDegrees(0);
		
		
		for (int i=0; i<thisVert.size(); i++)
		{
			for (int j=0; j<thisArest.size(); j++)
			{
				if ((thisArest.get(j).getTheFrom()==thisVert.get(i).GetId()) )
					thisVert.get(i).setDegrees(thisVert.get(i).getDegrees()+1);
			}
			
		}
	}
	
	public Vector searchWidth(int id){
		int visitados[][] = new int [thisVert.size()][2];
		Vector<Integer> fila = new Vector<Integer>();
		Vector<Integer> adjacents = new Vector<Integer> ();
		Vector<Integer> ordem = new Vector<Integer> ();
		int v;
		int aux = -1;
		for (int i=0;i<thisVert.size();i++)
		{
			if(thisVert.get(i).GetId()!=id)
			{
				visitados[i][1]=0;
				visitados[i][0]=thisVert.get(i).GetId();
			}
			else 
			{
				visitados[i][1]=1;
				visitados[i][0]=id;
			}
		}
		fila.add(id);
		
		while(!fila.isEmpty())
		{
			v = fila.get(0);
			for (int i=0;i<getAdjacents(v).size();i++)
			{
				adjacents = getAdjacents(v);
				for (int j=0; j<thisVert.size();j++)
				{
					if(visitados[j][0]==adjacents.get(i))
					{
						aux=j;
					}
				}	
				if(visitados[aux][1]==0)
				{
					visitados[aux][1]=1;
					fila.add(visitados[aux][0]);
				}
			}
			ordem.add(fila.get(0));
			fila.remove(0);
			
		}
		return ordem;
	}
	
	public Vector<Integer> searchDepth (int id){
		Vector<Integer> visitados = new Vector <Integer> ();
		Vector <Integer> ordem = new Vector <Integer> ();
		
		for (int i=0; i<thisVert.size(); i++)
		{
			if(!visitados.contains(id))
			{
				realSearchDepth(id, visitados, ordem);
			}
		}
		return ordem;
	}
	
	public void realSearchDepth(int ide, Vector<Integer> visits, Vector<Integer> order){
		Vector<Integer> adjacents = new Vector<Integer> ();
		order.add(0,ide);
		visits.add(ide);
		adjacents = getAdjacents(ide);
		for (int j=0; j<getAdjacents(ide).size(); j++)
		{
			if (!(visits.contains(adjacents.get(j))))
				realSearchDepth(adjacents.get(j), visits, order);
		}
		
	}
	
	public Vector<Integer> getAdjacents(int id){
		Vector<Integer> adjacents = new Vector<Integer>();
		//System.out.println ("getAdjacents(id) -> " + id);
		for (int i=0; i<thisArest.size(); i++)
		{
			
			if ((thisArest.get(i).getTheFrom()==id))
			{ 
				//System.out.println("(thisArest.get(i).getTheFrom()==id) ->"+ thisArest.get(i).getTheTo() + " " + thisArest.get(i).getTheFrom()  + " " + id);
				adjacents.add(thisArest.get(i).getTheTo());
			}
		}
		return adjacents;
	}
	
	public int getVertice (int id){
		for (int i=0; i<thisVert.size(); i++){
			if (thisVert.get(i).GetId() == id)
				return (i);
		}
		return -1;
	}
	
	public float getCoast (int from, int to){
		for (int i=0; i<thisArest.size();i++)
		{
			if ((thisArest.get(i).getTheFrom()==from) && (thisArest.get(i).getTheTo()==to))
				return thisArest.get(i).getTheCoast();
		}
		return 0;
	}
	
	public boolean isArest (int from, int to){
		for (int i=0; i<thisArest.size();i++)
			if ((thisArest.get(i).getTheFrom()==from) && (thisArest.get(i).getTheTo()==to))
				return true;
		return false;		
	}
	
	public boolean isArest (int id){
		for (int i=0; i<thisArest.size();i++)
			if ((thisArest.get(i).getTheFrom()==id) || (thisArest.get(i).getTheTo()==id))
				return true;
		return false;		
	}	 
	
	public int goodMan(){
		
		Graph H = new Graph (this);
		
		Vector <Integer> adj = new Vector <Integer> ();
		
		Vector <Integer> adjadj = new Vector <Integer> ();
		
		int C=0;
		
		int atual, adjatual;
		
		H.getGraph();
		
		while (H.thisVert.size() != 0) //enquanto tiver algum vértice no grafo
			
		{
			
			//System.out.println ("Id do primeiro vértice de H -> " + H.thisVert.get(0).GetId());
			
			//System.out.println ("Numero de adjacentes ao primeiro vértice de H -> " + getAdjacents(H.thisVert.get(0).GetId()).size() );
			
			while (H.getAdjacents(H.thisVert.get(0).GetId()).size()>0) //enquanto tiver algum adjacente a v0
				
			{
				
				//System.out.println ("Existe um adjacente a v0 ");
				
				adj= H.getAdjacents(H.thisVert.get(0).GetId()); //armazena os adjacentes a v0
				
				//System.out.println ("Id de v0 é -> " + H.thisVert.get(0).GetId());
				
				//System.out.println ("Adjacentes a v0) -> " + adj);
				
				adjadj = H.getAdjacents(adj.get(0)); //armazena os adjacentes do primeiro adjacente a v0
				
				//System.out.println ("Primeiro adjacente a v0 -> " + adj.get(0));
				
				//System.out.println ("Adjacentes do adjacente de v0 -> " + adjadj);
				
				atual= H.thisVert.get(0).GetId(); //armazena o id vértice v0
				
				adjatual= adj.get(0); //armazena o id primeiro adjacente a v0
				
				H.removeVertex(H.thisVert.get(0).GetId()); // remove o vértice "atual"
				
				H.removeVertex(adjatual); //remove o vértice "adjatual"
				
				H.getGraph();
				
				H.insertVertex(); //cria o vértice que será a fusão de "atual" com "adjatual"
				
				//System.out.println ("Removido v0 + adjacente ... tamanho deve ser -1 do anterior -> " + H.thisVert.size());
				
				adj.removeElement(adjatual); //remove o adjatual da lista de adjacentes do vetor adj
				
				adjadj.removeElement(atual);
				
				//System.out.println("Adjacentes a v0 com duplicados-> " + adj);
				
				//System.out.println("Adjacentes a adj de v0 -> " + adjadj);
				
				for (int i=0; i<adj.size();i++)
					
				{
					
					if (adjadj.contains(adj.get(i))) {
						
						adjadj.removeElement(adj.get(i)); //remove arestas que ligam o vértice atual E adjatual
						
					}
					
				}
				
				//System.out.println("Adjacentes a v0 Sem duplicados -> " + adj);
				
				//System.out.println("Adjacentes a adj de v0 -> " + adjadj);
				
				for (int i=0; i<adj.size(); i++)
					
				{
					
					H.insertEdge(H.thisVert.get(H.thisVert.size()-1).GetId(),adj.get(i),(float)2.9);
					
					H.insertEdge(adj.get(i),H.thisVert.get(H.thisVert.size()-1).GetId(),(float)2.9);
					
				}
				
				for (int i=0; i<adjadj.size(); i++)
					
				{
					
					H.insertEdge(H.thisVert.get(H.thisVert.size()-1).GetId(),adjadj.get(i),(float)2.9);
					
					H.insertEdge(adjadj.get(i),H.thisVert.get(H.thisVert.size()-1).GetId(),(float)2.9);
					
				}
				
			}
			
			H.removeVertex(H.thisVert.get(0).GetId());
			
			C++;
			
		}
		
		return C;
		
		
	}
	
	public Graph center() throws java.lang.CloneNotSupportedException {
        
        Graph aux;
        aux = ((Graph)this.clone());
        while(aux.thisVert.size()>2) 
        {
            for (int i=0; i<aux.thisVert.size(); i++)
            {
                if(getAdjacents(aux.thisVert.get(i).GetId()).size()<2)
                    aux.removeVertex(aux.thisVert.get(i).GetId());
            }
        }    
        return aux;
    } 	
	
	public Vector getCenter() throws java.lang.CloneNotSupportedException {
		Graph aux;
		Vector<Integer> retorno = new Vector <Integer> ();
		aux = ((Graph)center().clone());
		for (int i=0; i<thisVert.size();i++)
			retorno.add(thisVert.get(i).GetId());
		return retorno;
	}
	
	public boolean isPair (){
		boolean parOUimpar = true;
		for (int i=0; i<thisVert.size(); i++)
		{
			if (!(((thisVert.get(i).getDegrees())%2)==0))
				parOUimpar = false;
		}
		return parOUimpar;
	}
	
	public Vector fleury(){
		System.out.println("[Graph.java] Fleury()");
        Graph gCopia = new Graph (this);
        Vector caminho = new Vector();
        Aresta aux = new Aresta ();
        Aresta aux1 = new Aresta ();
        if ((gCopia.isPair()) && (gCopia.goodMan() == 1))
        {
            int vertice = gCopia.thisVert.get(0).GetId();
            int origem  = vertice;
            Vector<Integer> adjacents =    gCopia.getAdjacents(vertice);
            do{
                if(adjacents.size() > 0)
                {
                    Graph gRascunho = new Graph(gCopia);
                    gRascunho.removeEdge(vertice, adjacents.firstElement());
                    gRascunho.removeEdge( adjacents.firstElement(),vertice);
                    if (gRascunho.goodMan() == 1)
                    {
                        if(adjacents.firstElement() != origem)
                        {
                            gCopia.removeEdge(vertice, adjacents.firstElement());
                            gCopia.removeEdge(adjacents.firstElement(),vertice );
                            aux.setTheFrom(vertice);
                            aux.setTheTo(adjacents.firstElement());
                            aux.setCoast((float)2.9);
                            caminho.add(aux);
                            vertice = adjacents.firstElement();
                            adjacents = gRascunho.getAdjacents(vertice);
                        }
                        else 
                        {
                            adjacents.removeElementAt(0);
                        }
                        
                    }
                    
                    else
                    {
                        adjacents.remove(0);
                    }
                } 
            }while (gCopia.thisArest.size() > 2);
            
            aux1.setTheFrom(vertice);
            aux1.setTheTo(adjacents.firstElement());
            aux1.setCoast((float)2.9);
            caminho.add(aux);
            return caminho;   
        }
        return null;		
	}
	
	public Vector Dijkstra (int s, int vFinal){
		Vector<Integer> retorna = new Vector<Integer>();
		float dist[] = new float [thisVert.size()];
		int previous[] = new int [thisVert.size()];
		int u=0, v, idU;
		float alt=0;
		Vector<Integer> Q = new Vector<Integer>(); //lista de ID
		Vector<Integer> V = new Vector<Integer>(); //lista de vertice
		Vector<String> S = new Vector<String>(); //vertices
		
		for (int i=0;i<thisVert.size();i++)
		{
			dist[i] = 999999999; //seta distancia infinita
			previous[i] = -1; //vetor de previous
			V.add(i); //adiciona lista de vertices
		}
		
		for (int i=0;i<thisVert.size();i++)
		{
			Q.addElement(thisVert.get(i).GetId()); //preenche a lista de id's
			if(Q.get(i)==s) u=i; //pega a posicao do vertice
		}
		dist[u] = 0; //seta elemento inicial
		
		while (Q.size()!=1)
		{
			u=999;
			for(int i=0;i<V.size();i++) //pega o indice do menor valor de Q
				if(u>dist[V.get(i)]) u = i;
			
			idU = Q.get(u); //pega a id de U
			
			//remove u from Q
			Q.remove(u);
			V.remove(u);
			
			//calculo do menor caminho
			for(int i=0;i<thisVert.size();i++)
			{
				if(this.isArest(idU,thisVert.get(i).GetId()) || this.isArest(thisVert.get(i).GetId(),idU) )
				{
					alt = dist[u] + getCoast(thisVert.get(i).GetId(),idU);
					if(alt < dist[i]) //se o caminho for menor substitui
					{
						dist[i] = alt;
						previous[i] = idU;
						retorna.add(previous[i]);
					}
				}			}
		}
		
		Q.removeAllElements(); //lista de vertices
		V.removeAllElements(); //lista final com o caminho
		int i=0;
		
		for (int j=0;j<thisVert.size();j++)
		{
			Q.addElement(thisVert.get(j).GetId()); //preenche a lista de id's
			if(thisVert.get(j).GetId()==vFinal) i=j;
		}
	    int aux=-1;
		while(aux!=s)
		{
			aux = previous[i];
			for(int j=0;j<Q.size();j++)
				if(aux==Q.get(j)) i = j;
			
			V.add(aux);
		}
		V.add(s);
		
		return V;

	}
}