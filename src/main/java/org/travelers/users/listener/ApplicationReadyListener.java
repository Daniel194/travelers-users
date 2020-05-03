package org.travelers.users.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.travelers.users.service.UserConsumerService;

@Component
public class ApplicationReadyListener {

    private final Logger log = LoggerFactory.getLogger(ApplicationReadyListener.class);

    private final UserConsumerService userConsumerService;

    @Autowired
    public ApplicationReadyListener(UserConsumerService userConsumerService) {
        this.userConsumerService = userConsumerService;
    }

    @Async("taskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void startCreateNewUser() {
        log.info("START create-new-user");

        while (true) {
            userConsumerService.consumeCreateNewUser();
        }
    }

    @Async("taskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void startUpdateUser() {
        log.info("START update-user");

        while (true) {
            userConsumerService.consumeUpdateUser();
        }
    }

    @Async("taskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void startAddCountry() {
        log.info("START add-country");

        while (true) {
            userConsumerService.consumeAddCountry();
        }
    }

    @Async("taskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void startRemoveCountry() {
        log.info("START remove-country");

        while (true) {
            userConsumerService.consumeRemoveCountry();
        }
    }

    @Async("taskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void startDeleteUser() {
        log.info("START delete-user");

        while (true) {
            userConsumerService.consumeDeleteUser();
        }
    }

}

