package adventurebackpack.common;

import net.minecraft.util.ResourceLocation;
import adventurebackpack.ModInformation;

public class Textures {

	public static final String TEXTURE_LOCATION = ModInformation.ID.toLowerCase();

	public static String resourceString(String name) {
		return new ResourceLocation(TEXTURE_LOCATION, name).toString();

	}

	public static ResourceLocation resourceRL(String name) {
		return new ResourceLocation(TEXTURE_LOCATION, name);
	}
}
