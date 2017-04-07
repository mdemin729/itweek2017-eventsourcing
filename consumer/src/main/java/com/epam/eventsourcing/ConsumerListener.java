package com.epam.eventsourcing;

import com.epam.eventsourcing.event.PersonEmailChanged;
import com.epam.eventsourcing.event.PersonEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class ConsumerListener {

    private ConcurrentMap<UUID, Integer> emailChangedCountMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "events")
    public void listen(PersonEvent event) {

        if (event.getClass().equals(PersonEmailChanged.class)) {
            emailChangedCountMap.merge(event.getId(), 1, (val1, val2) -> val1 + 1);
        }
    }

    public Map<Integer, Integer> stats() {

        Map<Integer, Integer> map = new HashMap<>();
        for (Integer emailChangedCount : emailChangedCountMap.values()) {
            map.merge(emailChangedCount, 1,
                    (val1, val2) -> val1 + 1);
        }
        return map;
    }
}
