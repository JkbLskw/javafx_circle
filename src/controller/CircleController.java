package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.CircleElement;

public class CircleController<T> {

	private final int DEGREES_CIRCLE = 360;
	private final int ANIM_REVERSE = 2;

	private List<CircleElement<T>> circleElementList;
	private Pane pane;
	private boolean elementRotatable = false;
	private boolean elementReadable = false;
	private EventType<MouseEvent> elementMouseEvent = null;
	private Selectable elementSelectable = Selectable.NONE;

	// global animation variables
	private int animationDuration = 1000;
	private PathTransition lastTransition = null;
	private CircleElement<T> elementAnimateLast = null;

	private int circleRadius;

	/**
	 * Constructor of circle with generic elements
	 * 
	 * @param elementList
	 *            List of given elements
	 * @param pane
	 *            The pane element (e.g. Pane, GridPane, BorderPane ...)
	 * @param circleRadius
	 *            The radius of the circle
	 */
	public CircleController(List<T> elementList, Pane pane, int circleRadius) {
		this.circleElementList = new ArrayList<CircleElement<T>>();
		this.pane = pane;
		this.circleRadius = circleRadius;

		for (T element : elementList) {
			CircleElement<T> cElement = new CircleElement<T>();
			cElement.setT(element);
			circleElementList.add(cElement);
		}
	}

	/**
	 * Calculates and sets the position and rotation of the circleElements
	 */
	private void calculateCircle() {
		float rotationDegrees = calculateRotationDegrees(Float.valueOf(circleElementList.size()));
		int elementCount = 0;
		for (CircleElement<T> cElement : circleElementList) {

			if (Node.class.isAssignableFrom(cElement.getT().getClass())) {
				Node node = Node.class.cast(cElement.getT());
				node.setTranslateX(((Math.cos(Math.toRadians(rotationDegrees * elementCount))) * circleRadius));
				node.setTranslateY(((Math.sin(Math.toRadians(rotationDegrees * elementCount))) * circleRadius));

				// sets the initial coords for the circle element
				cElement.setInitialX(node.getTranslateX());
				cElement.setInitialY(node.getTranslateY());
				
				if (elementRotatable) {
					if (elementReadable)
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
	public void buildElements() {

		calculateCircle();
		setEvents();
		for (CircleElement<T> cElement : circleElementList) {
			pane.getChildren().add((Node) cElement.getT());
		}
	}

	/**
	 * Calculates the rotation of one circleElement
	 * 
	 * @param n
	 *            The count of elements in the circle
	 * @return
	 */
	private float calculateRotationDegrees(float n) {
		return DEGREES_CIRCLE / n;
	}

	/**
	 * Calculates the rotation of a circleElement to make it readable
	 * (horizontal)
	 * 
	 * @param rotationDegrees
	 *            The degrees of rotation of one circleElement
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
	 * set for each circle element an filter on the given mouse event. animates the circle element by firing the mouse event
	 */
	private void setEvents() {
		if (elementMouseEvent != null) {
			for (CircleElement<T> cElement : circleElementList) {
				Node.class.cast(cElement.getT()).addEventFilter(elementMouseEvent, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mEvent) {
						if (!cElement.getMoving()) {
							cElement.setMoving(true);
							animate(cElement, elementSelectable);
						}
					}

				});
			}
		}
	}

	/**
	 * Inner method to animate a circle element with given properties
	 * @param cElement A circle element
	 * @param elementSelectable The scalable enum property
	 */
	private void animate(CircleElement<T> cElement, Selectable elementSelectable) {

		// builds the path transition
		PathTransition pathTransition = buildTransition(cElement);

		switch (elementSelectable) {
			case NONE:
				// no fixed circle elements. all circle elements have auto reverse
				pathTransition.setAutoReverse(true);
				pathTransition.setCycleCount(ANIM_REVERSE);
				break;
			case MULTI:
				// fixed circle elements and multiple selectable
				pathTransition.setAutoReverse(false);
				break;
			case SINGLE:
				// fixed circle elements and single selectable
				pathTransition.setAutoReverse(false);
				if (lastTransition != null && elementAnimateLast != null) {
					lastTransition.stop();
					Node.class.cast(elementAnimateLast.getT()).setTranslateX(elementAnimateLast.getInitialX());
					Node.class.cast(elementAnimateLast.getT()).setTranslateY(elementAnimateLast.getInitialY());
				}
				break;
		}

		// after finished set the moving to false
		pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cElement.setMoving(false);
			}
		});

		// after a change of status set the moving to false and the circle element to his initial coords
		pathTransition.statusProperty().addListener(new ChangeListener<Status>() {
			@Override
			public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
				Node.class.cast(elementAnimateLast.getT()).setTranslateX(elementAnimateLast.getInitialX());
				Node.class.cast(elementAnimateLast.getT()).setTranslateY(elementAnimateLast.getInitialY());
				cElement.setMoving(false);
			}
		});

		pathTransition.play();
	}

	/**
	 * Builds the PathTransition of one circleElement for animation. Duration can be set with setAnimationDuration.
	 * @param cElement The element witch will animate
	 * @return A PathTransition
	 */
	private PathTransition buildTransition(CircleElement<T> cElement) {
		double currentInitialX = cElement.getInitialX();
		double currentInitialY = cElement.getInitialY();

		double compensateX = Node.class.cast(cElement.getT()).getLayoutBounds().getWidth() / 2,
				compensateY = Node.class.cast(cElement.getT()).getLayoutBounds().getHeight() / 2;

		double circleCenterX = Node.class.cast(cElement.getT()).getScaleX() + compensateX,
				circleCenterY = Node.class.cast(cElement.getT()).getScaleY() + compensateY;

		Path path = new Path();
		path.getElements().add(new MoveTo(currentInitialX + compensateX, currentInitialY + compensateY));
		path.getElements().add(new LineTo(circleCenterX, circleCenterY));

		PathTransition pathTransition = new PathTransition();

		pathTransition.setDuration(Duration.millis(animationDuration));
		pathTransition.setPath(path);
		pathTransition.setNode((Node) cElement.getT());

		// current animation variables now last
		lastTransition = pathTransition;
		elementAnimateLast = cElement;

		return lastTransition;

	}

	/**
	 * Sets the rotatable property
	 * 
	 * @return
	 */
	public boolean isElementRotatable() {
		return elementRotatable;
	}

	/**
	 * Gets the rotatable property
	 * 
	 * @param elementRotate
	 */
	public void setElementRotatable(boolean elementRotate) {
		this.elementRotatable = elementRotate;
	}

	/**
	 * Sets the readable property
	 * 
	 * @return
	 */
	public boolean isElementReadable() {
		return elementReadable;
	}

	/**
	 * Gets the readable property
	 * 
	 * @param elementReadable
	 */
	public void setElementReadable(boolean elementReadable) {
		this.elementReadable = elementReadable;
	}

	public EventType<MouseEvent> getElementMouseEvent() {
		return elementMouseEvent;
	}

	public void setElementMouseEvent(EventType<MouseEvent> elementMouseEvent) {
		this.elementMouseEvent = elementMouseEvent;
	}

	public Selectable getElementSelectable() {
		return elementSelectable;
	}

	public void setElementSelectable(Selectable elementSelectable) {
		this.elementSelectable = elementSelectable;
	}

	public int getAnimationDuration() {
		return animationDuration;
	}

	public void setAnimationDuration(int animationDuration) {
		this.animationDuration = animationDuration;
	}
}
