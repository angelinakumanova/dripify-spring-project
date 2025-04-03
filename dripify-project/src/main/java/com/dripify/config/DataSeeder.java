package com.dripify.config;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.model.enums.*;
import com.dripify.product.repository.ProductImageRepository;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.repository.UserRepository;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final UserRepository userRepository;


    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    public DataSeeder(CategoryRepository categoryRepository, UserService userService, UserRepository userRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedCategories();
        seedUsers();
        seedProducts();
        seedProductImages();
    }

    private void seedProductImages() {

        if (productImageRepository.count() == 0) {
            productRepository.findAll().forEach(product -> {

                List<ProductImage> savedProductImages = List.of();
                if (product.getGender().equals(Gender.WOMEN)) {


                    ProductImage productImage1 = ProductImage.builder()
                                .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705131/2722b3a9d1b3c0709b75c577a35c6b53_db7h0n.jpg")
                                .product(product).build();
                    ProductImage productImage2 = ProductImage.builder()
                                .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705133/2fba391031a398922dc1e483ed712247_a7muqa.jpg")
                                .product(product).build();
                    savedProductImages = productImageRepository.saveAll(List.of(productImage1, productImage2));



                } else if (product.getGender().equals(Gender.MEN)) {
                    ProductImage productImage1 = ProductImage.builder()
                            .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705464/a67dbf8f2685c775bca6f1961fb057b6_fham4g.jpg")
                            .product(product).build();
                    ProductImage productImage2 = ProductImage.builder()
                            .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705494/d78d7089588f6a00bda0fd5f466c229e_r8mxbk.jpg")
                            .product(product).build();

                    savedProductImages = productImageRepository.saveAll(List.of(productImage1, productImage2));

                } else {
                    ProductImage productImage1 = ProductImage.builder()
                            .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705565/1ba0be03233a9ce26ab5d9bdc5a07682_mzr1wf.jpg")
                            .product(product).build();
                    ProductImage productImage2 = ProductImage.builder()
                            .imageUrl("https://res.cloudinary.com/dpj028iin/image/upload/v1743705566/ed9d105158d6e7f46b60b1d1711f4fe6_sjssud.jpg")
                            .product(product).build();

                    savedProductImages = productImageRepository.saveAll(List.of(productImage1, productImage2));

                }

                product.setImages(savedProductImages);
                productRepository.save(product);
            });
        } else {
            log.info("Product Images already initialized. Skipping initialization");
        }

    }

    private void seedProducts() throws IOException {

        if (productRepository.count() == 0) {
            List<User> users = userRepository.findAll();

            List<Product> products = List.of(

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Pants - Stylish Edition",
                            "This pants is designed for comfort and style. Perfect for any occasion.",
                            Brand.PUMA,
                            Size.L,
                            Material.POLYESTER,
                            Color.GREEN,
                            Condition.WORN,
                            BigDecimal.valueOf(133),
                            categoryRepository.findByName("Pants").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.BERSHKA,
                            Size.S,
                            Material.SILK,
                            Color.OLIVE,
                            Condition.WORN,
                            BigDecimal.valueOf(126),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.BERSHKA,
                            Size.EU39,
                            Material.FAUX_LEATHER,
                            Color.RED,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(179),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.VANS,
                            Size.EU37,
                            Material.ACRYLIC,
                            Color.BEIGE,
                            Condition.WORN,
                            BigDecimal.valueOf(222),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.CALVIN_KLEIN,
                            Size.EU37,
                            Material.LEATHER,
                            Color.TEAL,
                            Condition.NEW,
                            BigDecimal.valueOf(84),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Bags - Stylish Edition",
                            "This bags is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.EU39,
                            Material.CHIFFON,
                            Color.ORANGE,
                            Condition.NEW,
                            BigDecimal.valueOf(430),
                            categoryRepository.findByName("Bags").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Heels - Stylish Edition",
                            "This heels is designed for comfort and style. Perfect for any occasion.",
                            Brand.PUMA,
                            Size.EU41,
                            Material.LEATHER,
                            Color.GRAY,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(373),
                            categoryRepository.findByName("Heels").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.GUCCI,
                            Size.EU40,
                            Material.SPANDEX,
                            Color.OLIVE,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(47),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.VANS,
                            Size.L,
                            Material.SPANDEX,
                            Color.ORANGE,
                            Condition.WORN,
                            BigDecimal.valueOf(357),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Skirts - Stylish Edition",
                            "This skirts is designed for comfort and style. Perfect for any occasion.",
                            Brand.LOUIS_VUITTON,
                            Size.EU44,
                            Material.NYLON,
                            Color.PINK,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(367),
                            categoryRepository.findByName("Skirts").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.ADIDAS,
                            Size.EU40,
                            Material.OTHER,
                            Color.PINK,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(316),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.BALENCIAGA,
                            Size.XL,
                            Material.LINEN,
                            Color.PURPLE,
                            Condition.NEW,
                            BigDecimal.valueOf(122),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.XS,
                            Material.LEATHER,
                            Color.MAROON,
                            Condition.NEW,
                            BigDecimal.valueOf(464),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.XXXL,
                            Material.SATIN,
                            Color.WHITE,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(353),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.H_M,
                            Size.EU39,
                            Material.FAUX_LEATHER,
                            Color.GRAY,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(183),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Skirts - Stylish Edition",
                            "This skirts is designed for comfort and style. Perfect for any occasion.",
                            Brand.NIKE,
                            Size.M,
                            Material.SILK,
                            Color.BROWN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(292),
                            categoryRepository.findByName("Skirts").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.NIKE,
                            Size.EU38,
                            Material.FLEECE,
                            Color.CYAN,
                            Condition.NEW,
                            BigDecimal.valueOf(138),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sunglasses - Stylish Edition",
                            "This sunglasses is designed for comfort and style. Perfect for any occasion.",
                            Brand.NO_LABEL,
                            Size.EU38,
                            Material.SPANDEX,
                            Color.CYAN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(54),
                            categoryRepository.findByName("Sunglasses").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.REEBOK,
                            Size.EU37,
                            Material.VELVET,
                            Color.GREEN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(29),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.NO_LABEL,
                            Size.XL,
                            Material.DENIM,
                            Color.PINK,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(63),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.GUCCI,
                            Size.L,
                            Material.VELVET,
                            Color.OLIVE,
                            Condition.WORN,
                            BigDecimal.valueOf(350),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Watches - Stylish Edition",
                            "This watches is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.EU41,
                            Material.ACRYLIC,
                            Color.PURPLE,
                            Condition.WORN,
                            BigDecimal.valueOf(350),
                            categoryRepository.findByName("Watches").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.LOUIS_VUITTON,
                            Size.EU40,
                            Material.OTHER,
                            Color.BLUE,
                            Condition.NEW,
                            BigDecimal.valueOf(186),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.FILA,
                            Size.S,
                            Material.NYLON,
                            Color.TEAL,
                            Condition.WORN,
                            BigDecimal.valueOf(103),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.LOUIS_VUITTON,
                            Size.EU41,
                            Material.OTHER,
                            Color.OLIVE,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(469),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.EU37,
                            Material.LACE,
                            Color.RED,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(241),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "T-Shirts - Stylish Edition",
                            "This t-shirts is designed for comfort and style. Perfect for any occasion.",
                            Brand.PRADA,
                            Size.EU42,
                            Material.LEATHER,
                            Color.GREEN,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(243),
                            categoryRepository.findByName("T-Shirts").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.PUMA,
                            Size.XXXL,
                            Material.CHIFFON,
                            Color.BROWN,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(17),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.TOMMY_HILFIGER,
                            Size.XXL,
                            Material.SILK,
                            Color.GREEN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(10),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Heels - Stylish Edition",
                            "This heels is designed for comfort and style. Perfect for any occasion.",
                            Brand.NEW_BALANCE,
                            Size.EU40,
                            Material.OTHER,
                            Color.ORANGE,
                            Condition.NEW,
                            BigDecimal.valueOf(217),
                            categoryRepository.findByName("Heels").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.REEBOK,
                            Size.M,
                            Material.COTTON,
                            Color.BLACK,
                            Condition.WORN,
                            BigDecimal.valueOf(142),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.CONVERSE,
                            Size.EU37,
                            Material.SILK,
                            Color.BEIGE,
                            Condition.WORN,
                            BigDecimal.valueOf(107),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Heels - Stylish Edition",
                            "This heels is designed for comfort and style. Perfect for any occasion.",
                            Brand.H_M,
                            Size.EU39,
                            Material.SILK,
                            Color.GREEN,
                            Condition.WORN,
                            BigDecimal.valueOf(464),
                            categoryRepository.findByName("Heels").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.LEVI_S,
                            Size.EU36,
                            Material.DENIM,
                            Color.BROWN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(235),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Heels - Stylish Edition",
                            "This heels is designed for comfort and style. Perfect for any occasion.",
                            Brand.OTHER,
                            Size.EU44,
                            Material.SATIN,
                            Color.WHITE,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(351),
                            categoryRepository.findByName("Heels").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.BERSHKA,
                            Size.EU44,
                            Material.SILK,
                            Color.GREEN,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(493),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "T-Shirts - Stylish Edition",
                            "This t-shirts is designed for comfort and style. Perfect for any occasion.",
                            Brand.PUMA,
                            Size.EU39,
                            Material.FAUX_LEATHER,
                            Color.BLACK,
                            Condition.VERY_GOOD,
                            BigDecimal.valueOf(327),
                            categoryRepository.findByName("T-Shirts").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Dresses - Stylish Edition",
                            "This dresses is designed for comfort and style. Perfect for any occasion.",
                            Brand.LOUIS_VUITTON,
                            Size.EU39,
                            Material.POLYESTER,
                            Color.BLUE,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(217),
                            categoryRepository.findByName("Dresses").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Suits - Stylish Edition",
                            "This suits is designed for comfort and style. Perfect for any occasion.",
                            Brand.NEW_BALANCE,
                            Size.XXXL,
                            Material.WOOL,
                            Color.RED,
                            Condition.WORN,
                            BigDecimal.valueOf(166),
                            categoryRepository.findByName("Suits").get(),
                            Gender.MEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                    ,

                    new Product(
                            true,
                            null,  // images (assumed empty for now)
                            users.get((int) (Math.random() * users.size())),
                            "Sandals - Stylish Edition",
                            "This sandals is designed for comfort and style. Perfect for any occasion.",
                            Brand.BERSHKA,
                            Size.EU41,
                            Material.POLYESTER,
                            Color.BROWN,
                            Condition.ACCEPTABLE,
                            BigDecimal.valueOf(21),
                            categoryRepository.findByName("Sandals").get(),
                            Gender.WOMEN,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    ));

            productRepository.saveAll(products);

            log.info("Initialized {} products", products.size());
        } else {
            log.info("Products already initialized. Skipping initialization");
        }

    }

    private void seedUsers() throws IOException {

        if (userRepository.count() == 0) {
            File file = new ClassPathResource("/data/users.json").getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            List<RegisterRequest> users = objectMapper
                    .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, RegisterRequest.class));

            users.forEach(userService::register);
            log.info("Successfully initialized {} users", users.size());
        } else {
            log.info("Users already exist. Skipping initialization");
        }

    }

    private void seedCategories() {
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
