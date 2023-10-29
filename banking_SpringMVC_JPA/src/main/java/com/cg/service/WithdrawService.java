package com.cg.service;

import com.cg.repository.ICustomerRepository;
import com.cg.repository.IWithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WithdrawService {
    @Autowired
    private IWithdrawRepository withdrawRepository;
}
