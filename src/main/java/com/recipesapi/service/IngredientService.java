package com.recipesapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.model.Ingredient;

@Service
public interface IngredientService {
    Ingredient getCustomerById(int customerId);

    ResponseEntity<String> updateCustomer(int id, Ingredient updateCustomer);

    ResponseEntity<String> deleteCustomer(Integer idCustomer);

    List<Ingredient> searchCustomers(String keyword);
}
