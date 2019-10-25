package movement;

import java.util.List;

import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalPath;

public interface RobotMovement {
	
	List<Direction> moveAtoB(Coordinates a, Coordinates b);
	
}
