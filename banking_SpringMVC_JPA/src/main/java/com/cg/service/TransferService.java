package com.cg.service;

import com.cg.model.Transfer;
import com.cg.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransferService {
    @Autowired
    private ITransferRepository iTransferRepository;
    public void saveTransfer(Transfer transfer){
        iTransferRepository.save(transfer);
    }
    public List<Transfer> findAllTransfer(){
        return iTransferRepository.findAll();
    }
    public void deleteTransfer(Long id){
        iTransferRepository.deleteById(id);
    }
}
