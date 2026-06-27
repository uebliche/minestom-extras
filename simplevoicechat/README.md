# Simple Voice Chat

Simple Voice Chat support provides the plugin-message and UDP voice-server bridge pieces needed by Simple Voice Chat clients.

## Status

Implemented as `SimpleVoiceChatExtra`:

- registers Simple Voice Chat clientbound and serverbound plugin-message packet types
- handles secret negotiation via `RequestSecretPacket`
- starts a lightweight voice server through `VoiceServer.Builder`
- tracks per-player voice state, secrets, groups, socket addresses, and keepalive metadata with Minestom tags

## Usage

Register it with the shared `ExtraRegistry` during server startup:

```java
ExtraRegistry registry = new ExtraRegistry();
registry.registerHelper(new SimpleVoiceChatExtra());
```

Attach `registry.eventNode()` to the Minestom global event handler so plugin messages are decoded and dispatched.
