package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class Simulation {
	private static final int N = 2;
	private static final double runningTime = 60; //Seconds

	private static final double L = 0.5;

	private static final double radius = 0.005;
	private static final double mass = 0.1;

	private static final double bigRadius = 0.05;
	private static final int bigMass = 100;

	private final static double printInterval = 0.1;

    private static Double currTime = 0d;

	private static ArrayList<Particle> list;
	private static PriorityQueue<Event> events = new PriorityQueue<>(new EventComparator());

	private static List<Particle> borders = generateBorders();


	public static void main( String[] args ){

        System.out.print("Creating particles... ");
		list = Particle.generateParticles(N, radius, mass, bigRadius, bigMass, L);
		System.out.println("Done!");

        initialEventFill();

        events.forEach(System.out::println);

        while (events.size() > 0 && currTime < runningTime){
            Event event = events.poll();
            if (!event.isInvalid()){
                if (event.particleCollision()){
                    double delta = event.getTime() - currTime;
                    advanceParticles(delta);



                    currTime = event.getTime();
                }
            }
        }
	}

    public static void advanceParticles(double time){
        for (Particle p : list) {
            p.advaceDelta(time);
        }
    }

    private static void initialEventFill(){

        for (int i = 0; i < list.size(); i++) {
            Particle p = list.get(i);

            //Check collision with other particles
            for (int j = i + 1; j < list.size(); j++){
                Optional<Double> opt = getCollisionDelta(p, list.get(j));

                if (opt.isPresent()){
                    events.add(new Event(opt.get(), p, list.get(j)));
                }
            }

            //Check collision with wall in X component
            Optional<Double> opt = getCollisionDelta(p.getX(), p.getXSpeed(), p.getRadius());
            if (opt.isPresent()) {
                events.add(new Event(opt.get(), null, p));
            }

            //Check collision with wall in Y component
            opt = getCollisionDelta(p.getY(), p.getYSpeed(), p.getRadius());
            if (opt.isPresent()) {
                events.add(new Event(opt.get(), p, null));
            }
        }

    }

    private static void collision(Particle p1, Particle p2) {
        double totalRadius = p1.getRadius() + p2.getRadius();

        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();

        double deltaVX = p2.getXSpeed() - p1.getXSpeed();
        double deltaVY = p2.getYSpeed() - p1.getYSpeed();

        double deltaVDeltaR = deltaVX * deltaX + deltaVY * deltaY;

        double J = (2 * p1.getMass() * p2.getMass() * deltaVDeltaR)/(totalRadius * (p1.getMass() + p2.getMass()));
        double Jx = J * deltaX / totalRadius;
        double Jy = J * deltaY / totalRadius;

        p1.setXSpeed(p1.getXSpeed() + Jx / p1.getMass());
        p1.setYSpeed(p1.getYSpeed() + Jy / p1.getMass());

        p2.setXSpeed(p1.getXSpeed() + Jx / p2.getMass());
        p2.setYSpeed(p1.getYSpeed() + Jy / p2.getMass());
    }

    /**
     * Computes the time in which two particles are going to collide.
     *
     * @param p1 The first particle.
     * @param p2 The second particle.
     * @return Optional containing or not the time.
     */

    private static Optional<Double> getCollisionDelta(Particle p1, Particle p2) {
        double totalRadius = p1.getRadius() + p2.getRadius();

        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();

        double deltaVX = p2.getXSpeed() - p1.getXSpeed();
        double deltaVY = p2.getYSpeed() - p1.getYSpeed();

        double deltaRSquare = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        double deltaVSquare = Math.pow(deltaVX, 2) + Math.pow(deltaVY, 2);

        double deltaVDeltaR = deltaVX * deltaX + deltaVY * deltaY;

        double d = Math.pow(deltaVDeltaR, 2) - deltaVSquare * (deltaRSquare - Math.pow(totalRadius, 2));


        if (deltaVDeltaR >= 0 || d < 0){
            return Optional.empty();
        }

        return Optional.of(-1 * ((deltaVDeltaR + Math.sqrt(d)) / deltaVSquare));

    }

    /**
     * Computes the time in which a particle is going to collide with the wall of the
     * component that is being tested.
     *
     * @param x The position in that component
     * @param Vx The velocity in that component
     * @return Optional containing or not the time.
     */
    public static Optional<Double> getCollisionDelta(Double x, Double Vx, Double Radius){
        if (Vx.equals(0d)){
            return Optional.empty();
        }

        Double time;

        if (Vx > 0){
            time = (L - Radius - x) / Vx;
        } else {
            time = (Radius - x) / Vx;
        }

        return Optional.of(time);
    }



    /////////////////////////////////////////////////

	private static void updateCollisions(Event e) {
		// TODO Auto-generated method stub

	}



	private static void getNewVelocities(Event e) {
		// TODO Auto-generated method stub

	}



	private static void getInteractions() {
		double tc;
		for(int i =0;i<list.size();i++){
			Particle p = list.get(i);
			//Checks collisions with wall in X
			tc = p.collideX();
			events.add(new Event(tc, p, null));

			//Checks collisions with wall in Y
			tc = p.collideY();
			events.add(new Event(tc,null,p));

			//Checks collisions with other particles
			for(int j=i+1;j<list.size();j++){
				Particle aux = list.get(j);
				tc = p.collide(aux);
				events.add(new Event(tc,p,aux));
			}
		}
	}

	private static void evolveParticles(double time){
		for(Particle p: list){
			p.setX(p.getX() + p.getXSpeed()*time);
			p.setY(p.getY() + p.getYSpeed()*time);
		}
	}

	private static List<Particle> generateBorders() {
		List<Particle> list = new ArrayList<>();
		list.add(new Particle(0,0,0.01));
		list.add(new Particle(0,L,0.01));
		list.add(new Particle(L,0,0.01));
		list.add(new Particle(L,L,0.01));
		return list;
	}

}
