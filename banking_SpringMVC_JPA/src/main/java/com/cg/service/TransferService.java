package com.cg.service;

import com.cg.model.Transfer;
import com.cg.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransferService {
    @Autowired
    private ITransferRepository iTransferRepository;
    public void saveTransfer(Transfer transfer){
        iTransferRepository.save(transfer);
    }
}
