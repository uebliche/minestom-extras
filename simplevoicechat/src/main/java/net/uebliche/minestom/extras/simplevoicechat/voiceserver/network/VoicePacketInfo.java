package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VoicePacketInfo {

    int id() default -1;

    long ttl() default 10000L;

    int maxBufferSize() default 20;
}
