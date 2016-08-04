/*
*/
package com.teaminfinity.elementalinvocations.reference;

/**
 * QuikMod Default Reference Class.
 *
 */
public interface Reference {

	String MOD_ID = /*^${mod.id}^*/ "default";
	String MOD_NAME = /*^${mod.name}^*/ "Default Mod";
	String MOD_AUTHOR = /*^${mod.author}^*/ "Captain Nemo";

	String MOD_VERSION_MAJOR = /*^${mod.version_major}^*/ "0";
	String MOD_VERSION_MINOR = /*^${mod.version_minor}^*/ "0";
	String MOD_VERSION_PATCH = /*^${mod.version_patch}^*/ "0";
	String MOD_VERSION = /*^${mod.version}^*/ "0.0.0";
	
	String MOD_UPDATE_URL = /*^${mod.update_url}^*/ "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
	
	String CLIENT_PROXY_CLASS = "com.teaminfinity.elementalinvocations.proxy.ClientProxy";
    String SERVER_PROXY_CLASS = "com.teaminfinity.elementalinvocations.proxy.ServerProxy";
    String GUI_FACTORY_CLASS = "com.teaminfinity.elementalinvocations.gui.GuiFactory";

}
