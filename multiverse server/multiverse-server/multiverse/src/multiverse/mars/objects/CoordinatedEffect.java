package multiverse.mars.objects;

import java.util.Map;
import java.io.*;
import java.util.HashMap;
import multiverse.server.engine.Engine;
import multiverse.mars.plugins.AnimationClient.InvokeEffectMessage;

/**
 * Use this class to configure and invoke coordinated effects scripts 
 * from the server.
 *
 */
public class CoordinatedEffect {
    public CoordinatedEffect(String effectName) {
	setEffectName(effectName);
    }

    /**
     * Invokes a client coordinated effect script, originating with the sourceOid object and targetted at the targetOid object.
     * @param sourceOid - Object ID of the object from which this coordinated effect originates.
     * @param targetOid - Object ID of the target object.
     * @return A CoordinatedEffect.State object that can be used to cancel or update the coordinated effect (not yet implemented).
     */
    public State invoke(Long sourceOid, Long targetOid) {
	State state = new State(sourceOid, targetOid);
	state.invoke();
	return state;
    }

    /**
     * Sets the name of the coordinated effect script to invoke.
     * @param effectName - Name of the coordinated effect script.
     */
    public void setEffectName(String effectName) { this.effectName = effectName; }
    
    /**
     * Get the name of the coordinated effect script to invoke.
     * @return - the name of the coordinated effect script invoked by this CoordinatedEffect..
     */
    public String getEffectName() { return effectName; }
    protected String effectName;

    /**
     * Adds an argument that will be passed to the effects script when it is invoked on the client.
     * @param argName - Name of the script argument.
     * @param argValue - Value of the argument. Must be one of: String, Boolean, Integer, Long, Float, Point, Quaternion.
     */
    public void putArgument(String argName, Serializable argValue) { argMap.put(argName, argValue); }
    
    /**
     * Get the value of an argument that will be passed to the effects script when it is invoked on the client.
     * @param argName - Name of the script argument.
     * @return - Value of the argument.
     */
    public Object getArgument(String argName) { return argMap.get(argName); }
    protected Map<String, Serializable> argMap = new HashMap<>();

    /**
     * Sets whether to send the sourceOid parameter to the client coordinated effect script.
     * @param val - Whether or not to send the sourceOid parameter to the client coordinated effect script.
     */
    public void sendSourceOid(boolean val) { sendSrcOid = val; }
    
    /**
     * Sets whether to send the sourceOid parameter to the client coordinated effect script.
     * @return whether or not to send the sourceOid parameter to the client coordinated effect script.
     */
    public boolean sendSourceOid() { return sendSrcOid; }
    
    protected boolean sendSrcOid = false;

    /**
     * Sets whether to send the targetOid parameter to the client coordinated effect script.
     * @param val - true if you want to send the taregetOid parameter to the client coordinated effect script. 
     */
    public void sendTargetOid(boolean val) { sendTargOid = val; }
    
    /**
     * Gets whether the targetOid parameter will be sent to the client coordinated effect script.
     * @return - true if you want to send the targetOid parameter to the client coordinated effect script; false otherwise.
     */
    public boolean sendTargetOid() { return sendTargOid; }
    protected boolean sendTargOid = false;

    class State {
	protected State(Long sourceOid, Long targetOid) {
	    this.sourceOid = sourceOid;
	    this.targetOid = targetOid;
	}

	protected Long effectOid = Engine.getOIDManager().getNextOid();
	protected Long sourceOid, targetOid;

	protected void invoke() {
	    InvokeEffectMessage msg = new InvokeEffectMessage(sourceOid, effectName);
	    if (sendSrcOid) {
		msg.setProperty("sourceOID", sourceOid);
	    }
	    if (sendTargOid) {
		msg.setProperty("targetOID", targetOid);
	    }
	    for (Map.Entry<String, Serializable> entry : argMap.entrySet()) {
		msg.setProperty(entry.getKey(), entry.getValue());
	    }
	    Engine.getAgent().sendBroadcast(msg);
	}
    }
}