package sensors;

import navitagion.Coordinates;


//Test implimentation for testing purposes only - Kevin
public class TESTSensors implements ISensorPackage{
	
	boolean cleanTile = true;

	@Override
	public boolean collisionLeft(Coordinates coord) {
		return false;
	}

	@Override
	public boolean collisionAbove(Coordinates coord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collisionRight(Coordinates coord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collisionBelow(Coordinates coord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dirtDetector(Coordinates coord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int terrainType(Coordinates coord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Coordinates[] stationInRange(Coordinates coord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanTile(Coordinates coord) {
		// TODO Auto-generated method stub
		
	}
	

}
