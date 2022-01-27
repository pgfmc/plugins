# plugins
Has all of the plugins used on the PGF server.
## Core
Implements core (heh heh) functionality for the PGF server.
Contains some simple commands, a custom perms manager, and multiple small frameworks.
### Commands
- **/Broadcast \<message\>** Broadcasts a raw message to all players online. Can only be used by Admins.
- **/Skull \<player\>** Gives the sender a skull of the input player. can only be used by Admins.
- **/tag \<player\> \<path\> \<data\> \<save \{true/false\}\>** Saves input <data> to the player's Playerdata. if <save> is true, saves the tag to file.
- **/nick \<nickname/reset\>** Sets the sender's nickname to input String. if **/nick reset** is used, the sender's nickname is reset.
