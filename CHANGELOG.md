# 5.5.0
- Rewrite the entire mod... again.
- Generally with faster tickability check, less lag spike and lower used memory.
- We no longer force-cancel the entire entity tick but update the "canUpdate" flag that forge provides for entities so that compatibility with mods would hopefully be better.
- Remove unnecessary config options and add new.
- Force the entire tickability check run on the server thread.