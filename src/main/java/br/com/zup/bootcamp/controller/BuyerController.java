package br.com.zup.bootcamp.controller;

import br.com.zup.bootcamp.controller.model.BuyerRequest;
import br.com.zup.bootcamp.controller.model.BuyerResponse;
import br.com.zup.bootcamp.controller.model.GenericResponse;
import br.com.zup.bootcamp.domain.model.Buyer;
import br.com.zup.bootcamp.domain.model.Coupon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// Intrinsic charge = 9
@RestController
@RequestMapping("/buy")
public class BuyerController {

    @PersistenceContext
    EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<GenericResponse> register(@Validated @RequestBody BuyerRequest request){
        GenericResponse response;

        Buyer newBuyer = request.convertBuyer();
        Coupon coupon = request.getCart().convertCoupon(manager, newBuyer.getPurchase());

        if(coupon.isExpired()){
            response = new GenericResponse("Coupon is expired");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        if(newBuyer.getPurchase().isTotalInvalid()){
            response = new GenericResponse("Total must to be greater than 0");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        if(newBuyer.getPurchase().isRequestTotalNotEquals(manager)){
            response = new GenericResponse("The total request is different from the calculated");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        if(newBuyer.isStateInvalid(manager)){
            response = new GenericResponse("Country not have this state");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        manager.persist(newBuyer);

        response = new GenericResponse("Buyer was registered");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<BuyerResponse> consultOne(@PathVariable String id){
        Buyer buyer = (Buyer) manager.find(Buyer.class, id);
        BuyerResponse response = new BuyerResponse(buyer);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
