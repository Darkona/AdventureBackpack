package aussie.codechicken.lib.lighting;

import aussie.codechicken.lib.vec.BlockCoord;
import net.minecraft.world.IBlockAccess;

public class LazyLightMatrix
{
    private BlockCoord pos = new BlockCoord();
    private IBlockAccess access;
    private boolean computed = false;
    private LightMatrix lightMatrix = new LightMatrix();
    
    public LightMatrix lightMatrix()
    {
        if(computed) 
            return lightMatrix;
        computed = true;
        lightMatrix.computeAt(access, pos.x, pos.y, pos.z);
        return lightMatrix;
    }
    
    public void setPos(IBlockAccess access, int x, int y, int z)
    {
        computed = false;
        this.access = access;
        pos.set(x, y, z);
    }
}