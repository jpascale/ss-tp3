package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Simulation {
	private static final int N = 150;
	private static final double runningTime = 60; //Seconds

	private static final double L = 0.5;

	private static final double radius = 0.005;
	private static final double mass = 0.1;

	private static final double bigRadius = 0.05;
	private static final int bigMass = 100;

	private final static double printInterval = 0.1;

	private static List<Particle> list;
	private static PriorityQueue<Event> events;

	private static List<Particle> borders = generateBorders();

	public static void main( String[] args ){
		boolean finished = false;
		events = new PriorityQueue<>();
		list = Particle.generateParticles(N, radius, mass, bigRadius, bigMass, L);

		//debug
		Particle.CreateFile(list);

		//Initial interactions
		getInteractions();

		for(int i = 0; i< runningTime || !finished; i++){
			Event e = events.poll();
			if(e == null){
				finished = true;
			}
			evolveParticles(e.getTime());
			getNewVelocities(e);
			updateCollisions(e);
		}
	}



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
