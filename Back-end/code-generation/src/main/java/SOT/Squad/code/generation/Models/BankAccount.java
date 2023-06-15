package SOT.Squad.code.generation.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"BankAccounts\"")
public class BankAccount {
    @Id
    @GeneratedValue
    private long id;
    private String iban;
    private double balance;
    private long userId;
    private boolean disabled;
    private String currencies;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<AccountType> accountType;
    private int absoluutLimit;
}
