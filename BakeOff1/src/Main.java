import java.awt.AWTException;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;

import java.awt.Robot;

public class Main extends PApplet
{
	// you shouldn't need to edit any of these variables
	int margin = 300; // margin from sides of window
	int marginX = 500;
	int marginY = 200;
	

	final int dimen = 60; // padding between buttons and also their width/height
	final int padding = 10;
	//final int padding = 28;
	ArrayList<Integer> trials = new ArrayList<Integer>(); //contains the order of buttons that activate in the test
	int trialNum = 0; //the current trial number (indexes into trials array above)
	int startTime = 0; // time starts when the first click is captured.
	int userX = mouseX; //stores the X position of the user's cursor
	int userY = mouseY; //stores the Y position of the user's cursor

	int prevUserX = mouseX; //stores the X position of the user's cursor
	int prevUserY = mouseY; //stores the Y position of the user's cursor
	int prevTime = 0; 
	int finishTime = 0; //records the time of the final click
	int hits = 0; //number of succesful clicks
	int misses = 0; //number of missed clicks
	Robot robot;
	// You can edit variables below here and also add new ones as you see fit
	int numRepeats = 10; //sets the number of times each button repeats in the test (you can edit this)
	double easing = 0.05;
	boolean start = false;
	int prev_dist = Integer.MAX_VALUE;
	int dist = 0;
	boolean first = true;



	public void draw()
	{

		//margin = width/3; //scale the padding with the size of the window
		//margin = width/6;
		margin = 300;

		if(first)
		{
			userX = 275;
			userY = 275;
			first = false;
		}


		//background(0); //set background to black
		background(255);

		if (trialNum >= trials.size()) //check to see if test is over
		{
			//fill(255); //set fill color to white
			fill(0);
			//write to screen
			text("Finished!", width / 2, height / 2); 
			text("Hits: " + hits, width / 2, height / 2 + 20);
			text("Misses: " + misses, width / 2, height / 2 + 40);
			text("Accuracy: " + (float)hits*100f/(float)(hits+misses) +"%", width / 2, height / 2 + 60);
			text("Total time taken: " + (finishTime-startTime) / 1000f + " sec", width / 2, height / 2 + 80);
			text("Average time for each button: " + ((finishTime-startTime) / 1000f)/(float)(hits+misses) + " sec", width / 2, height / 2 + 100);      
			return; //return, nothing else to do now test is over
		}

		//fill(255); //set fill color to white
		fill(0);
		text((trialNum + 1) + " of " + trials.size(), 40, 20); //display what trial the user is on
		//text("Prev Distance from target " + userX+" " +userY,100,40);

		for (int i = 0; i < 16; i++)// for all button
			drawButton(i); //draw button

		// you shouldn't need to edit anything above this line! You can edit below this line as you see fit
		//text("Distance from target " + dist,100,40); 
		//text("Prev Distance from target " + prev_dist,100,80); 


		//fill(255, 0, 0); // set fill color to red
		//fill(0, 0, 255);
		//ellipse(userX, userY, 20, 20); //draw user cursor as a circle with a diameter of 20

		stroke(220, 20, 60);
		strokeWeight(3);
		line(userX - 10, userY, userX + 10, userY);
		line(userX, userY - 10, userX, userY + 10);

		//fill(0,0);


		float endPX = (float) ((3) * dimen + (3) *padding + marginX +dimen);
		float endPY = (float) ((3) * dimen + (3) *padding + marginY +dimen);

		stroke(0);
		strokeWeight(4);

		line(marginX, marginY, endPX,marginY);
		line(marginX, marginY, marginX,endPY);
		line(marginX,endPY, endPX,endPY);
		line(endPX,marginY, endPX,endPY);

		//rect(padding  + margin, padding  + margin, x, y);

		stroke(0, 0, 0);
		strokeWeight(2);

	}
	public void keyPressed() {
		// if (key == ' ') {
		mousePressed();
		//} 
	}

	public void mousePressed() // test to see if hit was in target!
	{
		int clickTime = millis();
		if (trialNum >= trials.size())
			return;

		if (trialNum == 0)
		{ //check if first click
			startTime = millis();
			clickTime = startTime;
		}

		if (trialNum == trials.size() - 1) //check if final click
		{
			finishTime = millis();
			//write to terminal

			System.out.println("Hits: " + hits);
			System.out.println("Misses: " + misses);
			System.out.println("Accuracy: " + (float)hits*100f/(float)(hits+misses) +"%");
			System.out.println("Total time taken: " + (finishTime-startTime) / 1000f + " sec");
			System.out.println("Average time for each button: " + ((finishTime-startTime) / 1000f)/(float)(hits+misses) + " sec");
		}

		Rectangle bounds = getButtonLocation(trials.get(trialNum));

		// YOU CAN EDIT BELOW HERE IF YOUR METHOD REQUIRES IT (shouldn't need to edit above this line)
		int hit = 1;
		if ((userX > bounds.x && userX < bounds.x + bounds.width) && (userY > bounds.y && userY < bounds.y + bounds.height)) // test to see if hit was within bounds
		{
			// System.out.println("HIT! " + trialNum + " " + (millis() - startTime)); // success
			hit = 1;
			hits++;
		} else
		{
			//System.out.println("MISSED! " + trialNum + " " + (millis() - startTime)); // fail
			hit = 0;
			misses++;
		}

		System.out.println(trialNum+","+prevUserX+","+prevUserY+","+userX+","+userY+","+dimen+","+(clickTime - prevTime)+","+hit);

		trialNum++; // Increment trial number
		
		prevUserX = userX;
		prevUserY = userY;
		prevTime = clickTime;

		//userX = 275;
		//userY = 275;
	}

	boolean isWithinSquare(double userX, double userY)
	{
		for (int i = 0; i < 16; i++)
		{
			double x = (i % 4) * dimen + (i % 4)*padding + marginX;
			double y = (i / 4) * dimen + (i / 4)*padding+ 1.5 + marginY;

			if ((userX > x && userX < x +dimen) 
					&& (userY > y && userY < y + dimen)) 
				return true;
		}
		return false;
	}

	public void updateUserMouse() // YOU CAN EDIT THIS
	{




		double easing = 1.9;

		double newPosX = userX + easing*(mouseX - pmouseX);
		double newPosY = userY + easing*(mouseY - pmouseY);




		if(isWithinSquare(newPosX,newPosY))
			easing = 1;

		userX += easing*(mouseX - pmouseX); //add to userX the difference between the current mouseX and the previous mouseX
		userY += easing*(mouseY - pmouseY);
		if(userX<marginX+10)
			userX = marginX+10;
		if(userY<marginY+10)
			userY = marginY+10;

		float endX = (float) ((15 % 4)* dimen + (15 % 4)*padding + marginX+dimen -10);
		float endY = (float) ((15 / 4)* dimen + (15 / 4)*padding + marginY+dimen -10);

		if(userX>endX)
			userX = (int)endX;
		if(userY> endY)
			userY = (int)endY;


	}









	// ===========================================================================
	// =========SHOULDN'T NEED TO EDIT ANYTHING BELOW THIS LINE===================
	// ===========================================================================

	public void setup()
	{
		//size(900,900); // set the size of the window
		size(1500,900);
		try {
			robot = new Robot();

		} catch (AWTException e) {
			e.printStackTrace();
		} 
		noCursor(); // hides the system cursor (can turn on for debug, but should be off otherwise!)
		noStroke(); //turn off all strokes, we're just using fills here (can change this if you want)
		textFont(createFont("Arial",16));
		textAlign(CENTER);
		frameRate(120);
		ellipseMode(CENTER); //ellipses are drawn from the center (BUT RECTANGLES ARE NOT!)
		// ====create trial order======
		for (int i = 0; i < 16; i++)
			// number of buttons in 4x4 grid
			for (int k = 0; k < numRepeats; k++)
				// number of times each button repeats
				trials.add(i);

		Collections.shuffle(trials); // randomize the order of the buttons    
		//System.out.println("trial order: " + trials);


		//double x = (5 % 4) * padding * 2 + width/8;
		//double y = (5 / 4) * padding * 2 + width/8;
		double x = 600;
		double y = 300;
		robot.mouseMove((int)x,(int) y);
		userX = mouseX;
		userY = mouseY;


	}

	public Rectangle getButtonLocation(int i)
	{
		double x = (i % 4) * dimen + (i % 4) *padding + marginX;
		double y = (i / 4) * dimen + (i / 4) *padding + marginY;

		return new Rectangle((int)x, (int)y, dimen, dimen);
	}

	public void drawButton(int i)
	{
		Rectangle bounds = getButtonLocation(i);
		fill(0);
		if (trials.get(trialNum) == i) // see if current button is the target
		{
			//fill(0, 255, 255); // if so, fill cyan
			fill(50, 205, 50);
			if ((userX > bounds.x && userX < bounds.x + bounds.width) && (userY > bounds.y && userY < bounds.y + bounds.height))
				fill(220,20,60);
		}
		else
		{
			fill(105); // if not, fill gray
			//fill(0);

		}

		rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void mouseMoved() // Don't edit this
	{
		updateUserMouse();
	}

	public void mouseDragged() // Don't edit this
	{
		updateUserMouse();
	}
}