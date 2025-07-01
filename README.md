[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5bc5707682204db49ec1721cc0c5ca43)](https://app.codacy.com/gh/winterhavenmc/WorldManagerLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/5bc5707682204db49ec1721cc0c5ca43)](https://app.codacy.com/gh/winterhavenmc/WorldManagerLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Spigot Version](https://badgen.net/static/spigot-api/1.21.7?color=yellow)](https://spigotmc.org)
&nbsp;[![License](https://badgen.net/static/license/GPLv3)](https://www.gnu.org/licenses/gpl-3.0)

# WorldManagerLib

**WorldManagerLib** is a lightweight utility library for Bukkit plugin developers. It enables plugin functionality to be selectively enabled or disabled on a per-world basis using simple configuration settings. It also provides utilities for world name resolution and spawn location handling, with support for [Multiverse-Core](https://mvplugins.org).

---

## 🌍 Features

- ✅ Enable or disable plugin functionality on a per world basis.  

- 🔧 Simple method provided for checking world enabled status  

- 🌐 Multiverse-Core integration  
  If [Multiverse-Core](https://github.com/Multiverse/Multiverse-Core) (version 4.x or 5.x) is installed:
    - Supports alias resolution for world names.
    - Fetches Multiverse-defined spawn locations.

---

## 🔧 How It Works

### Configuration

Add `enabled-worlds` and/or `disabled-worlds` to your plugin’s `config.yml`:

```yaml
enabled-worlds:
  - world
  - world_nether

disabled-worlds:
  - creative_world
  - mini_games_world
```

If the enabled-worlds list is empty, all server worlds shall be added to the internal tracking list of enabled worlds.
Conversely, if the enabled-worlds config list contains any worlds, only worlds with a matching name will be added 
to the internal tracking list.
Then, any worlds listed in the disabled-worlds configuration setting will be be removed from the internal tracking list
of enabled worlds.

In this way, the server operator is given the opportunity to have all worlds, including newly created worlds,
enabled by default, with exceptions listed in the disabled-worlds setting. Or, they may adopt a default disabled stance,
by listing only specifically enabled worlds in the enabled-worlds setting. Then, any new worlds that may be created later
will have the status of disabled by default.

Non-existent worlds in either list will be ignored.
