package chnu.edu.ua.web_application.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuditService {
    private final List<AuditNote> notes = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    /** Records a note for the user of the current security context. */
    public AuditNote record(String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return recordFor(auth == null ? "anonymous" : auth.getName(), action, details);
    }

    public AuditNote recordFor(String username, String action, String details) {
        AuditNote note = new AuditNote(sequence.incrementAndGet(), Instant.now(), username, action, details);
        notes.add(note);
        return note;
    }

    public List<AuditNote> findAll() {
        return List.copyOf(notes);
    }

    public Optional<AuditNote> findById(long id) {
        return notes.stream().filter(n -> n.id() == id).findFirst();
    }

    public List<AuditNote> findByUsername(String username) {
        return notes.stream().filter(n -> n.username().equals(username)).toList();
    }
}
