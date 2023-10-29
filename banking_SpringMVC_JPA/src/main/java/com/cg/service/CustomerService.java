package com.cg.service;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private ICustomerRepository customerRepository;
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }
    public Customer findById(Long id){
        return customerRepository.getById(id);
    }
    public boolean checkOKCustomer(Customer customer){
        return !customer.getFullName().isEmpty() && !customer.getEmail().isEmpty() && !customer.getAddress().isEmpty() && !customer.getPhone().isEmpty();
    }
    public void createCustomer(Customer customer){
        customer.setBalance(BigDecimal.ZERO);
        customer.setDeleted(false);
        customerRepository.save(customer);
    }
    public void deleteCustomer(Long id){
         customerRepository.delete(customerRepository.getById(id));
    }
    public void updateCustomer(Customer customer){
        customerRepository.save(customer);
    }
    public void deposit(Deposit deposit){
        Customer customer = findById(deposit.getId());
        BigDecimal newBalance = customer.getBalance().add(deposit.getTransactionAmount());
        customer.setBalance(newBalance);
        customerRepository.save(customer);
    }
}
