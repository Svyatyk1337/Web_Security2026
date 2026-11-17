package chnu.edu.ua.web_application.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    /** All audit notes, optionally filtered by ?username=. ADMIN only. */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuditNote> getAll(@RequestParam(required = false) String username) {
        return username == null ? auditService.findAll() : auditService.findByUsername(username);
    }

    /** Own audit notes of the current user. Any authenticated user. */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public List<AuditNote> getMine(Authentication authentication) {
        return auditService.findByUsername(authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public AuditNote getOne(@PathVariable long id) {
        return auditService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Audit note not found"));
    }
}
