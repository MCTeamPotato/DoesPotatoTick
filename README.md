# **DoesPotatoTick — Overview**

**DoesPotatoTick** improves performance by suspending (“freezing”) distant entities that do not need to update every tick. Through configurable rules and safety checks, it reduces server CPU load and may also increase client FPS while preserving normal gameplay behavior.

---

## **1. Purpose**

Minecraft updates every entity 20 times per second, which can overload servers with large worlds, many players, or heavy modpacks. Vanilla settings cannot distinguish important entities from irrelevant ones.
DoesPotatoTick introduces a controlled system that updates only what is necessary.

---

## **2. Core Mechanism**

Each entity is assigned a *tickable* state. The server evaluates this state every tick based on location, category, and context; the client optionally skips rendering non-tickable entities.

### **2.1 Distance-Based Activation**

* Players define an **active region** (configurable radius in chunks).
* Entities outside this region become candidates for suspension.
* Regions update only when a player moves between chunks, logs in, or changes dimensions.

### **2.2 Dimension and Claim Support**

* Optimization can be restricted to selected dimensions.
* Entities in claimed areas (FTB Chunks, OpenPartiesAndClaims) are always ticked.
* Players are notified if no claim mod is installed.

### **2.3 Entity Exceptions**

Certain entities never freeze:

* Players, falling blocks, vanilla bosses
* A broad list of modded bosses and mechanical systems
* Entire mods can be whitelisted
  The list reloads automatically on configuration changes.

### **2.4 Filtering Rules**

Configurable rules allow ignoring or including:

* Hostile mobs, animals, raiders
* Dropped items
* Projectiles (with optional logic to force-hit targets to tick)
  Dying entities are allowed to complete their death sequence.

### **2.5 Mob Farm Detection**

Periodically scans chunk densities; if a farm-like pattern is detected, affected entities are forced to tick to ensure farms continue functioning.

### **2.6 Raid Handling**

During active raids, optimization can be disabled or adjusted according to configuration.

---

## **3. Client Features**

### **3.1 Render Skipping**

Non-tickable entities may be hidden to improve FPS.
Drawing a bow temporarily re-enables rendering to avoid aiming issues.

### **3.2 Embeddium Integration**

Adds a dedicated configuration screen when Embeddium is installed.

### **3.3 Synchronization**

A lightweight packet informs the client which entities should be displayed.

---

## **4. Configuration**

* **doespotatotick-common.toml**: core server behavior
* **doespotatotick-client.toml**: rendering controls
* Defaults prioritize safety and compatibility.
* All logic runs on the server thread; changes apply consistently across ticks and reloads.

---

## **5. Make DPT Better**
When you:
* Feel that an specific entity type should never stop ticking
* Have a config option request
* Encounter unexpected situation because of entities' ticking freezing

Don't hesitate to contact me by [GitHub Issues](https://github.com/MCTeamPotato/DoesPotatoTick/issues) or email `670067575@qq.com`

---

## **6. Design Considerations**

The mod aims to:

* Reduce server load without altering gameplay
* Preserve boss fights, farms, and modded mechanics
* Remain compatible with existing mod ecosystems

Its behavior resembles a controlled, context-aware scheduling system rather than a simple “disable distant entities” toggle.

---

## **7. A Bit of History & Evolution**

DoesPotatoTick didn’t start as the polished mod you see today.

It began life as a fork of **Txni’s DoesItTick**, created purely as a stop-gap compatibility fix for the Lithium/RoadRunner ecosystem. The original version was, frankly, quite crude: only a handful of config options, and every single entity, on every single tick, looped through **all online players** to check “is anyone close enough?”  
That’s O(entities × players) per tick — on a 20k-entity server with 50 players, that alone added **millions** of unnecessary iterations per second. Pure brutality.

The old `dpt$checkAlwaysTick()` was equally painful: it ran expensive config and tag checks on every entity every tick, with zero caching.  
No client-side rendering fixes either — frozen entities would just stutter and “ghost” in place, looking broken.

Everything changed when we updated the mod:

- Replaced the naïve player loop with the current **PlayerTracker**: a dimension → chunk → Y-range spatial map, updated lazily only on movement. Complexity dropped from O(n×m) to essentially O(players) per update.
- `alwaysTick` flags are now computed once at entity-type registration and cached forever.
- Added proper network packets and client-side culling (plus the bow-drawing safety valve) so frozen entities simply vanish cleanly instead of glitching.
- Mob-farm detection, raid awareness, claim-mod integration, Embeddium GUI… none of these existed back then.

Today’s DoesPotatoTick is no longer “just another entity culling mod.” It’s the result of multiple full refactors and an obsession with doing it right — without ever breaking your bosses, farms, or Create contraptions.

Thanks to Txni for the original spark, and to every tester and contributor, as well as Grok and ChatGPT for helping me design, write and polish this description (I'm just feeling dead when writing readme by myself lol).

---

Enjoy the buttery-smooth ticks (or lack thereof)!
