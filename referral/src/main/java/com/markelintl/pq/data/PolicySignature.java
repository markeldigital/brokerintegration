package com.markelintl.pq.data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class PolicySignature {
    private final Charset charset = StandardCharsets.UTF_8;
    private final String ALGORITHM = "HmacSHA256";
    private final SecretKeySpec key;

    public PolicySignature(final String psk) {
        this.key = new SecretKeySpec(charset.encode(psk).array(), ALGORITHM);
    }

    public byte[] signPolicy(final long epoch, final PolicyReference policy) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder sb = new StringBuilder();
        sb.append(epoch);
        sb.append(policy);

        final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(key);
        return mac.doFinal(charset.encode(sb.toString()).array());
    }

    public String signToBase64(final long epoch, final PolicyReference policy) throws InvalidKeyException, NoSuchAlgorithmException {
        return DatatypeConverter.printBase64Binary(signPolicy(epoch, policy));
    }

    public boolean verifyFromBase64(final long epoch, final PolicyReference policy, final String s) throws NoSuchAlgorithmException, InvalidKeyException {
        final String sig = signToBase64(epoch, policy);
        return sig.equals(s);
    }
}