package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import model.Model;

/**
 * This is a sample of what you can do with DBCanvas.
 * 
 * @author J2579
 */

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {
	
	private static final int TICK_RATE = 60; //60 ticks per second
	private static final int WIDTH = 400;  //GUI is 400px wide
	private static final int HEIGHT = 400; //GUI is 400px tall
	
	private static final int BASE_CIRCLE_RADIUS = 20;    //Each circle's radius is a multiple of 20
	private static final double BASE_CHANGE_RATE = 0.01; //Each circle's 'speed' is a multiple of 0.01
	
	private Timer timer;
	private ArrayList<Model> modelList;
	private ModelWindow window;
	
	public static void main(String[] args) {
		GUI s = new GUI();
		s.run();
	}
	
	public void run() {
		
		setTitle("Model Test");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit button stops program
		
		//Setup the "concentric" circles
		setUpModels();
		
		//Add graphics window to GUI
		window = new ModelWindow(WIDTH, HEIGHT);
		add(window); 
		
		//Show the window
		setVisible(true);
		
		//Create the window's "Buffer"
		window.createAndSetBuffer();
		
		//Create the timer
		timer = new Timer(1000 / TICK_RATE, this); //Timer will tick TICK_RATE times per second
												   //[every (1000/TICK_RATE) ms]
		//Start the timer
		timer.start(); 
	}
	
	private void setUpModels() {
		
		modelList = new ArrayList<Model>(); //Initialize the empty list
		
		/**************************************************************
		 * Create 6 circles. We start at 'idx = 1', so that our first * 
		 * (0th) circle does not have a radius and change rate of 0.  *
		 **************************************************************/
		for(int idx = 1; idx < 7; ++idx) { 	
			modelList.add(new Model(BASE_CHANGE_RATE * idx, BASE_CIRCLE_RADIUS * idx));
		}
	}

	/**
	 * Because our class GUI as a whole implements ActionListener, we have to override
	 * actionPerformed. This means that if we pass in GUI as an actionListener to something,
	 * whenever that 'something' activates, it will send an actionEvent here. We check that
	 * the action signal comes from the timer, and call the code we need.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//If the timer gave us an action signal
		if(e.getSource().equals(timer)) {
			
			//Update our models
			for(int idx = 0; idx < modelList.size(); ++idx) {
				modelList.get(idx).update();
			}
			
			//Update the graphics
			window.update();
		}
	}
	
	/**
	 * The 'Canvas' class. Because it extends DoubleBufferedCanvas, the only thing				   
	 * that we need to do is give it a constructor call, and override draw(). We could've		   
	 * made this a public class in another file, but then we would've had to put in logic for 	   
	 * accessing private variable "modelList" from our GUI class. Instead, this approach allows us 
	 * just to reference modelList directly.													   
	 */
	private class ModelWindow extends DoubleBufferedCanvas {

		public ModelWindow(int width, int height) {
			super(width, height); //Any class that 'extends' another class requires you
								  //to call the extended class's constructor through super(),
								  //if your class's constructor has parameters.
		}

		@Override
		public void draw(Graphics g) {
			
			g.setColor(Color.BLACK); //After this call, everything drawn to the screen (even text)
									 //will be colored black
			
			for(int idx = 0; idx < modelList.size(); ++idx) {
				
				Model m = modelList.get(idx); //Get a copy of the 'model' we're referencing
				
				/************************************************************************
				* Because Java has no g.drawCircle(), we have to make some minor		*
				* coordinate changes to accommodate. drawOval(x,y,width,height) draws 	*
				* an ellipse with width 'width', and height 'height', starting from the	*
				* top-left corner, specified by point (x,y). 							*
				*************************************************************************/
				int x = m.getX() - (m.getRadius()/2);
				int y = m.getY() - (m.getRadius()/2);
				
				/******************************************************************************** *
				* WIDTH/2 = Center 'X' position of the screen									  *
				* HEIGHT/2 = Center 'Y' position of the screen. Because AWT (and pygame, as well) *
				* treat coordinates like the 4th quadrant of a graph, y = HEIGHT - (quadrant 1 y) *
				***********************************************************************************/
				
				x += getWidth() / 2;															  
				y += getHeight() - (getHeight() / 2);											  
				
				//Draw the 'circle'
				g.drawOval(x, y, m.getRadius(), m.getRadius());
			}
			
		}
	}
}