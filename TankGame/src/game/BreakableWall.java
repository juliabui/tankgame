package TankGame.src.game;

import java.awt.image.BufferedImage;

public class BreakableWall extends Wall implements Wall.Breakable{
    private boolean breakable;

    public BreakableWall(int x, int y, BufferedImage wallImg, boolean breakable){
        super(x, y, wallImg);
        this.breakable = breakable;
    }

    @Override
    public boolean isBreakable(){
        return true;
    }
}
