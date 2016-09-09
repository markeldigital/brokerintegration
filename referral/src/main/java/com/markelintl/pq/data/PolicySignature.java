package com.markelintl.pq.data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class PolicySignature {
    private final Charset charset = StandardCharsets.UTF_8;
    private static final String ALGORITHM = "HmacSHA256";
    private final SecretKeySpec key;

    public PolicySignature(final String psk) {
        this.key = new SecretKeySpec(charset.encode(psk).array(), ALGORITHM);
    }

    public byte[] signPolicy(final long epoch,
                             final PolicyReference policy)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String s = payload(epoch, policy);

        final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(key);
        return mac.doFinal(charset.encode(s).array());
    }

    public static String payload(long epoch, PolicyReference policy) {
        StringBuilder sb = new StringBuilder();
        sb.append(policy);
        sb.append(epoch);
        return sb.toString();
    }

    public String signToBase64(final long epoch,
                               final PolicyReference policy)
            throws InvalidKeyException, NoSuchAlgorithmException {
        return DatatypeConverter.printBase64Binary(signPolicy(epoch, policy));
    }

    public boolean verifyFromBase64(final long epoch,
                                    final PolicyReference policy,
                                    final String signature)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final String expectedSignature = signToBase64(epoch, policy);
        return expectedSignature.equals(signature);
    }
}