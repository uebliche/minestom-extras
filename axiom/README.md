# Axiom

Axiom support currently provides a compatibility extra for Minestom servers that want to opt into creator/world-edit workflows without adding client-specific protocol hacks.

## Status

Implemented as `AxiomExtra`:

- declares Axiom compatibility as server-authoritative world-edit capability metadata
- provides conservative limits for live block edit batches
- keeps all actual world mutations behind server-owned APIs instead of trusting client-side editor actions directly
- registers no custom plugin-message channels by default because Axiom primarily uses normal Minecraft interaction and block update behavior

## Usage

```java
ExtraRegistry registry = new ExtraRegistry();
AxiomExtra axiom = new AxiomExtra();
registry.registerHelper(axiom);

if (axiom.capability().liveWorldEditing()) {
    // Route creator tools through your own permission-checked edit service.
}
```

For production servers, apply region/permission checks before accepting any edit operation and broadcast block changes through Minestom's normal instance/block APIs.
