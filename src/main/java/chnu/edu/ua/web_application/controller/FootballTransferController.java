package chnu.edu.ua.web_application.controller;

import chnu.edu.ua.web_application.audit.AuditService;
import chnu.edu.ua.web_application.model.FootballTransfer;
import chnu.edu.ua.web_application.service.FootballTransferService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class FootballTransferController {
    private final FootballTransferService footballTransferService;
    private final AuditService auditService;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public List<FootballTransfer> getAllTransfers() {
        return footballTransferService.getAllTransfers();
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{id}")
    public FootballTransfer getTransfer(@PathVariable String id) {
        return footballTransferService.getTransfer(id);
    }

    @RolesAllowed({"MANAGER", "ADMIN"})
    @PostMapping
    public FootballTransfer createTransfer(@RequestBody FootballTransfer transfer) {
        auditService.record("CREATE_TRANSFER", "id=" + transfer.getId());
        return footballTransferService.createTransfer(transfer);
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @PutMapping
    public FootballTransfer updateTransfer(@RequestBody FootballTransfer transfer) {
        auditService.record("UPDATE_TRANSFER", "id=" + transfer.getId());
        return footballTransferService.updateTransfer(transfer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTransfer(@PathVariable String id) {
        auditService.record("DELETE_TRANSFER", "id=" + id);
        footballTransferService.deleteTransfer(id);
    }
}