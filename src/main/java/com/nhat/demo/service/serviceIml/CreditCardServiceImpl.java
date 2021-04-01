package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.CreditCard;
import com.nhat.demo.model.BookingCart;
import com.nhat.demo.repository.CreditCardRepository;
import com.nhat.demo.service.CreditCardServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardServiceImpl implements CreditCardServiceIF {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private BookingCart bookingCart;

    @Override
    public String ValidateCart(CreditCard creditCard) {

        CreditCard crdCard = creditCardRepository.findByCardNumber(creditCard.getCardNumber());
        // neu thông tin gửi lên không khớp
        if (crdCard == null || !crdCard.equals(creditCard)) {
            return "not match";
        }
        //Nếu thông tin gửi lên khớp
        //kiem tra tiep tai khoan con du tien khong?

        double total = bookingCart.calculateTotal();
        if (crdCard.getBalance() < total) {
            return "not enough";
        }
        return "ok";
    }

    @Override
    public void tranferMoney(String fromCard, String toCard, double amount) {
        CreditCard fromCrCard = creditCardRepository.findByCardNumber(fromCard);
        CreditCard toCrCard = creditCardRepository.findByCardNumber(toCard);

        fromCrCard.setBalance(fromCrCard.getBalance() - amount);

        toCrCard.setBalance(toCrCard.getBalance() + amount);

        creditCardRepository.save(fromCrCard);
        creditCardRepository.save(toCrCard);

    }




}
