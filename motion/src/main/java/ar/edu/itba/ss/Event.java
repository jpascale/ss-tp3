package ar.edu.itba.ss;

/**
 * Create a new event representing a collision between
 * particles p1 and p1 at time t. If neither p1 nor p2 is null, then it represents a pairwise collision between p1 and p2; if both p1
 * and p2 are null, it represents a redraw event; if only p2 is null, it represents a collision between p1 and a vertical wall; if
 * only p1 is null, it represents a collision between p2 and a horizontal wall.
 */

public class Event {

	private final Double time;
	private final Particle p1;
	private final Particle p2;

	private Integer eventCountP1 = 0;
    private Integer eventCountP2 = 0;


    /**
	 * If (a != null && b != null) Collision between particles
	 * If (a != null) Collision X wall
	 * If (b != null) Collision Y wall
	 * Else Redraw the map (Update Particles)
	 * @param p1 particle 1
	 * @param p2 particle 2
	 */
	public Event(double time, Particle p1, Particle p2){
		this.time = time;
		this.p1 = p1;
		this.p2 = p2;

        if (this.p1 != null)
            this.eventCountP1 = p1.getEventCount();

        if (this.p2 != null)
            this.eventCountP2 = p2.getEventCount();
	}

    public boolean isInvalid(){
        return !((p1 != null && !p1.getEventCount().equals(eventCountP1)) || (p2 !=null && !p2.getEventCount().equals(eventCountP2)));
    }

    public double getEventTimeP1() {
        return eventCountP1;
    }

    public double getEventTimeP2() {
        return eventCountP2;
    }


	public Double getTime() {
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (p1 != null && p2 != null){
            sb.append("Collision particle").append(p1.getId()).append(" and particle").append(p2.getId()).append(" t=").append(time);
        } else if (p2 == null) {
            sb.append("Collision particle").append(p1.getId()).append(" and wall Y t=").append(time);
        } else {
            sb.append("Collision particle").append(p2.getId()).append(" and wall X t=").append(time);
        }

        return sb.toString();
    }

}
