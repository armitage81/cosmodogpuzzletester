package tiled;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * tiled.Position of a point
 * Represents a point in 2D space and the name of the map it is on.
 */
public class Position implements Serializable {

	@Serial
	private static final long serialVersionUID = 703260030937304215L;

	private float x;
	private float y;

	public static Position fromCoordinatesOnPlayerLocationMap(float x, float y) {
		return fromCoordinates(x, y);
	}

	public static Position fromCoordinates(float x, float y) {
		Position position = new Position();
		position.x = x;
		position.y = y;
		return position;
	}

	private Position() {

	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	/**
	 * Moves the position at given offsets.
	 * Offsets can be negative (moving left and up).
	 * THIS IS NOT POSITIONING, RESULT IS RELATIVE TO CURRENT PLACEMENT!!!
	 */
	public void shift(float offsetX, float offsetY) {
		this.x += offsetX;
		this.y += offsetY;
	}

	public Position copy() {
        return Position.fromCoordinates(this.x, this.y);
	}

	public Position shifted(float offsetX, float offsetY) {
		Position shiftedPosition = Position.fromCoordinates(this.x, this.y);
		shiftedPosition.shift(offsetX, offsetY);
		return shiftedPosition;
	}

	/**
	 * Moves the position to given location.
	 * RESULTING POSITION IS ABSOLUTE!!!
	 */
	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float distance(float x, float y) {
		float deltaX = this.getX() - x;
		deltaX = deltaX < 0 ? (deltaX * -1) : deltaX;
		float deltaY = this.getY() - y;
		deltaY = deltaY < 0 ? (deltaY * -1) : deltaY;
		
		return deltaX + deltaY;
	}
	
	public float distance(Position pos) {
		return distance(pos.getX(), pos.getY());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return Float.compare(x, position.x) == 0 && Float.compare(y, position.y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}

	/*
	public DirectionType facingDirection(Position reference) {
		float deltaX = reference.x - this.x;
		float deltaY = reference.y - this.y;
		if (deltaX <= deltaY) {
			return deltaY <= 0 ? DirectionType.NORTH : DirectionType.SOUTH;
		} else {
			return deltaX <= 0 ? DirectionType.WEST : DirectionType.EAST;
		}
	}

	public static void main(String[] args) {
		Position ref = Position.fromCoordinates(3, 3);
		Position other;

		other = Position.fromCoordinates(3, 3);
		System.out.println(ref.facingDirection(other));

	}
	*/

}
