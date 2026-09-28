package chnu.edu.ua.web_application.controller;

import chnu.edu.ua.web_application.model.FootballTransfer;
import chnu.edu.ua.web_application.service.FootballTransferService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<FootballTransfer> getAllTransfers() {
        return footballTransferService.getAllTransfers();
    }

    @GetMapping("/{id}")
    public FootballTransfer getTransfer(@PathVariable String id) {
        return footballTransferService.getTransfer(id);
    }

    @PostMapping
    public FootballTransfer createTransfer(@RequestBody FootballTransfer transfer) {
        return footballTransferService.createTransfer(transfer);
    }

    @PutMapping
    public FootballTransfer updateTransfer(@RequestBody FootballTransfer transfer) {
        return footballTransferService.updateTransfer(transfer);
    }

    @DeleteMapping("/{id}")
    public void deleteTransfer(@PathVariable String id) {
        footballTransferService.deleteTransfer(id);
    }
}