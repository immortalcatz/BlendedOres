package com.tterrag.blendedOres;

import net.minecraftforge.common.MinecraftForge;

import com.tterrag.blendedOres.block.DummyBlock;
import com.tterrag.blendedOres.config.ConfigHandler;
import com.tterrag.blendedOres.event.TextureEvents;
import com.tterrag.blendedOres.lib.Reference;
import com.tterrag.blendedOres.proxy.CommonProxy;
import com.tterrag.blendedOres.render.OreRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BlendedOres {

	@Instance(Reference.MOD_ID)
	public static BlendedOres instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
	
	public static int renderID;
	
	public static DummyBlock block;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new TextureEvents());
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new OreRenderer());
		System.out.println("RenderID: " + renderID);
		proxy.initSounds();
		
		block = new DummyBlock(3324);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
