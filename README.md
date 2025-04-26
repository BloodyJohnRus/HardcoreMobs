# HardcoreMobs
### New customizable plugin for your bosses!

# What does it provide you?
- Customizable name, health and type of boss!
- Own script system for better use!
- Customizable boss bar and entity equipment!
- Event script system!
- PlaceholderAPI support!
## Originally, this plugin was created for Paper 1.16.5. But I think it will work perfectly on 1.13 and above.

# How does it works?
### Entity configures only through config. You are one, who's writing your functionality for your boss.
### Bossbar progress is entity HP.
### Only one boss is available in one time. But you can spawn others.
### Each boss is independent.
### The structure of config looks like this:
```yaml
boss:
  displayName: "Test boss" # The display name of boss
  health: 200 # Health of boss
  type: ZOMBIE # Type of spawned entity
  spawnLocation: # Spawn location origin
    x: 0
    y: 100
    z: 0
    world: world
  bossBar: # Boss bar section, for better understanding look in https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BossBar.html
    title: "This is my boss!"
    color: RED 
    style: SOLID
    flags:
      - "CREATE_FOG"
      - "DARKEN_SKY"
    radius: 20
  equipment: # Optional. You can put one, or all slots. Refer to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/EquipmentSlot.html
    HEAD: # Here you must put ItemStack instance. Use other plugins config for this.
    CHEST:
    LEGS:
    FEET:
    HAND:
    OFF_HAND:
    BODY: #For horse and wolf,
    SADDLE: # Horse and pig.
  eventScripts: # And this part is interesting
    damage: # Activates when boss received damage
      - ""
    attack: # Activates when boss attacked player(Not triggered by [ATTACK])
      - ""
    death: # Activates when boss were killed
      - ""
  preSpawnScript: # Activates, when /hmobs spawn were executed
    - ""
  spawnScript: # Activates only when [SPAWN] was invoked
    - ""
```

# How the fuck I should write this shit ass script?
Calm down. Here's the guide of all commands:
```
[ATTACK] playerName damage - Attacks specific player. Good to use with "damage" event
[DIRECT] playerName text - Sends message to specific player. Useful, when you need privacy.
[SET] key value - Sets value in local placeholder system. Use %name% to use this value.
[CHECK] condition scriptLine - Checks for condition and executes script command. Only available !=(not equals) and ==(equals).
[CONSOLE] command - Executes command as console.
[BROADCAST] message - It's obvious.
[PLAYERS] radius command - executes command for players in radius. Use %player% to access player name.
[RANDOM_PLAYER] radius command - Same as PLAYERS, but selects one specific player.
[RANDOM] chance scriptLine - Randomly executes command. Good for surprises!
[MESSAGE] radius message - Sends message for all players in radius.
[ACTIONBAR] radius message - Same as MESSAGE, but sends in actionbar.
[SOUND] type volume pitch - Sends to each player sound. Look about sound type in Spigot wiki.
[SOUND_GLOBAL] type volume pitch - Same as SOUND, but plays sound from spawn location globally.
[PLAYERSOUND] radius sound volume pitch - Same as SOUND, but uses radius.
[COOLDOWN] radius material delay(ticks) - Adds to all players in radius item cooldown. No one will escape mighty boss!
[DELAY] ticks scriptLine - Runs script line after time.
[SPAWN] - Spawns entity and invokes "spawnScript".
[EFFECT] PotionEffectType duration amplifier - Adds effect to boss. Look about it on Spigot wiki.
[ANTIRELOG] radius - Activates player PvP cooldown in radius. Works only with 2 or more players in radius. Works only when AntiRelog installed.
[REPEAT] delay name - Same as while in coding. Runs to the moment of mob death, or when plugin were reloaded. Must ends with [REPEATEND].
[REPEATEND] - Obvious.
[REPEATBREAK] name - Stops REPEAT when you need.
[FOR] count delay - Runs block of code "count" amount of times with delay. Need to be closed with [FOREND].
[FOREND] - And again - obvious.
```
# Local placeholders
```
"damage" script section:
%attacker% - player who attacked boss
%damage% - count of dealt damage (type: double)

"death" script section:
%killer% - player who killed boss

Overall placeholders:
%name% - display name of boss
%center% - centers text. Put this on start of message. Works only in [BROADCAST], [MESSAGE], and [ACTIONBAR]
%player% - shows player name. Works only in [PLAYERS], [RANDOM_PLAYER], [MESSAGE], [ACTIONBAR].
%top1% - shows top 1 player in damage dealing to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%top2% - shows top 2 player in damage dealing to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%top3% - shows top 3 player in damage dealing to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%top1_damage% - shows top 1 dealt damage to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%top2_damage% - shows top 2 dealt damage to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%top3_damage% - shows top 3 dealt damage to boss. Shows "null" in [DIRECT], [SET], [CHECK], [ATTACK] if not set. In other commands doesn't replaced.
%your_key% - shows your value, seted through [SET] command.
```

# Important notes!
### I'd very recommend to not use nested [REPEAT]. Better is to run them separately.
### Also, you can't run [FOR] cycle in [REPEAT]. This is for safety of your performance.
### When you using PlaceholderAPI placeholder, plugin looks first for his placeholders, and after parses PlaceholderAPI.
### Values, set in one script section, won't work. For example:
```yaml
"death":
- "[SET] killed yes"
- "[BROADCAST] %center%%killed%" # Wrong!
```
```yaml
"attack":
  - "[SET] lastDamage %damage%"
  - "[SET] lastAttacker %attacker%"
"death":
  - "[BROADCAST] %center%%lastAttacker% killed boss, dealing %lastDamage% damage!" # Not perfect, but great example!
```

# How to install/configure:
1. Download .jar file.
2. Put your .jar file in "plugins" folder.
3. Create folder "HardcoreMobs", and in this folder, create "mobs.yml" file.
4. Edit your config, and enjoy!