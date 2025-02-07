package multiverse.mars.events;

import multiverse.mars.objects.*;
import multiverse.server.engine.*;
import multiverse.server.objects.*;
import multiverse.server.network.*;
import multiverse.server.util.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class QuestInfo extends Event {
    public QuestInfo() {
	super();
    }

    public QuestInfo(MVByteBuffer buf, ClientConnection con) {
	super(buf, con);
    }

//    public QuestInfo(MarsMob player, 
//		     MarsMob questNpc,
//		     MarsQuest quest) {
//	super(player);
//	setQuestNpcOid(questNpc.getOid());
//	setTitle(quest.getName());
//	setDesc(quest.getDesc());
//	setObjective(quest.getObjective());
//        setQuestId(quest.getOid());
//
//        List<ItemTemplate> rewardTempls = quest.getRewards();
//        if ((rewardTempls == null) || (rewardTempls.isEmpty())) {
//            Log.debug("QuestInfo: rewardtemplate is null for quest " +
//                      getTitle());
//        }
//        else {
//            List<Reward> rewards = new LinkedList<Reward>();
//            for (ItemTemplate itemTempl : rewardTempls) {
//                Reward reward = new Reward(itemTempl.getName(),
//                                           itemTempl.getIcon(),
//                                           1);
//                rewards.add(reward);
//                setRewards(rewards);
//            }
//        }
//    }

    @Override
    public String toString() {
	try {
	    return "[Event=QuestInfo: player=" +
		getObjectOid() +
		",npc=" + getQuestNpc().getName() +
		",questId=" + getQuestId() +
		",title=" + getTitle() +
		",desc=" + getDesc() +
		",objective=" + getObjective() +
		"]";
	}
	catch(MVRuntimeException e) {
	    throw new RuntimeException("questinfo.tostring", e);
	}
    }

    @Override
    public String getName() {
	return "QuestInfo";
    }

    public MarsMob getQuestNpc() {
	return MarsMob.convert(MVObject.getObject(questNpcOid));
    }
    public Long getQuestNpcOid() {
	return this.questNpcOid;
    }
    public void setQuestNpcOid(Long questNpcOid) {
	this.questNpcOid = questNpcOid;
    }


    public void setTitle(String s) {
	this.title = s;
    }
    public String getTitle() {
	return title;
    }
    String title = null;

    public void setDesc(String s) {
	this.desc = s;
    }
    public String getDesc() {
	return desc;
    }
    String desc = null;

    public void setObjective(String s) {
	this.objective = s;
    }
    public String getObjective() {
	return objective;
    }
    String objective = null;

    public void setQuestId(long id) {
        this.questId = id;
    }
    public long getQuestId() {
        return questId;
    }
    long questId = -1;

    public void setRewards(List<Reward> rewards) {
        lock.lock();
        try {
            this.rewards = new LinkedList<>(rewards);
        }
        finally {
            lock.unlock();
        }
    }
    public List<Reward> getRewards() {
        lock.lock();
        try {
            return new LinkedList<>(rewards);
        }
        finally {
            lock.unlock();
        }
    }

    public static class Reward {
        public Reward(String name, String icon, int count) {
            this.name = name;
            this.icon = icon;
            this.count = count;
        }
        public String name = null;
        public String icon = null;
        public int count = 0;
    }

    @Override
    public MVByteBuffer toBytes() {
	int msgId = Engine.getEventServer().getEventID(this.getClass());

	MVByteBuffer buf = new MVByteBuffer(500);
	buf.putLong(getObjectOid()); 
	buf.putInt(msgId);
	buf.putLong(getQuestNpc().getOid());
        buf.putLong(getQuestId());
	buf.putString(getTitle());
	buf.putString(getDesc());
	buf.putString(getObjective());

        lock.lock();
        try {
            if (rewards == null) {
                buf.putInt(0);
            }
            else {
                int size = rewards.size();
                buf.putInt(size);
                Iterator<Reward> iter = rewards.iterator();
                while(iter.hasNext()) {
                    Reward reward = iter.next();
                    buf.putString(reward.name);
                    buf.putString(reward.icon);
                    buf.putInt(reward.count);
                }
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
	buf.rewind();
	long playerId = buf.getLong();
	setObjectOid(playerId);
	/* int msgId = */ buf.getInt();
	long questNpcId = buf.getLong();
	setQuestNpcOid(questNpcId);
        setQuestId(buf.getLong());
	setTitle(buf.getString());
	setDesc(buf.getString());
	setObjective(buf.getString());

        lock.lock();
        try {
            this.rewards = new LinkedList<>();
            int size = buf.getInt(); // num rewards
            while (size > 0) {
                String name = buf.getString();
                String icon = buf.getString();
                int count = buf.getInt();
                Reward reward = new Reward(name, icon, count);
                rewards.add(reward);
                size--;
            }
        }
        finally {
            lock.unlock();
        }
    }

    List<Reward> rewards = null;
    protected Long questNpcOid = null;
    transient Lock lock = LockFactory.makeLock("QuestInfoLock");
}
