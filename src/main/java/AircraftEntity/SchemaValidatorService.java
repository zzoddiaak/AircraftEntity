package AircraftEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class SchemaValidatorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void validateSchema() {
        validateTableCounts();
        validateConstraints();
        validateIndexes();
        validateKeyTablesHaveData();
    }

    private void validateTableCounts() {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = 'bookings'";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            System.out.println("✓ Total tables in bookings schema: " + count);
        } catch (Exception e) {
            System.err.println("✗ Error validating table counts: " + e.getMessage());
        }
    }

    private void validateConstraints() {
        try {
            String sql = "SELECT table_name, constraint_name, constraint_type " +
                    "FROM information_schema.table_constraints " +
                    "WHERE table_schema = 'bookings' " +
                    "ORDER BY table_name, constraint_type";

            jdbcTemplate.query(sql, (rs) -> {
                System.out.println("Constraint: " + rs.getString("table_name") +
                        " - " + rs.getString("constraint_name") +
                        " (" + rs.getString("constraint_type") + ")");
            });
        } catch (Exception e) {
            System.err.println("✗ Error validating constraints: " + e.getMessage());
        }
    }

    private void validateIndexes() {
        try {
            String sql = "SELECT tablename, indexname, indexdef " +
                    "FROM pg_indexes " +
                    "WHERE schemaname = 'bookings' " +
                    "ORDER BY tablename, indexname";

            jdbcTemplate.query(sql, (rs) -> {
                System.out.println("Index: " + rs.getString("tablename") +
                        " - " + rs.getString("indexname"));
            });
        } catch (Exception e) {
            System.err.println("✗ Error validating indexes: " + e.getMessage());
        }
    }

    private void validateKeyTablesHaveData() {
        String[] keyTables = {
                "bookings", "tickets", "flights",
                "aircrafts_data", "airports_data"
        };

        for (String table : keyTables) {
            try {
                String sql = "SELECT COUNT(*) FROM bookings." + table;
                Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
                System.out.println("✓ Table " + table + " has " + count + " records");
            } catch (Exception e) {
                System.err.println("✗ Error checking table " + table + ": " + e.getMessage());
            }
        }
    }

    public void validateTableStructure() {
        // Проверяем наличие основных таблиц
        Set<String> requiredTables = new HashSet<>(Arrays.asList(
                "aircrafts_data", "airports_data", "bookings",
                "flights", "tickets", "ticket_flights",
                "boarding_passes", "seats"
        ));

        try {
            String sql = "SELECT table_name FROM information_schema.tables " +
                    "WHERE table_schema = 'bookings'";

            Set<String> existingTables = new HashSet<>();
            jdbcTemplate.query(sql, (rs) -> {
                existingTables.add(rs.getString("table_name"));
            });

            // Проверяем отсутствующие таблицы
            requiredTables.removeAll(existingTables);
            if (requiredTables.isEmpty()) {
                System.out.println("✓ All required tables exist");
            } else {
                System.err.println("✗ Missing tables: " + requiredTables);
            }

        } catch (Exception e) {
            System.err.println("✗ Error validating table structure: " + e.getMessage());
        }
    }

    public void checkColumnTypes() {
        // Проверяем типы данных ключевых колонок
        String[][] columnChecks = {
                {"bookings", "book_ref", "character"},
                {"tickets", "ticket_no", "character"},
                {"flights", "flight_id", "integer"},
                {"aircrafts_data", "aircraft_code", "character"},
                {"airports_data", "airport_code", "character"}
        };

        for (String[] check : columnChecks) {
            try {
                String sql = "SELECT data_type FROM information_schema.columns " +
                        "WHERE table_schema = 'bookings' " +
                        "AND table_name = ? AND column_name = ?";

                String dataType = jdbcTemplate.queryForObject(sql, String.class, check[0], check[1]);
                if (dataType != null && dataType.startsWith(check[2])) {
                    System.out.println("✓ Column " + check[0] + "." + check[1] + " has correct type: " + dataType);
                } else {
                    System.err.println("✗ Column " + check[0] + "." + check[1] + " has wrong type: " + dataType);
                }
            } catch (Exception e) {
                System.err.println("✗ Error checking column " + check[0] + "." + check[1] + ": " + e.getMessage());
            }
        }
    }
}