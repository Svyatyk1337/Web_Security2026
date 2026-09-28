package chnu.edu.ua.web_application.service;

/*
  @author   Sviatoslav Stratii
  @project   web_security26
  @class  FootballTransferService
  @version  1.0.0 
  @since 02/09/2026 - 21.10
*/

import chnu.edu.ua.web_application.model.FootballTransfer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FootballTransferService {
    private final List<FootballTransfer> transfers = new ArrayList<>();

    {
        transfers.add(new FootballTransfer("1", "Kylian Mbappe", "Paris Saint-Germain", "Real Madrid", 0));
        transfers.add(new FootballTransfer("2", "Jude Bellingham", "Borussia Dortmund", "Real Madrid", 103000000));
        transfers.add(new FootballTransfer("3", "Erling Haaland", "Borussia Dortmund", "Manchester City", 60000000));
    }

    public List<FootballTransfer> getAllTransfers() {
        return transfers;
    }

    public FootballTransfer createTransfer(FootballTransfer transfer) {
        transfers.add(transfer);
        return transfer;
    }

    public FootballTransfer getTransfer(String id) {
        return transfers.stream()
                .filter(transfer -> transfer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public FootballTransfer updateTransfer(FootballTransfer transfer) {
        FootballTransfer oldTransfer = getTransfer(transfer.getId());
        if (oldTransfer != null) {
            transfers.remove(oldTransfer);
        }
        transfers.add(transfer);
        return transfer;
    }

    public void deleteTransfer(String id) {
        FootballTransfer transfer = getTransfer(id);
        if (transfer != null) {
            transfers.remove(transfer);
        }
    }
}