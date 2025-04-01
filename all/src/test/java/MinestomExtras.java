import net.uebliche.minestom.extras.appleskin.AppleSkinHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ExtraRegistryTest.class)
public class MinestomExtras extends ExtraRegistryTest {

    @Test
    void registerAll() {
        registry.registerHelper(new AppleSkinHelper());
    }

}
