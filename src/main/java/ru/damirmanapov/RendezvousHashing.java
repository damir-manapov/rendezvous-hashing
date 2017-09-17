package ru.damirmanapov;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RendezvousHashing {

    public static String defineDestination(String key, Set<String> nodeIds) {

        Map<String, byte[]> nodeHashes = new HashMap<>();

        for (String nodeIs : nodeIds) {
            nodeHashes.put(nodeIs, SHA3_256.hash(nodeIs.getBytes(StandardCharsets.UTF_8)));
        }

        Map<String, byte[]> destinationHashes = new HashMap<>();

        for (String nodeIs : nodeIds) {
            String concatenation = key + nodeIs;
            destinationHashes.put(nodeIs, SHA3_256.hash(concatenation.getBytes(StandardCharsets.UTF_8)));
        }

        for (int i = 0; i < 32; i++) {

            byte maxByte = Byte.MIN_VALUE;

            for (Map.Entry<String, byte[]> destinationHash : destinationHashes.entrySet()) {
                maxByte = (byte) Math.max(maxByte, destinationHash.getValue()[i]);
            }

            Iterator<Map.Entry<String, byte[]>> iter = destinationHashes.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, byte[]> entry = iter.next();
                if (entry.getValue()[i] < maxByte) {
                    iter.remove();
                }
            }

        }

        if (destinationHashes.size() == 0) {
            throw new RuntimeException("Something went wrong");
        }

        return destinationHashes.entrySet().iterator().next().getKey();
    }

}
