# JavaFX Circle
Project to generate a circle of given javafx-elements.

## Usage (e.g. list of labels)
```java
import controller.CircleController;

...

Pane circlePane = new Pane();
int circleRadius = 250;

CircleController<Label> circleController = new CircleController<Label>(labelList, circlePane, circleRadius);

circleController.setElementRotatable(true);
circleController.setElementReadable(false);
circleController.buildElements();
```

Example:
![alt JavaFX Circle Example][exa]

[exa]: http://wp12362127.server-he.de/github_space/example.png "JavaFX Circle Example"