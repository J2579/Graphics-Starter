package model;

public class Model {

	private double theta; //Radians
	private int radius;
	private double change; //Radians / Tick
	
	public Model(double change, int radius) {
		theta = 0;
		this.radius = radius;
		this.change = change;
	}
	
	public void update() {
		theta += change;
		theta %= (2 * Math.PI); //[0,2pi)
	}
	
	public int getX() {
		return (int) (radius * Math.cos(theta));
	}
	
	public int getY() {
		return (int) (radius * Math.sin(theta));
	}
	
	public int getRadius() {
		return radius;
	}
}