import simulation.SimulationOptions;

import java.util.ArrayList;
import java.util.List;

public class AreaInitializer {

    public static Area initializeArea(SimulationOptions options){
        List<Particle> particleList = new ArrayList<>();
        //First add big particle
        particleList.add(new Particle(options.getLength()/2, options.getLength()/2, options.getBigRadius(),
                0, 0, options.getBigMass(), true));
        //Add n initial particles
        int i=0;
        while(i < options.getN()){
            double ang = RandomUtils.randDouble(0, 2*Math.PI);
            double mod = RandomUtils.randDouble(0, options.getVelocityRange());
            double vx = mod * Math.cos(ang);
            double vy = mod * Math.sin(ang);
            Particle particle = new Particle(RandomUtils.randDouble(options.getLittleRadius(), options.getLength()-options.getLittleRadius()),
                    RandomUtils.randDouble(options.getLittleRadius(), options.getLength()-options.getLittleRadius()), options.getLittleRadius(),
                    vx, vy, options.getLittleMass(), false);
            if(particleList.stream().noneMatch((p) -> p.overlapsWith(particle))){
                particleList.add(particle);
                i++;
            }
        }
        return new Area(options.getLength(), particleList);


    }
}
