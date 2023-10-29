package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.repository.ICustomerRepository;
import com.cg.service.CustomerService;
import com.cg.service.DepositService;
import com.cg.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private WithdrawService withdrawService;

    @GetMapping
    public String showList(Model model){
        model.addAttribute("customers", customerService.findAll());
        return "customer/list";
    }
    @GetMapping("/create")
    public String showCreate(Model model){
        model.addAttribute("customer", new Customer());
        return "customer/create";
    }
    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes){
        if(customerService.checkOKCustomer(customer)){
            customerService.createCustomer(customer);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", "Create Customer successfully");
            return "redirect:/customers";
        }else {
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("message", "Create unsuccessfully");
            return "redirect:/customers";
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes){
        customerService.deleteCustomer(id);
        redirectAttributes.addAttribute("success", true);
        redirectAttributes.addAttribute("message", "Delete Success");
        return "redirect:/customers";
    }
    @GetMapping("/edit/{id}")
    public String showEditCustomer(Model model, @PathVariable Long id){
        List<Customer> customers = customerService.findAll();
        Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id).findFirst().orElse(null);
        model.addAttribute("customerEdit", customer);
        return "customer/edit";
    }
    @PostMapping("/edit")
    public String editCustomer(RedirectAttributes redirectAttributes, @ModelAttribute Customer customer){
        if(customerService.checkOKCustomer(customer)){
            customerService.updateCustomer(customer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Update customer success");
            return "redirect:/customers";
        }else {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Update customer unsuccessful");
            return "redirect:/customers";
        }
    }
    @GetMapping("deposit/{id}")
    public String showDeposit(@PathVariable Long id, Model model){
        List<Customer> customers = customerService.findAll();
        Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id).findFirst().orElse(null);
        Deposit deposit = new Deposit();
        deposit.setCustomer(customer);
        model.addAttribute("deposit", deposit);
        return "customer/deposit";
    }
    @PostMapping("deposit")
    public String deposit(@ModelAttribute Deposit deposit,RedirectAttributes redirectAttributes){
        if(deposit.getTransactionAmount().compareTo(BigDecimal.ZERO) <=0){
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("message", "Deposit unsuccessfully");
            return "redirect:/customers";
        }else {
            customerService.deposit(deposit);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Deposit successfully");
            return "redirect:/customers";
        }

    }
}
