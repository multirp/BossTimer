# BossTimer

BossTimer is a general Boss manager.  It will allow me to quickly create bosses, locations where bosses spawn, and create them as well as
create bossbars for them, handle timing mechanics for spawning them, etc.

Right now, the plugin is fairly far along, though only the Ender Dragon is currently summonable.

It is not config modifiable yet, but once I get into other bosses, I will likely make a main generic summon routine so that other bosses
can be added via config in order to be spawned.  At least that's the hope.

## Commands
The commands are pretty straightforward for this: `/bosstimer <mode> <action> <boss>` is the main command.  It should all be tab
completable as well.

### Modes
Right now there are two modes, `admin` and `help`.  Help brings up a general list of how to utilize the plugin, while admin actually
manipulates the spawning of bosses.

The current actions are `start` and `stop`.  These start and stop the respective bosses timers.  Start also spawns the boss.

The last one, boss, will be a list of bosses that you are able to summon, right now only `enderdragon`.

This would mean a full command to start the Ender Dragon's spawn routine would be:
`/bosstimer admin start enderdragon`

## Permissions
This plugin only provides one permission: `bosstimer.admin`.  Anyone with that permission is able to start and stop any boss timer
that is configured in the program, and provides access to the bosstimer help (since it's of no use to people that can't actually
spawn the bosses...)