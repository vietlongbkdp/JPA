package com.cg.service;

import com.cg.repository.ICustomerRepository;
import com.cg.repository.IDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepositService {
    @Autowired
    private IDepositRepository depositRepository;
}
