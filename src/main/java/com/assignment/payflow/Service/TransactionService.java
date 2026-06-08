package com.assignment.payflow.Service;

import com.assignment.payflow.Entity.Transactions;
import com.assignment.payflow.Entity.Users;
import com.assignment.payflow.Enums.TrxStatus;
import com.assignment.payflow.Enums.UserStatus;
import com.assignment.payflow.Repository.TransactionRepository;
import com.assignment.payflow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    // At startup, Spring scans the classpath for @Service and @Repository beans,
    // creates them in the application context, and injects these dependencies here automatically.
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
        if (fromUser == null || toUser == null) {
            trxs.setTrxStatus(TrxStatus.Failure);
            trxs.setErrorMessage("Transaction failed: user not found.");
            trxs.setErrorCode("USER_NOT_FOUND");
            return transactionRepository.save(trxs);
        }
        trxs.setFromUser(fromUser.getUserName());
        trxs.setToUser(toUser.getUserName());
        if(!isActive(fromUser) || !isActive(toUser)) {
            trxs.setTrxStatus(TrxStatus.Failure);
            trxs.setErrorMessage("Transaction failed due to inactive user account.");
            trxs.setErrorCode("INACTIVE_USER");
            return transactionRepository.save(trxs);
        }
        if(fromUser.getBalance() < amtToPay){
            trxs.setTrxStatus(TrxStatus.Failure);
            trxs.setErrorMessage("Transaction failed due to insufficient balance.");
            trxs.setErrorCode("INSUFFICIENT_BALANCE");
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
    private boolean isActive(Users u) {
        return u.getUserStatus() == UserStatus.A;
    }
}
