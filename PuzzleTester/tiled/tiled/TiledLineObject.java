package tiled;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public abstract class TiledLineObject extends TiledObject {

	private static final long serialVersionUID = -4100680246093467329L;

	public static class Point implements Serializable {
		
		private static final long serialVersionUID = 9014114862165386002L;

		public float x;
		public float y;
		
		
		
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public Point() {

		}

		@Override
		public String toString() {
			return String.valueOf(x) + "," + String.valueOf(y);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(x);
			result = prime * result + Float.floatToIntBits(y);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
				return false;
			if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
				return false;
			return true;
		}
		
	}
	
	private List<Point> points = Lists.newArrayList();

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TiledLineObject other = (TiledLineObject) obj;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		return true;
	}
	
	
	
}
