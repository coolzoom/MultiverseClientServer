package multiverse.mars.events;

import multiverse.server.engine.*;
import multiverse.server.network.*;
import multiverse.server.util.*;
import multiverse.mars.core.*;
import java.util.*;
import java.util.concurrent.locks.*;

// Provide information about a specific ability

public class AbilityInfoEvent extends Event {
    public AbilityInfoEvent() {
	super();
    }

    public AbilityInfoEvent(MVByteBuffer buf, ClientConnection con) {
	super(buf,con);
    }

    public AbilityInfoEvent(MarsAbility ability) {
	super();
	setAbilityName(ability.getName());
	setIcon(ability.getIcon());
	setDesc("");
	for (String cooldownID : ability.getCooldownMap().keySet()) {
	    addCooldown(cooldownID);
	}
	setProperty("targetType", ability.getTargetType().toString());
	setProperty("minRange", Integer.toString(ability.getMinRange()));
	setProperty("maxRange", Integer.toString(ability.getMaxRange()));
	setProperty("costProp", ability.getCostProperty());
        setProperty("cost", Integer.toString(ability.getActivationCost()));
    }

    @Override
    public String getName() {
	return "AbilityInfoEvent";
    }

    @Override
    public MVByteBuffer toBytes() {
	int msgId = Engine.getEventServer().getEventID(this.getClass());
	MVByteBuffer buf = new MVByteBuffer(400);

        lock.lock();
        try {
	    buf.putInt(-1); // dummy PlayerID
	    buf.putInt(msgId);
	
	    buf.putString(abilityName);
	    buf.putString(icon);
	    buf.putString(desc);

            int size = cooldowns.size();
            buf.putInt(size);
            for(String cooldown : cooldowns) {
                buf.putString(cooldown);
            }
	    size = props.size();
	    buf.putInt(size);
	    for(Map.Entry<String, String> entry : props.entrySet()) {
		buf.putString(entry.getKey());
		buf.putString(entry.getValue());
	    }
        }
        finally {
            lock.unlock();
        }

	buf.flip();
	return buf;
    }

    @Override
    protected void parseBytes(MVByteBuffer buf) {
        lock.lock();
        try {
	    buf.rewind();

	    buf.getInt(); // dummy playerID
	    /* int msgId = */ buf.getInt();

	    setAbilityName(buf.getString());
	    setIcon(buf.getString());
	    setDesc(buf.getString());

            int size = buf.getInt();
            cooldowns = new HashSet<>(size);
            while (size-- > 0) {
                String cooldown = buf.getString();
		cooldowns.add(cooldown);
            }
	    size = buf.getInt();
	    props = new HashMap<>(size);
	    while (size-- > 0) {
		String key = buf.getString();
		String value = buf.getString();
		setProperty(key, value);
	    }
        }
        finally {
            lock.unlock();
        }
    }

    public String getAbilityName() { return abilityName; }
    public void setAbilityName(String abilityName) { this.abilityName = abilityName; }
    protected String abilityName;

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    protected String icon;

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    protected String desc;

    public void addCooldown(String cooldownID) {
	lock.lock();
	try {
	    if (cooldowns == null) {
		cooldowns = new HashSet<>();
	    }
	    cooldowns.add(cooldownID);
	}
	finally {
	    lock.unlock();
	}
    }
    public Set<String> getCooldowns() {
	lock.lock();
	try {
	    return new HashSet<>(cooldowns);
	}
	finally {
	    lock.unlock();
	}
    }
    protected Set<String> cooldowns = null;

    public String getProperty(String key) { return props.get(key); }
    public void setProperty(String key, String value) {
	lock.lock();
	try {
	    if (props == null) {
		props = new HashMap<>();
	    }
	    props.put(key, value);
	}
	finally {
	    lock.unlock();
	}
    }
    protected Map<String, String> props = null;

    transient Lock lock = LockFactory.makeLock("AbilityInfoEvent");
}
