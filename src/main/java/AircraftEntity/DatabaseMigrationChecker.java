package AircraftEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationChecker implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrationChecker.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkTablesCreated();
        checkTableStructure();
    }

    private void checkTablesCreated() {
        String[] expectedTables = {
                "aircrafts_data", "airports_data", "bookings",
                "flights", "tickets", "ticket_flights",
                "boarding_passes", "seats"
        };

        for (String table : expectedTables) {
            try {
                Long count = (Long) entityManager.createNativeQuery(
                                "SELECT COUNT(*) FROM information_schema.tables " +
                                        "WHERE table_schema = 'bookings' AND table_name = :tableName")
                        .setParameter("tableName", table)
                        .getSingleResult();

                if (count > 0) {
                    logger.info("✓ Table {} created successfully", table);
                } else {
                    logger.error("✗ Table {} not found", table);
                }
            } catch (Exception e) {
                logger.error("Error checking table {}: {}", table, e.getMessage());
            }
        }
    }

    private void checkTableStructure() {
        // Проверка структуры ключевых таблиц
        checkColumnExists("bookings", "book_ref", "character(6)");
        checkColumnExists("flights", "flight_id", "integer");
        checkColumnExists("tickets", "ticket_no", "character(13)");
    }

    private void checkColumnExists(String table, String column, String dataType) {
        try {
            Long count = (Long) entityManager.createNativeQuery(
                            "SELECT COUNT(*) FROM information_schema.columns " +
                                    "WHERE table_schema = 'bookings' AND table_name = :tableName " +
                                    "AND column_name = :columnName AND data_type LIKE :dataType")
                    .setParameter("tableName", table)
                    .setParameter("columnName", column)
                    .setParameter("dataType", dataType + "%")
                    .getSingleResult();

            if (count > 0) {
                logger.info("✓ Column {}.{} with type {} exists", table, column, dataType);
            } else {
                logger.error("✗ Column {}.{} with type {} not found", table, column, dataType);
            }
        } catch (Exception e) {
            logger.error("Error checking column {}.{}: {}", table, column, e.getMessage());
        }
    }
}
