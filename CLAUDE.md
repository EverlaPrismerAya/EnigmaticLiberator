# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EnigmaticLiberator is a Minecraft Forge mod for Minecraft 1.20.1 that makes Enigmatic Legacy's Cursed Ring fully configurable. It uses Mixin to inject into and modify the behavior of the CursedRing class from Enigmatic Legacy without replacing it.

**Key Dependencies:**
- EnigmaticLegacy 2.30.1 (the mod being extended)
- Curios API (for curio/accessory functionality)
- Caelus (for elytra functionality)
- enigmaticaddons 1.2.6
- Patchouli (documentation)

All mod dependencies are in `libs/` directory and loaded via `fg.deobf()` from a flat directory repository.

## Build Commands

```bash
# Build the mod jar
./gradlew build

# Run Minecraft client with the mod for testing
./gradlew runClient

# Run dedicated server
./gradlew runServer

# Generate IntelliJ IDEA run configurations
./gradlew genIntellijRuns

# Clean build artifacts
./gradlew clean

# Refresh dependencies if libraries are missing
./gradlew --refresh-dependencies
```

The built mod jar will be in `build/libs/` and is automatically reobfuscated for distribution.

## Architecture

### Mixin-Based Modification Pattern

This mod uses **Mixin** (SpongePowered) to modify Enigmatic Legacy's CursedRing behavior at runtime without source access. Mixins are configured in `src/main/resources/enigmatic_liberator.mixins.json`.

**Three Mixin Classes:**

1. **MixinCursedRing** (`mixin/MixinCursedRing.java`) - Server-side and common logic
   - Targets: `com.aizistral.enigmaticlegacy.items.CursedRing`
   - Injects into methods like `getLootingLevel`, `getFortuneLevel`, `getAttributeModifiers`, `curioTick`, `canUnequip`
   - Uses `@Inject`, `@ModifyVariable` to override curse and blessing effects
   - All injections use `remap = false` because EnigmaticLegacy uses SRG names

2. **MixinCursedRingTooltip** (`mixin/MixinCursedRingTooltip.java`) - Client-only
   - Modifies tooltip rendering based on configuration
   - Registered in `"client"` array in mixins.json

3. **MixinEnigmaticEventHandler** (`mixin/MixinEnigmaticEventHandler.java`)
   - Injects into Enigmatic Legacy's event handlers to modify curse behaviors
   - Handles curses that are implemented via Forge events rather than CursedRing methods

### Configuration System

Three separate TOML config files via ForgeConfigSpec (all in `config/` directory):

1. **enigmatic-liberator-curses.toml** - Controls the 7 curses + extras
   - Each curse has an `enabled` boolean toggle
   - Each curse has configurable intensity parameters
   - Special option: `disable_eternal_binding` allows ring removal in survival

2. **enigmatic-liberator-blessings.toml** - Controls beneficial effects
   - Fortune and Looting bonuses can be toggled or adjusted

3. **enigmatic-liberator-extra.toml** - Additional features
   - Extra curse configurations (knockback vulnerability, etc.)

Config classes are in `config/` package: `CurseConfig.java`, `BlessingConfig.java`, `ExtraConfig.java`.

### Mixin Injection Points

When modifying curse/blessing behaviors:
- **For methods with return values**: Use `@Inject(at = @At("RETURN"), cancellable = true)` and `CallbackInfoReturnable`
- **For void methods**: Use `@Inject(at = @At("HEAD"), cancellable = true)` and `CallbackInfo` to cancel early
- **For modifying local variables**: Use `@ModifyVariable` with proper ordinals and names
- Always set `remap = false` in the annotation since EnigmaticLegacy is obfuscated

### Package Structure

```
net.everla.enigmaticliberator/
├── EnigmaticLiberator.java      # Main mod class (@Mod annotation)
├── config/                       # ForgeConfigSpec configurations
│   ├── BlessingConfig.java
│   ├── CurseConfig.java
│   └── ExtraConfig.java
├── gui/                          # Configuration screen (optional)
│   ├── ConfigScreen.java
│   └── ConfigScreenFactory.java
└── mixin/                        # Mixin injection classes
    ├── MixinCursedRing.java           # Modifies CursedRing item behavior
    ├── MixinCursedRingTooltip.java    # Client-side tooltip modifications
    ├── MixinEnigmaticEventHandler.java # Event handler modifications
    └── MixinPacketEnderRingKey.java   # Intercepts Ender Chest access packets
```

## Development Workflow

### Adding New Configurable Curses

1. Add config entries to appropriate config class (`CurseConfig`, `BlessingConfig`, or `ExtraConfig`)
2. Identify the target method in `com.aizistral.enigmaticlegacy.items.CursedRing` or event handlers
3. Create mixin injection in the appropriate mixin class
4. Use config values to conditionally modify behavior
5. First complete static verification of the source, target bytecode signatures, configuration wiring, and injection points; only then perform one necessary runtime verification with `./gradlew runClient`

When choosing an implementation mechanism:

- For behavior owned by or specific to Enigmatic Legacy, prefer Mixin after verifying the target class, method name, descriptor, overload, inheritance, and runtime mapping.
- For behavior primarily owned by vanilla Minecraft or Forge, try the appropriate Forge event first. Use Mixin only when no suitable event exists or when the event cannot provide the required timing or scope.

### Debugging Mixins

- Mixin refmap is generated at `build/resources/main/enigmatic_liberator.refmap.json`
- Set `"verbose": true` in `enigmatic_liberator.mixins.json` for detailed mixin application logs
- Check logs in `run/logs/` for mixin application status
- If mixin fails to apply, verify target method signature matches exactly (use SRG names, not MCP)

### Mixin Mapping and Distribution

- This project loads local mod dependencies through `fg.deobf(...)`, but the dependency JARs and runtime classes are exposed with Forge/SRG names in the development run configuration.
- Mixin targets that use `remap = false` must use the actual SRG method name. For Minecraft 1.20.1 item tooltips, use `m_7373_`, not `appendHoverText`.
- A target such as `appendHoverText` with `remap = false` can appear correct in source or IDE inspection but will fail in a production Forge environment with `InvalidInjectionException` because the target method is actually `m_7373_`.
- The final mod JAR must contain `enigmatic_liberator.mixins.json` and `enigmatic_liberator.refmap.json`, and its Manifest must contain `MixinConfigs: enigmatic_liberator.mixins.json`. The `jar` task explicitly adds this Manifest entry because its generated Manifest overrides `src/main/resources/META-INF/MANIFEST.MF`.
- Test environments and real game environments can expose different class names, mappings, method descriptors, overload resolution, dependency versions, and transformed bytecode. Do not treat a successful ForgeGradle test run as proof that a production JAR will load; inspect both development and production-facing mappings and signatures when a Mixin is involved.
- After changing Mixin targets, first use static inspection to verify the target class, method name, descriptor, overload, inheritance behavior, development/production mapping behavior, and refmap expectations. Once that review is complete, run `./gradlew clean build` and, when runtime verification is necessary, run `./gradlew runClient` once; confirm the log reports `Mixing <MixinClass> from enigmatic_liberator.mixins.json into <TargetClass>` and contains no `InvalidInjectionException` or `MixinApplyError`.

### Verification Discipline

- Static verification comes first: inspect relevant source code, dependency bytecode, method descriptors, overloads, inheritance, configuration registration, network synchronization, and Mixin registration before launching Minecraft.
- Always account for differences between the ForgeGradle test environment and the real game environment, including mappings, remapped names, method descriptors, overloads, dependency versions, and class transformations.
- Perform only one necessary runtime verification after static verification is complete. Do not repeatedly start the client for exploratory debugging when the target and injection behavior can be established statically.
- If runtime verification fails, fix the identified issue through static analysis before starting another runtime verification.

### Starter Ring Settings

- Enigmatic Legacy implements `UltraHardcore` and `AutoEquip` in `EnigmaticEventHandler`, not in `CursedRing` itself.
- `grantStarterGear(ServerPlayer)` reads `CursedRing.ultraHardcore` when granting the first Cursed Ring: false puts it in the inventory, true attempts to force-equip it.
- `onPlayerTick(LivingTickEvent)` scans the player's inventory and reads `CursedRing.autoEquip`; when true, a non-creative/non-spectator player holding a Cursed Ring without one already equipped is passed to `SuperpositionHandler.tryForceEquip`.
- `MixinEnigmaticEventHandlerConfig` redirects only these two original `BooleanParameter.getValue()` calls to `ExtraConfig.ULTRA_HARDCORE` and `ExtraConfig.AUTO_EQUIP`, preserving the original equipment and fallback behavior.

### Testing

Run the client and create a test world:
```bash
./gradlew runClient
```

The mod loads EnigmaticLegacy and its dependencies from `libs/`, so the Cursed Ring will be available in creative inventory under the Enigmatic Legacy tab.

## Important Notes

- **Java 17 Required**: Minecraft 1.20.1 uses Java 17
- **Mixin Compatibility Level**: Set to JAVA_17 in mixins.json
- **No Source Access**: This mod works entirely through runtime bytecode injection - we don't have EnigmaticLegacy source
- **SRG Names**: All mixin targets use `remap = false` because mod JARs use SRG (obfuscated) names
- **Config Location**: Generated config files appear in `run/config/` during testing, `config/` in production
- **Annotation Processor**: Mixin annotation processor is configured in build.gradle with proper SRG file paths
- **Deprecation Warnings**: `FMLJavaModLoadingContext.get()` and `ModLoadingContext.get()` are marked as deprecated in Forge 1.20.1 but still functional - suppressed with `@SuppressWarnings("removal")`

## Blessing System Implementation

The Cursed Ring inherits functionality from the Ender Ring, meaning it can access Ender Chest anywhere. This blessing can be disabled:

- **Ender Ring Blessing**: Controlled via `MixinPacketEnderRingKey` which intercepts `PacketEnderRingKey.handle()`
- When `ENDER_RING_ENABLED = false`, the mixin cancels the packet if player has Cursed Ring but not actual Ender Ring
- This allows players with actual Ender Ring to still use the feature while blocking Cursed Ring users
