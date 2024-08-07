package org.solareflare.project.BankSystemMangement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private Long senderBankId;
    private Long recipientBankId;
    private Long senderAccountId;
    private Long recipientAccountId;
    private Double amount;
}