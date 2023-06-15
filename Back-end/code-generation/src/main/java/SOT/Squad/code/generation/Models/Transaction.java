package SOT.Squad.code.generation.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"Transactions\"")
public class Transaction {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    private double amount;
    private String bankAccountFrom;
    private String bankAccountTo;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<AccountType> accountTypeFrom;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<AccountType> accountTypeTo;
    private String paymentReference;
    private LocalDateTime date;
    @OneToOne
    private User PerformedByUser;
}
