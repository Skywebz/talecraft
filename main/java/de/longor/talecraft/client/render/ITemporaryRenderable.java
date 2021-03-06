package de.longor.talecraft.client.render;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ITemporaryRenderable {
	
	boolean canRemove();
	
	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, WorldRenderer worldrenderer, double partialTicks);
	
}
