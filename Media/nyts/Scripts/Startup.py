import ClientAPI
import Input

# Most worlds will want this for the client side animations,
# though it should probably be customized
import ClientAnimations

# This sets up the character creation and selection
import nytsCharacterCreation

##
## Import any world specific files here
##
## import ToggleShadows
import WorldInit
import AnimationEffects

# this contains the quest api
import MarsQuest
# this contains the container api
import MarsContainer
# this contains the cursor api
import MarsCursor
# this contains the action api
import MarsAction
# this contains the unit api
import MarsUnit
# this contains the ability api
import MarsAbility
# this contains the (limited) target api
import MarsTarget
# this contains the (limited) trade api
import MarsTrade

# this contains a system to handle commands on the client
import MarsCommand
# this contains code to create ui events for some network messages
import MarsEvent
# this contains typical game world commands and their handlers
import MarsStandardCommands

# Make Client available to Bindings.xml
from Multiverse.Base import Client

import Sounds

# this is to activate VOIP
# import mvVoice

# Write to the log file (trace.txt) that the Startup script has finished
ClientAPI.Log("Startup.py loaded")
