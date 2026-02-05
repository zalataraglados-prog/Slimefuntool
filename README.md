# AutoSlimefunUnlock

A Paper/Purpur plugin that automatically unlocks all Slimefun researches for first-time players.

## Behavior
- Triggers only on a player's first join (`hasPlayedBefore == false`).
- Waits 3 minutes, then runs as console:
  - `sf research <player> all`
- Skips if the player is offline at execution time.

## Build
- Requires Java 17+
- `mvn -DskipTests package`

## Install
- Put the jar in your server's `plugins/` folder.
- Ensure `Slimefun` is installed.

## License
Unlicense (public domain).
