package main.model;

public class Vertex {
	
	private int id;
	private Label label;
	
	private double time;
	
	public Vertex(int id, Label label) {
		super();
		this.id = id;
		this.label = label;
	}
	
	public Vertex(int id, Label label, double time){
		this(id, label);
		this.time = time;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public double getTime(){
		return time;
	}

}
