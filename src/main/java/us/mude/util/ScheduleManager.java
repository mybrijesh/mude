package us.mude.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.mude.repository.MediaRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduleManager {

    @Autowired
    MediaRepository mediaRepo;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void updateComingSoonStatus() {
        logger.info("updateComingSoonStatus: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        mediaRepo.markAsReleased();
        mediaRepo.markAsComingSoon();
    }
}
