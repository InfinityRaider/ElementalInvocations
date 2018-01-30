/*
*/
package com.infinityraider.elementalinvocations.reference;

/**
 * QuikMod Default Reference Class.
 *
 */
public interface Reference {

	String MOD_ID = /*^${mod.id}^*/ "elemental_invocations";

	String MOD_NAME = /*^${mod.name}^*/ "Elemental Invocations";
	String MOD_AUTHOR = /*^${mod.author}^*/ "InfinityRaider";

	String MOD_VERSION_MAJOR = /*^${mod.version_major}^*/ "0";
	String MOD_VERSION_MINOR = /*^${mod.version_minor}^*/ "1";
	String MOD_VERSION_PATCH = /*^${mod.version_patch}^*/ "0";
	String MOD_VERSION = /*^${mod.version}^*/ MOD_VERSION_MAJOR + "." + MOD_VERSION_MINOR + "." + MOD_VERSION_PATCH;
	
	String MOD_UPDATE_URL = /*^${mod.update_url}^*/ "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
	
	String CLIENT_PROXY_CLASS = "com.infinityraider.elementalinvocations.proxy.ClientProxy";
    String SERVER_PROXY_CLASS = "com.infinityraider.elementalinvocations.proxy.ServerProxy";
    String GUI_FACTORY_CLASS = "com.infinityraider.elementalinvocations.gui.GuiFactory";

}
