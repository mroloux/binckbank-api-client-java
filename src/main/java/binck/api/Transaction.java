package binck.api;

import java.time.Instant;
import java.util.List;

public class Transaction {

    public String accountCurrency;
    public long number;
    public Instant transactionDate;
    public Instant settlementDate;
    public String mutationType;
    public double balanceMutation;
    public double mutatedBalance;
    public Instrument instrument;
    public double price;
    public double quantity;
    public String exchange;
    public double totalCosts;
    public String currency;
    public double netAmount;
    public double currencyRate;
    public List<TransactionCostComponent> transactionCostComponents;
}
