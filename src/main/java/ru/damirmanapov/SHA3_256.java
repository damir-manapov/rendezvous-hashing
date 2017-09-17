package ru.damirmanapov;

import org.bouncycastle.jcajce.provider.digest.SHA3;

public class SHA3_256 {

    public static byte[] hash(byte[] input) {

        final SHA3.Digest256 digest = new SHA3.Digest256();
        digest.update(input);
        return digest.digest();

    }

}
