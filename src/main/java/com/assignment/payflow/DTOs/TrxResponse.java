package com.assignment.payflow.DTOs;

import com.assignment.payflow.Entity.Transactions;

public class TrxResponse {
   private int trxId;
   private String message;
   public TrxResponse(Transactions trxs, String message){
       this.trxId = trxs.getTrxId();
       this.message = message;
   }
}
