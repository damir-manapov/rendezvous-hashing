package ru.damirmanapov;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class RendezvousHashingTest {

    @Test
    public void testDefineDestiantion() {

//      Распределяем 10 объектов по 3 нодам,  добавляем 4. Проверяем что если объект поменял расположение
//      то новая нода 4. Убираем 2 ноду, если нода изменила расположение, то старая была второй.

        String nodeId1 = "1";
        String nodeId2 = "2";
        String nodeId3 = "3";

        Map<String, String> destination = new HashMap<>();
        Set<String> nodeIds = new HashSet<>();
        nodeIds.add(nodeId1);
        nodeIds.add(nodeId2);
        nodeIds.add(nodeId3);
        Set<String> objectKeys = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            objectKeys.add(String.valueOf(i));
        }

        // Dispersing between 3 nodes
        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            destination.put(objectId, RendezvousHashing.defineDestination(objectId, nodeIds));
        }

        String nodeId4 = "4";

        nodeIds.add(nodeId4);

        Map<String, String> newDestination = new HashMap<>();

        // Dispersing between 4 nodes
        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            newDestination.put(objectId, RendezvousHashing.defineDestination(objectId, nodeIds));
        }

        // Check object moved only to new node
        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            if (!destination.get(objectId).equals(newDestination.get(objectId))) {
                assertThat(newDestination.get(objectId), is(nodeId4));
            }
        }

        nodeIds.remove(nodeId2);

        Map<String, String> destinationRemovedSecond = new HashMap<>();

        // Dispersing between nodes after removing second node
        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            destinationRemovedSecond.put(objectId, RendezvousHashing.defineDestination(objectId, nodeIds));
        }

        // Check after removing second node only its objects moved
        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            if (!newDestination.get(objectId).equals(destinationRemovedSecond.get(objectId))) {
                assertThat(newDestination.get(objectId), is(nodeId2));
            }
        }

    }

}