package Graph;

import java.io.Serializable;

public class Aresta implements Serializable {
	int to;
	int from;
	float coast=-1;
	
	public Aresta (){
		
	}

	public Aresta (Aresta E){
		this.from = E.getTheFrom();
		this.to = E.getTheTo();
		this.coast = E.getTheCoast();
	}	
	
	public void setTheTo (int to){
		this.to = to;
	}
	
	public int getTheTo(){
		return to;
	}
	
	public void setTheFrom(int from){
		this.from = from;
	}	
	
	public int getTheFrom(){
		return from;
	}
	
	public void setCoast (float coast){
		this.coast = coast;
	}
	
	public float getTheCoast(){
		return coast;
	}

	public void InsereArest(int from, int to){
		setTheFrom(from);
		setTheTo(to);
	}
	

}
