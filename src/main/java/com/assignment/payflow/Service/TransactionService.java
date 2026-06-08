package com.assignment.payflow.Service;

import com.assignment.payflow.Entity.Transactions;
import com.assignment.payflow.Entity.Users;
import com.assignment.payflow.Enums.TrxStatus;
import com.assignment.payflow.Repository.TransactionRepository;
import com.assignment.payflow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public Transactions addTrx(Transactions trxs) {
        double amtToPay = trxs.getAmount();
        Users fromUser = userService.getUser(null,trxs.getFromUpiId());
        Users toUser = userService.getUser(null,trxs.getToUpiId());
        if(fromUser.getBalance() < amtToPay){
            trxs.setTrxStatus(TrxStatus.Failure);
        }
        else{
            trxs.setTrxStatus(TrxStatus.Success);
            fromUser.setBalance(fromUser.getBalance() - amtToPay);
            toUser.setBalance(toUser.getBalance() + amtToPay);
            userRepository.save(fromUser);
            userRepository.save(toUser);
        }
        Transactions trxDone = transactionRepository.save(trxs);
        return trxDone;
    }
}
