package de.longor.talecraft.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;

public class ServerHandler {

	public static void handleSNBTCommand(NetHandlerPlayServer serverHandler, StringNBTCommand cmd) {
		EntityPlayerMP playerEntity = serverHandler.playerEntity;
		WorldServer worldServer = playerEntity.getServerForPlayer();
		handleSNBTCommand(playerEntity, worldServer, cmd);
	}
	
	private static void handleSNBTCommand(EntityPlayerMP player, World world, StringNBTCommand command) {
		if(world.isRemote){
			System.out.println("FATAL ERROR: ServerHandler method was called on client-side!");
			return;
		}
		
		if(command.command.startsWith("blockdatamerge:")) {
			String positionString = command.command.substring(15);
			String[] posStrings = positionString.split(" ");
			BlockPos position = new BlockPos(Integer.valueOf(posStrings[0]), Integer.valueOf(posStrings[1]), Integer.valueOf(posStrings[2]));
			
			TileEntity entity = world.getTileEntity(position);
			
			if(entity != null) {
				TaleCraft.logger.info("(datamerge) " + position + " -> " + command.data);
				mergeTileEntityData(entity, command.data);
			}
		}
	}
	
	private static void mergeTileEntityData(TileEntity entity, NBTTagCompound data) {
		BlockPos blockpos = entity.getPos();
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeToNBT(compound);
		
		compound.merge(data);
		compound.setInteger("x", blockpos.getX());
		compound.setInteger("y", blockpos.getY());
		compound.setInteger("z", blockpos.getZ());
		
		entity.readFromNBT(compound);
		entity.markDirty();
		entity.getWorld().markBlockForUpdate(blockpos);
	}
	
	public static void handleEntityJoin(World world, Entity entity) {
		
		if(entity instanceof EntityPlayerMP) {
			System.out.println("Sending extended EntityData to player: " + entity);
	    	TaleCraft.simpleNetworkWrapper.sendTo(new PlayerNBTDataMerge(entity.getEntityData()), (EntityPlayerMP) entity);
		}
		
	}
	
}