package main.java.engine.objects.tower;

import main.java.engine.EnvironmentKnowledge;

abstract class TowerBehaviorDecorator implements ITower {
    /**
     * The base tower will have behaviors added to it ("decorations")
     */
    protected ITower baseTower;  
    
    public TowerBehaviorDecorator (ITower baseTower) {
        this.baseTower = baseTower;
    }
    
    @Override
    public void callTowerActions (EnvironmentKnowledge environ) {
        baseTower.callTowerActions(environ);
    }
    
    @Override
    public boolean atInterval(int intervalFrequency) {
        return baseTower.atInterval(intervalFrequency);
    }
    
    @Override
    public double getXCoordinate(){
        return baseTower.getXCoordinate();
    }
    
    @Override
    public double getYCoordinate(){
        return baseTower.getYCoordinate();
    }
}
