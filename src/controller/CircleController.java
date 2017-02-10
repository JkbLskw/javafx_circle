package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.CircleElement;

public class CircleController<T> {
	
	private final int DEGREES_CIRCLE = 360;
	
	private List<CircleElement<T>> circleElementList;
	private Pane pane;
	private boolean elementRotatable = false;
	private boolean elementReadable = false;
	
	private int circleRadius;
	
	/**
	 * Constructor of circle with generic elements
	 * @param elementList List of given elements
	 * @param pane The pane element (e.g. Pane, GridPane, BorderPane ...)
	 * @param circleRadius The radius of the circle
	 */
	public CircleController(List<T> elementList, Pane pane, int circleRadius){
		this.circleElementList = new ArrayList<CircleElement<T>>();
		this.pane = pane;
		this.circleRadius = circleRadius;
		
		for(T element : elementList){
			CircleElement<T> cElement = new CircleElement<T>();
			cElement.setT(element);
			circleElementList.add(cElement);
		}
	}
	
	/**
	 * Calculates and sets the position and rotation of the circleElements
	 */
	private void calculateCircle(){
		float rotationDegrees = calculateRotationDegrees(Float.valueOf(circleElementList.size()));
		int elementCount = 0;
		for(CircleElement<T> cElement : circleElementList){
			
			if(Node.class.isAssignableFrom(cElement.getT().getClass())){
				Node node = Node.class.cast(cElement.getT());
				node.setTranslateX(((Math.cos(Math.toRadians(rotationDegrees * elementCount))) * circleRadius));
				node.setTranslateY(((Math.sin(Math.toRadians(rotationDegrees * elementCount))) * circleRadius));
				
				if(elementRotatable){
					if(elementReadable)
						node.setRotate(calculateElementRotation(rotationDegrees * elementCount));
					else
						node.setRotate(rotationDegrees * elementCount);
				}				
				elementCount += 1;
			}
		}
		
	}
	
	/**
	 * Builds the circle with the circleElements and set them on the pane
	 */
	public void buildElements(){
		
		calculateCircle();
		
		for(CircleElement<T> cElement : circleElementList){
			pane.getChildren().add((Node) cElement.getT());
		}
	}
	
	/**
	 * Calculates the rotation of one circleElement
	 * @param n The count of elements in the circle
	 * @return
	 */
	private float calculateRotationDegrees(float n){
		return DEGREES_CIRCLE / n;
	}
	
	/**
	 * Calculates the rotation of a circleElement to make it readable (horizontal)
	 * @param rotationDegrees The degrees of rotation of one circleElement
	 * @return
	 */
	private double calculateElementRotation(float rotationDegrees) {
		if (rotationDegrees >= 90 && rotationDegrees < 270) {
			return rotationDegrees - 180;
		} else {
			return rotationDegrees;
		}
	}

	/**
	 * Sets the rotatable property
	 * @return
	 */
	public boolean isElementRotatable() {
		return elementRotatable;
	}

	/**
	 * Gets the rotatable property
	 * @param elementRotate
	 */
	public void setElementRotatable(boolean elementRotate) {
		this.elementRotatable = elementRotate;
	}

	/**
	 * Sets the readable property
	 * @return
	 */
	public boolean isElementReadable() {
		return elementReadable;
	}

	/**
	 * Gets the readable property
	 * @param elementReadable
	 */
	public void setElementReadable(boolean elementReadable) {
		this.elementReadable = elementReadable;
	}
}
