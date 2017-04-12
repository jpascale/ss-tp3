package ar.edu.itba.ss;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {

	private Integer id;

	private Double radius = 0.0;
	private Double mass = 0.0;

	private Double x_pos = 0.0;
	private Double y_pos = 0.0;
	
	private Double x_speed = 0.0;
	private Double y_speed = 0.0;

    private Integer eventCount = 0;

    public Particle(final Integer id){
		this.id = id;
	}


	public Particle(int id, double radius, double mass, double x, double y, double x_speed, double y_speed, double speedModule, double heading){
		this.id = id;
		this.radius = radius;
		this.mass = mass;
		this.x_pos = x;
		this.y_pos = y;
		this.x_speed = x_speed;
		this.y_speed = y_speed;
	}

	public Particle(int id, double radius, double mass, double x, double y, double x_speed, double y_speed){
		this.id = id;
		this.radius = radius;
		this.mass = mass;
		this.x_pos = x;
		this.y_pos = y;
		this.x_speed = x_speed;
		this.y_speed = y_speed;
	}
	
	public Particle(double x, double y,double radius){
		this.x_pos = x;
		this.y_pos = y;
		this.radius = radius;
	}

    /**
     * Generates N + 1 particles with a valid position in space of a given length side.
     * Particle with id 0 is the one corresponding to bigRadius and bigMass, the rest
     * are created with radius and mass.
     *
     * @param N Number of small particles
     * @param radius Radious of the N small particles
     * @param mass Mass of the N small particles
     * @param bigRadius Radius of the big particle
     * @param bigMass Mass of the big particle
     * @param L Side length of the space
     * @return List of the generated particles with valid position
     */
	public static ArrayList<Particle> generateParticles(int N, double radius, double mass, double bigRadius, int bigMass, double L) {
		ArrayList<Particle> list = new ArrayList<>();
		Random rand = new Random();

        //Create big particle with id 0
		createParticle(0, bigRadius, bigMass, 0, L, list);

		//Create small particles
		for (int i = 1; i <= N; i++){
            System.out.println("Creating particle " + i);
			createParticle(i, radius, mass, 0.1, L, list);
		}

		return list;
	}

    /**
     * Generates one single particle and makes sure it is correctly placed in the space.
     * Speed is hardcoded for every component between 0.1 and -0.1
     *
     * @param radius Radius of the particle
     * @param mass Mass of the particle
     * @param L Side length of the space
     * @param list List of the particles already placed in the space
     */
    private static void createParticle(int id, double radius, double mass, double speedRange, double L, ArrayList<Particle> list){
        Random rand = new Random();
        double randX, randY;

        //Place it correctly in space
        do {
            randX = L * rand.nextDouble();
            randY = L * rand.nextDouble();
        } while(!valid(randX, randY, list, L, radius));

        //Define speed vector
        double randVx = rand.nextDouble() * (speedRange * 2) - speedRange;
        double randVy = rand.nextDouble() * (speedRange * 2) - speedRange;

        list.add(new Particle(id, radius, mass, randX, randY, randVx, randVy));
    }

	private static boolean valid(final double x, final double y, final ArrayList<Particle> list, final double l, final double r) {
		if (x - r <= 0 || x + r >= l || y - r <= 0 || y + r >= l) {
			return false;
		}
		for (Particle p:list){
			double dist = Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
			double rad = r + p.getRadius();
			if (Double.compare(dist, rad) <= 0){
				return false;
			}
		}
		return true;
	}

	public void advaceDelta(double delta) {
		x_pos = x_pos + x_speed * delta;
		y_pos = y_pos + y_speed * delta;
	}
	
	public double collideX() {
		if(x_speed > 0){
			return 0;
		}
		// TODO
		return 0;
	}

	public double collideY() {
		if(y_speed > 0){
			return 0;
		}
		// TODO
		return 0;
	}

	public double collide(final Particle particle) {
		// TODO 
		return 0;
	}
	
	public void bounceX(){
		// TODO 
	}
	
	public void bounceY(){
		// TODO 
	}
	
	public void bounce(Particle p){
		// TODO 
	}
	

	public double getXSpeed(){
		return x_speed;
	}

	public double getYSpeed(){
		return y_speed;
	}

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Particle particle = (Particle) o;

		return id.equals(particle.id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public Double getY() {
		return y_pos;
	}
	public void setY(double y_pos) {
		this.y_pos = y_pos;
	}

	public Double getX() {
		return x_pos;
	}

	public void setX(double x_pos) {
		this.x_pos = x_pos;
	}

	public Double getMass() {
		return mass;
	}

	public Double getRadius() {
		return radius;
	}

    public void setXSpeed(Double x_speed) {
        this.x_speed = x_speed;
    }

    public void setYSpeed(Double y_speed) {
        this.y_speed = y_speed;
    }

    @Override
	public String toString() {
		return "Particle " + this.id + " (" + this.x_pos + "," + this.y_pos + ")(" + this.x_speed + "," + this.y_speed + ")";
	}

	public Integer getId() {
		return id;
	}


    public void increaseEventCount() {
        eventCount++;
    }


	public double getSpeed() {
		return Math.sqrt(Math.pow(getXSpeed(), 2)+ Math.pow(getYSpeed(), 2));
	}


	public double getPosition() {
		return Math.sqrt(Math.pow(getX(), 2)+ Math.pow(getY(), 2));
	}
}

