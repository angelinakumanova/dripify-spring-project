package com.dripify.category.service;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.shared.enums.Gender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CategoryInit implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryInit(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count() == 0) {

            // Parent Categories
            Category clothing = new Category("Clothing", Gender.UNISEX);
            Category accessories = new Category("Accessories", Gender.UNISEX);
            Category shoes = new Category("Shoes", Gender.UNISEX);
            categoryRepository.saveAll(List.of(clothing, accessories, shoes));

            // UNISEX Clothing Subcategories
            List<Category> unisexCategories = List.of(
                    new Category("Jeans", Gender.UNISEX, clothing),
                    new Category("T-Shirts",  Gender.UNISEX, clothing),
                    new Category("Hoodies",  Gender.UNISEX, clothing),
                    new Category("Shirts",  Gender.UNISEX, clothing),
                    new Category("Pants",  Gender.UNISEX, clothing),
                    new Category("Shorts",  Gender.UNISEX, clothing),
                    new Category("Coats",  Gender.UNISEX, clothing),
                    new Category("Jackets",  Gender.UNISEX, clothing)
            );
            categoryRepository.saveAll(unisexCategories);

            // MEN Clothing Subcategories
            categoryRepository.save(new Category("Suits", Gender.MEN, clothing));

            // WOMEN Clothing Subcategories
            categoryRepository.saveAll(List.of(
                    new Category("Dresses", Gender.WOMEN, clothing),
                    new Category("Skirts", Gender.WOMEN, clothing)
            ));

            // Accessories subcategories
            categoryRepository.saveAll(List.of(
                    new Category("Bags", Gender.UNISEX, accessories),
                    new Category("Hats", Gender.UNISEX, accessories),
                    new Category("Belts", Gender.UNISEX, accessories),
                    new Category("Sunglasses", Gender.UNISEX, accessories),
                    new Category("Jewelry", Gender.UNISEX, accessories),
                    new Category("Watches", Gender.UNISEX, accessories),
                    new Category("Gloves", Gender.UNISEX, accessories)
            ));

            // Shoes subcategories
            categoryRepository.saveAll(List.of(
                    new Category("Sneakers", Gender.UNISEX, shoes),
                    new Category("Boots", Gender.UNISEX, shoes),
                    new Category("Sandals", Gender.WOMEN, shoes),
                    new Category("Heels", Gender.WOMEN, shoes),
                    new Category("Loafers", Gender.UNISEX, shoes),
                    new Category("Slippers", Gender.UNISEX, shoes)
            ));

            log.info("Categories initialized successfully.");
        } else {
            log.info("Categories already exist. Skipping initialization.");
        }
    }

}
