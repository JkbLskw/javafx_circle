package model;

public class CircleElement<T> {
	private T t;
	private double initialX;
	private double initialY;
	private boolean moving;
	private boolean selected;

	public CircleElement() {
		this.moving = false;
	}

	public T getT() {
		return t;
	}

	public void setT(T newT) {
		this.t = newT;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean getMoving() {
		return this.moving;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CircleElement other = (CircleElement) obj;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}

	public double getInitialX() {
		return initialX;
	}

	public void setInitialX(double initialX) {
		this.initialX = initialX;
	}

	public double getInitialY() {
		return initialY;
	}

	public void setInitialY(double initialY) {
		this.initialY = initialY;
	}
}
