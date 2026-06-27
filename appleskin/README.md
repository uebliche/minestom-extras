# AppleSkin

AppleSkin support keeps the client HUD in sync with server-side hunger metadata.

## Status

Implemented as `AppleSkinExtra`:

- registers AppleSkin plugin-message packet types for saturation, exhaustion, and natural regeneration
- mirrors Minestom `UpdateHealthPacket` saturation values into the AppleSkin saturation channel
- exposes helper methods for manually sending exhaustion, saturation, and natural regeneration state when custom food systems update those values outside normal health packets

## Usage

```java
ExtraRegistry registry = new ExtraRegistry();
AppleSkinExtra appleSkin = new AppleSkinExtra();
registry.registerHelper(appleSkin);

appleSkin.sendExhaustion(player, 1.2f);
appleSkin.sendNaturalRegeneration(player, true);
```

Attach `registry.eventNode()` to the Minestom global event handler so outgoing health packets are observed and AppleSkin messages are emitted.
