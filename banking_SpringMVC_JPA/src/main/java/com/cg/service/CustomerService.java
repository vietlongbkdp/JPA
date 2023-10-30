package com.cg.service;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private ICustomerRepository customerRepository;
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }
    public Customer getByEmail(String email){
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().filter(customer -> customer.getEmail().equals(email)).findFirst().orElse(null);
    }
    public Customer findById(Long id){
        return customerRepository.findById(id).orElse(null);
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
         customerRepository.delete(Objects.requireNonNull(customerRepository.findById(id).orElse(null)));
    }
    public void updateCustomer(Customer customer){
        customerRepository.save(customer);
    }
    public void deposit(Deposit deposit){
        Customer customer = getByEmail(deposit.getCustomer().getEmail());
//        Customer customer = findById(deposit.getId());
        BigDecimal newBalance = customer.getBalance().add(deposit.getTransactionAmount());
        customer.setBalance(newBalance);
        customerRepository.save(customer);
    }
    public void withdraw(Withdraw withdraw){
        Customer customer = getByEmail(withdraw.getCustomer().getEmail());
        BigDecimal newBalance = customer.getBalance().subtract(withdraw.getTransactionAmount());
        customer.setBalance(newBalance);
        customerRepository.save(customer);
    }

    public List<Customer> findAllWithout(Long id) {
        return findAll().stream().filter(customer -> customer.getId()!= id).collect(Collectors.toList());
    }
}
