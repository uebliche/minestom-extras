package net.uebliche.minstom.extras.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to mark a class as a packet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketInfo {
    /**
     * The id of the packet.
     *
     * @return the id of the packet
     */
    String channel();
}
