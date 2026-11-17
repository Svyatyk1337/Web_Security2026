package chnu.edu.ua.web_application.audit;

import java.time.Instant;

public record AuditNote(long id, Instant timestamp, String username, String action, String details) {}
