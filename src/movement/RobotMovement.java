
package movement;

import java.util.List;
import navitagion.Coordinates;
import navitagion.Direction;


public interface RobotMovement {
	
	List<Direction> moveAtoB(Coordinates a, Coordinates b);
	
}
