# JavaFX Circle
Project to generate a circle of given javafx-elements.

## Usage (e.g. list of labels)
```java
import controller.CircleController;

...

Pane circlePane = new Pane();
int circleRadius = 250;

CircleController<Label> circleController = new CircleController<Label>(labelList, circlePane, circleRadius);

circleController.setElementMouseEvent(MouseEvent.MOUSE_CLICKED);
circleController.setElementSelectable(Selectable.NONE);
circleController.setElementRotatable(true);
circleController.setElementReadable(true);
circleController.buildElements();
```
