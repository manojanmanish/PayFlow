package com.assignment.payflow.Controller;

import com.assignment.payflow.DTOs.TrxResponse;
import com.assignment.payflow.Entity.Transactions;
import com.assignment.payflow.Enums.TrxStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.payflow.Service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> addTrxDetails(@RequestBody Transactions trxs){
        Transactions trxDone = transactionService.addTrx(trxs);
        String message = "";
        if(trxDone==null){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TrxResponse(null, "Transaction could not be saved due to unforeseen circumstances."));
        }
        else if(trxDone.getTrxStatus() == TrxStatus.Failure){
            message = "Transaction failed due to insufficient balance.";
        }
        else{
            message = "Transaction has been successfully processed.";
        }
        TrxResponse trxResponse = new TrxResponse(trxDone,message);
        return new ResponseEntity<>(trxResponse, HttpStatus.CREATED);
    }
}
