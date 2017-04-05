package ar.edu.itba.ss;

import java.util.Comparator;

public class Event implements Comparator<Event>{
	private double time;
	private Particle p1;
	private Particle p2;
	
	/**
	 * If (a != null && b != null) Collision between particles
	 * If (a != null) Collision X wall
	 * If (b != null) Collision Y wall
	 * Else Redraw the map (Update Particles)
	 * @param a
	 * @param b
	 */
	public Event(double time, Particle p1,Particle p2){
		this.time = time;
		this.p1 = p1;
		this.p1 = p2;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public void setP1(Particle p1) {
		this.p1 = p1;
	}

	public void setP2(Particle p2) {
		this.p2 = p2;
	}

	public double getTime() {
		return time;
	}

	public Particle getP1() {
		return p1;
	}

	public Particle getP2() {
		return p2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
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
		Event other = (Event) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}

	@Override
	public int compare(Event o1, Event o2) {
		return (int)(o1.getTime() - o2.getTime());
	}
		
	
}
