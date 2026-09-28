package chnu.edu.ua.web_application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FootballTransfer {
    private String id;
    private String playerName;
    private String fromClub;
    private String toClub;
    private double transferFee;

    public FootballTransfer(String playerName, String fromClub, String toClub, double transferFee) {
        this.playerName = playerName;
        this.fromClub = fromClub;
        this.toClub = toClub;
        this.transferFee = transferFee;
    }
}