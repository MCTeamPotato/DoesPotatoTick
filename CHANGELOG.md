# 5.5.0
- Rewrite the entire mod... again.
- Generally with faster tickability check, less lag spike, higher tps and lower used memory.
- We no longer force-cancel the entire entity tick but update the "canUpdate" flag that forge provides for entities so that compatibility with mods would hopefully be better.
- Remove unnecessary config options and add new.
- Force the entire tickability check run on the server thread.
- And other trillions of optimizations and changes I failed to remember
- Take a look at our brand-new introduction at the main page!