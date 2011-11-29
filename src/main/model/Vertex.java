package main.model;

public class Vertex {
	
	private int id;
	private Label label;
	
	private double time;
	private double reliability;

	public Vertex(Vertex other){
		this.id = other.getId();
		this.label = other.getLabel();
		this.time = other.getTime();
		this.reliability = other.getReliability();
	}
	
	public Vertex(int id) {
		this.id = id;
	}
	
	public Vertex(int id, Label label) {
		this(id);
		this.id = id;
		this.label = label;
	}
	
	public Vertex(int id, double time, double reliability){
		this(id);
		this.time = time;
		this.reliability = reliability;
	}
	
	public Vertex(int id, Label label, double time, double reliability){
		this(id, label);
		this.time = time;
		this.reliability = reliability;
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
	
	public void setTime(double time){
		this.time = time;
	}

	public double getReliability() {
		return reliability;
	}

	public void setReliability(double reliability) {
		this.reliability = reliability;
	}

}
