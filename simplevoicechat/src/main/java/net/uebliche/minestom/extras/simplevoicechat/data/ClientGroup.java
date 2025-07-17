package net.uebliche.minestom.extras.simplevoicechat.data;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ClientGroup(
        @NotNull UUID id,
        @NotNull String name,
        boolean passwordProtected,
        boolean persistent,
        boolean hidden,
        @NotNull GroupType type
) {

    public static final @NotNull NetworkBuffer.Type<ClientGroup> NETWORK_TYPE = NetworkBufferTemplate.template(
            NetworkBuffer.UUID, ClientGroup::id,
            NetworkBuffer.STRING, ClientGroup::name,
            NetworkBuffer.BOOLEAN, ClientGroup::passwordProtected,
            NetworkBuffer.BOOLEAN, ClientGroup::persistent,
            NetworkBuffer.BOOLEAN, ClientGroup::hidden,
            NetworkBuffer.Enum(GroupType.class), ClientGroup::type,
            ClientGroup::new
    );
}