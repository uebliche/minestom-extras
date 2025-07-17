package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public final class AES {

    private static final Logger log = LoggerFactory.getLogger(AES.class);

    private AES() {
    }

    private static final @NotNull Random RANDOM = new SecureRandom();
    private static final @NotNull String CIPHER = "AES/CBC/PKCS5Padding";
    private static final int UUID_LENGTH = 16;

    public static byte @NotNull [] getBytesFromUuid(@NotNull UUID uuid) {
        NetworkBuffer buffer = NetworkBuffer.staticBuffer(UUID_LENGTH);
        buffer.write(NetworkBuffer.UUID, uuid);
        byte[] bytes = new byte[UUID_LENGTH];
        buffer.copyTo(0, bytes, 0, UUID_LENGTH);
        return bytes;
    }

    private static byte @NotNull [] generateIv() {
        byte[] iv = new byte[UUID_LENGTH];
        RANDOM.nextBytes(iv);
        return iv;
    }

    private static @NotNull SecretKeySpec createKey(@NotNull UUID secret) {
        return new SecretKeySpec(getBytesFromUuid(secret), "AES");
    }

    public static Optional<byte[]> encrypt(@NotNull UUID secret, byte @NotNull[] data) {
        try {
            byte[] iv = generateIv();
            IvParameterSpec spec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, createKey(secret), spec);

            byte[] encrypted = cipher.doFinal(data);
            byte[] result = new byte[iv.length + encrypted.length];

            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            return Optional.of(result);
        } catch (Exception e) {
            log.warn("Failed to encrypt data", e);
            return Optional.empty();
        }
    }

    public static Optional<byte @NotNull []> decrypt(@NotNull UUID secret, byte @NotNull [] result) {
        try {
            byte[] iv = new byte[UUID_LENGTH];
            System.arraycopy(result, 0, iv, 0, iv.length);

            byte[] data = new byte[result.length - iv.length];
            System.arraycopy(result, iv.length, data, 0, data.length);

            IvParameterSpec spec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, createKey(secret), spec);
            byte[] decrypted = cipher.doFinal(data);
            return Optional.of(decrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 InvalidAlgorithmParameterException | BadPaddingException e) {
            log.warn("Failed to decrypt data", e);
            return Optional.empty();
        }
    }

}
