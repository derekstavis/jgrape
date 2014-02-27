package Graph;

import java.io.Serializable;

public class Vertice implements Serializable {
	int id=-1;
	int degree=0;
	
	public Vertice (){
	}
	
	public Vertice (Vertice V){
		this.id = V.GetId();
		this.degree=V.getDegrees();
	}		
	
	public void SetId(int id){
		this.id = id;
	}
	
	public int GetId(){
		return this.id;
	}
	
	public void setDegrees(int grau){
		degree=grau;
	}
	
	public int getDegrees(){
		return degree;
	}

}
