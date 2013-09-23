package aussie.codechicken.lib.render;

import aussie.codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.Tessellator;

public interface IVertexModifier
{
    public void applyModifiers(CCModel m, Tessellator tess, Vector3 vec, UV uv, Vector3 normal, int i);

    public boolean needsNormals();
}
