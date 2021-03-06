package tests;

import navitagion.Coordinates;
import WorldSimulator.TileType;
import sensors.ISensorPackage;


//Test implementation for testing purposes only - Kevin
public class MockSensors implements ISensorPackage{
	
	boolean cleanTile = true;
	int aboveCount = 10;
	int leftCount = 20;
	int belowCount = 10;
	int rightCount = 5;

	@Override
	public boolean collisionWest(Coordinates coord) {
		if (leftCount-- < 0) {
			leftCount = 20;
			return true;
		}
		else return false;
	}

	@Override
	public boolean collisionNorth(Coordinates coord) {
		if (aboveCount-- < 0) {
			aboveCount = 10;
			return true;
		}
		else return false;
	}

	@Override
	public boolean collisionEast(Coordinates coord) {
		if (rightCount-- < 0) {
			rightCount = 5;
			return true;
		}
		else return false;
	}

	@Override
	public boolean collisionSouth(Coordinates coord) {
		if (belowCount-- < 0) {
			belowCount = 10;
			return true;
		}
		else return false;
	}

	@Override
	public boolean dirtDetector(Coordinates coord) {
		//Cycles for robot testing
		if (cleanTile == true) {
			cleanTile = false;
		}
		else cleanTile = true;
		
		return cleanTile;
	}

	@Override
	public TileType terrainType(Coordinates coord) {
		// TODO Auto-generated method stub
		return TileType.HIGH;
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
