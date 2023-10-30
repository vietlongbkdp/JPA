package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.CustomerService;
import com.cg.service.DepositService;
import com.cg.service.TransferService;
import com.cg.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Autowired
    private TransferService transferService;
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
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Deposit unsuccessfully");
            return "redirect:/customers";
        }else {
            customerService.deposit(deposit);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Deposit successfully");
            return "redirect:/customers";
        }
    }
    @GetMapping("/withdraw/{id}")
    public String showWithdraw(@PathVariable Long id, Model model){
        List<Customer> customers = customerService.findAll();
        Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id).findFirst().orElse(null);
        Withdraw withdraw = new Withdraw();
        withdraw.setCustomer(customer);
        model.addAttribute("withdraw", withdraw);
        return "/customer/withdraw";
    }
    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute Withdraw withdraw, RedirectAttributes redirectAttributes){
        Customer customer = customerService.getByEmail(withdraw.getCustomer().getEmail());
        if(withdraw.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0|| withdraw.getTransactionAmount().compareTo(customer.getBalance()) > 0){
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Withdraw unsuccessfully");
            return "redirect:/customers";
        }else {
            customerService.withdraw(withdraw);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Withdraw successfully");
            return "redirect:/customers";
        }
    }
    @GetMapping("/transfer/{id}")
    public String showTransfer(@PathVariable Long id, Model model){
        List<Customer> customers = customerService.findAll();
        Customer sender = customers.stream().filter(customer1 -> customer1.getId() == id).findFirst().orElse(null);
        List<Customer> recipients = customerService.findAllWithout(id);
        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setFees(10L);
        model.addAttribute("transfer", transfer);
        model.addAttribute("recipients", recipients);
        return "customer/transfer";
    }
    @PostMapping("/transfer")
    public String transfer(@ModelAttribute Transfer transfer, RedirectAttributes redirectAttributes, @RequestParam Long recipientId){
        Customer sender = customerService.getByEmail(transfer.getSender().getEmail());
        Customer recipient = customerService.findById(recipientId);
        if(transfer.getTransactionAmount().compareTo(BigDecimal.ZERO) <=0 || transfer.getTransactionAmount().compareTo(sender.getBalance()) >0){
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Transfer Unsuccessfully");
            return "redirect:/customers";
        }else {
            BigDecimal newSenderBalance = sender.getBalance().subtract(transfer.getTransactionAmount());
            sender.setBalance(newSenderBalance);
            customerService.updateCustomer(sender);
            BigDecimal newRecipient = recipient.getBalance().add(transfer.getTransferAmount());
            recipient.setBalance(newRecipient);
            customerService.updateCustomer(recipient);
            transfer.setRecipient(recipient);
            transfer.setSender(sender);
            transfer.setFees(10L);
            transfer.setDeleted(false);
            transfer.setTimeTransfer(LocalDateTime.now());
            transfer.setFeesAmount(transfer.getTransferAmount().multiply(BigDecimal.valueOf(0.1)));
            transfer.setTransactionAmount(transfer.getTransferAmount().multiply(BigDecimal.valueOf(1.1)));
            transferService.saveTransfer(transfer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Transfer Successfully");
            return "redirect:/customers";
        }
    }
    @GetMapping("/transfer_history")
    public String showTransferHistory(Model model){
        model.addAttribute("transfers", transferService.findAllTransfer());
        return "customer/transfer_history";
    }
    @GetMapping("/delete_history/{id}")
    public String deleteTransferHistory(@PathVariable Long id,  RedirectAttributes redirectAttributes){
        transferService.deleteTransfer(id);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Delete success");
        return "redirect:/customers/transfer_history";
    }
}
