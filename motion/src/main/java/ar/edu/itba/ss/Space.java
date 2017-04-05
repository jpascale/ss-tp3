package ar.edu.itba.ss;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//TODO: Test the whole class
public class Space {
    final private Integer M;
    final private Double L;
    final private Double Rc;

    //Matrix with Particle lists
    final private ArrayList<ArrayList<HashSet<Particle>>> space = new ArrayList<>();

    final private List<Particle> particleList;

    //TODO: implement factory
    public Space(final Integer M, final Double L, final Double Rc, List<Particle> particleList) {
        this.M = M;
        this.L = L;
        this.Rc = Rc;
        this.particleList = particleList;

        for (int i = 0; i < M; i++) {
            space.add(new ArrayList<>());
            for (int j = 0; j < M; j++) {
                space.get(i).add(new HashSet<>());
            }
        }

        placeParticles(particleList);
    }

    public void placeParticles(List<Particle> particleList){
        for (Particle particle : particleList) {
            int dstX = (int) (particle.getX_pos() / (this.L / this.M));
            int dstY = (int) (particle.getY_pos() / (this.L / this.M));

            this.space.get(dstX).get(dstY).add(particle);
        }
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < this.space.size(); i++) {
            for (int j = 0; j < this.space.get(i).size(); j++) {
                for (Particle particle: this.space.get(i).get(j)){
                    stringBuilder.append("Cell (").append(i).append(",").append(j).append(")");
                    stringBuilder.append(" -> ");
                    stringBuilder.append("Particle ").append(particle.getId()).append("(").append(particle.getX_pos()).append(",").append(particle.getY_pos()).append(")");
                    stringBuilder.append("\n");
                    count++;
                }
            }
        }

        stringBuilder.append("Size: " + count + "\n");

        return stringBuilder.toString();
    }

    public HashSet<Particle> getParticlesXY(final int x, final int y){
        return this.space.get(x).get(y);
    }

    public Double getRc() {
        return Rc;
    }

    public Integer getM() {
        return M;
    }

    public Double getL() {
        return L;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }
}





