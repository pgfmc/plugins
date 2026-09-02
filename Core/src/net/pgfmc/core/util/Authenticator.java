package net.pgfmc.core.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator {

    private static final String ALGORITHM = "HmacSHA256";

    final static String key = getKey();

    public static byte[] construct(byte[] data) {
        try {
            // 1. Create a SecretKeySpec object from the secret key bytes
            final SecretKeySpec secretKeySpec = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8), 
                ALGORITHM
            );

            // 2. Get a Mac instance for the specific algorithm
            final Mac mac = Mac.getInstance(ALGORITHM);

            // 3. Initialize the Mac instance with your secret key
            mac.init(secretKeySpec);

            // 4. Compute the HMAC raw bytes
            final byte[] hmac = mac.doFinal(data);

            return hmac;
        }

        return null;
    }

    public static byte[] deconstruct(byte[] payload) {

    }

    private static String getKey() {
        return ""; // TODO get key from file
    }

}
