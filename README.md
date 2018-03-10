# JavaFX Circle
Project to generate a circle of given javafx-elements.

## Usage (e.g. list of labels)
```java
import controller.CircleController;

...

Pane circlePane = new Pane();
int circleRadius = 250;

CircleController<Label> circleController = new CircleController<Label>(labelList, circlePane, circleRadius);

cController.setElementMouseEvent(MouseEvent.MOUSE_CLICKED);
cController.setElementSelectable(Selectable.NONE);
cController.setElementRotatable(true);
cController.setElementReadable(true);
cController.buildElements();
```
