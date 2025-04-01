import net.uebliche.minstom.extras.common.ExtraRegistry;
import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExtraRegistryTest implements BeforeAllCallback, AfterAllCallback {

    static ExtraRegistry registry;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        MinecraftServer.init();
        registry = new ExtraRegistry();
        MinecraftServer.getGlobalEventHandler().addChild(registry.eventNode());

    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        MinecraftServer.stopCleanly();
    }
}
