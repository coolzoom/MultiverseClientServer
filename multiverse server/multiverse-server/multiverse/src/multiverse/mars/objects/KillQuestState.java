package multiverse.mars.objects;

import multiverse.server.util.*;
import multiverse.server.engine.*;
import multiverse.mars.plugins.QuestClient;
import multiverse.msgsys.*;

import java.io.*;
import java.util.*;

public class KillQuestState extends QuestState {
    public KillQuestState() {
    }

    public KillQuestState(MarsQuest quest,
                          Long playerOid) {
        super(quest, playerOid);
    }
    
    /**
     * private method to recreate the lock when deserializing
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        setupTransient();
    }
    
    /**
     * returns the current state of this quest, ie, how many mobs to kill,etc
     * @return 
     */
    @Override
    public String toString() {
        return "KillQuest '" + getName() + "': you have killed " +
            currentKillCount() + " out of " +
            targetKillCount() +
            " " +
            getKillGoal();
    }

    @Override
    public void activate() {
        log.debug("in activate");
        // subscribe for some messages
        SubjectFilter filter = new SubjectFilter(getPlayerOid());
        filter.addType(QuestClient.MSG_TYPE_CONCLUDE_QUEST);
//        filter.add(CombatInfo.COMBAT_PROP_DEADSTATE);
        sub = Engine.getAgent().createSubscription(filter, this);
        log.debug("QuestPlugin activated");
    }
    
    @Override
    public void deactivate() {
        if (Log.loggingDebug)
            log.debug("CollectionQuestState.deactivate: playerOid=" + getPlayerOid() + " questRef=" + getQuestRef());
        if (sub != null) {
            Engine.getAgent().removeSubscription(sub);
            sub = null;
        }
    }
    
    /**
     * process network messages
     * @param msg
     * @param flags
     */
    @Override
    public void handleMessage(Message msg, int flags) {
//        if (msg instanceof InventoryClient.InvUpdateMessage) {
//            return processInvUpdate((InventoryClient.InvUpdateMessage) msg);
//        }
//        else if (msg instanceof QuestClient.ConcludeMessage) {
//            return processConcludeQuest((QuestClient.ConcludeMessage) msg);
//        }
//        else {
//            log.error("unknown msg: " + msg);
//        }
        //return true;
    }
    
    /**
     * for client display: current state
     * @return 
     */
    @Override
    public List<String> getObjectiveStatus() {
        lock.lock();
        try {
            List<String> l = new LinkedList<>();
            String obj = getKillGoal() + ": " + 
                currentKillCount() + "/" + targetKillCount();
            l.add(obj);
            return l;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean handleConclude() {
	super.handleConclude();

//	// send a remove quest state event to player
//	Event removeQuest = new RemoveQuestResponse(this);
//	this.getPlayer().sendEvent(removeQuest);
	return true;
    } 

    /**
     * called when a mob is killed that the player is getting credit for
     * @param mobKilled
     */
    @Override
    public void handleDeath(MarsMob mobKilled) {
        if (getCompleted()) {
            return;
        }

//        MarsMob player = getPlayer();
//        String targetMobName = goal.getName();
//	if (mobKilled.getName().equals(targetMobName)) {
//            Log.debug("KillQuestState: mob name matched");
//	    currentCount++;
//            player.sendServerInfo("You have killed " +
//                                  currentCount + 
//                                  " out of " +
//                                  targetKillCount());
//            Log.debug("KillQuestState.handleDeath: sending state update to player");
//            Event questStateInfo = new QuestStateInfo(player, this);
//            player.sendEvent(questStateInfo);
//	}
//        else {
//            Log.debug("KillQuestState: mob name '" + mobKilled.getName() + 
//                      "' did not match '" + targetMobName + "'");
//        }
//	if (currentCount >= goal.getCount()) {
//	    setCompleted(true);
//
//            player.sendServerInfo("You have completed the quest " + getName());
//
//            // send a QuestCompleted event to mobs that perceive us
//            // so that if one of them can accept this quest for completion,
//            // it can update its state
//            QuestCompleted completeEvent = new QuestCompleted(player, 
//                                                              getQuest());
//
//            Engine.getMessageServer().sendToPerceivers(player,
//                                                       completeEvent,
//                                                       RDPMessageServer.NON_USERS);
//	}
    }

    public void setKillGoal(String mobName, int count) {
	this.goal = new MarsKillQuest.KillGoal(mobName, count);
    }
    public void setKillGoal(MarsKillQuest.KillGoal kg) {
        this.goal = kg;
    }
    public MarsKillQuest.KillGoal getKillGoal() {
        return goal;
    }
    public int targetKillCount() {
	return goal.getCount();
    }
    public int currentKillCount() {
	return currentCount;
    }

    public void giveReward() {
        lock.lock();
        try {
//            getPlayer().sendServerInfo("You have completed the quest " +
//                                       getName());
//            
//            if (getCashReward() > 0) {
//                // give the player some cash
//            }
//            throw new RuntimeException("KillQuestState: need to give out reward");
        }
        finally {
            lock.unlock();
        }
    }

    private MarsKillQuest.KillGoal goal = null;
    private int currentCount = 0;
    private static final Logger log = new Logger("KillQuestState");
    transient Long sub = null;
    private static final long serialVersionUID = 1L;
}
