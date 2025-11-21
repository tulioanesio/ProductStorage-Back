package com.unisul.product_storage.config;

import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.CategoryRepository;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            MovementRepository movementRepository
    ) {
        return args -> {
            logger.info("Iniciando o seed do banco de dados...");

            Map<String, Category> categoriesMap = seedCategories(categoryRepository);

            List<Product> newProducts = seedProducts(productRepository, categoriesMap);

            if (!newProducts.isEmpty()) {
                seedMovements(movementRepository, newProducts);
            }

            logger.info("Seed do banco de dados finalizado com sucesso.");
        };
    }

    @Transactional
    public Map<String, Category> seedCategories(CategoryRepository categoryRepository) {
        List<Category> categoriesData = Arrays.asList(
                new Category("Informática", "Eletrônicos", "Caixa"),
                new Category("Smartphones", "Mobile", "Caixa"),
                new Category("Bebidas", "Líquido", "Garrafa/Lata"),
                new Category("Padaria", "Perecíveis", "Pacote/Unidade"),
                new Category("Limpeza", "Químicos", "Frasco"),
                new Category("Hortifruti", "Frescos", "Granel/Kg"),
                new Category("Mercearia", "Grãos/Massas", "Pacote"),
                new Category("Carnes", "Perecíveis", "Kg"),
                new Category("Eletrônicos", "Tecnologia", "Caixa"),
                new Category("Escritório", "Papelaria", "Pacote/Unidade"),
                new Category("Construção", "Materiais", "Saco/Unidade"),
                new Category("Ferramentas", "Equipamentos", "Unidade"),
                new Category("Pet Shop", "Animais", "Pacote/Unidade"),
                new Category("Perfumaria", "Higiene/Beleza", "Frasco"),
                new Category("Cosméticos", "Beleza", "Frasco/Unidade"),
                new Category("Brinquedos", "Lazer", "Caixa/Unidade"),
                new Category("Automotivo", "Veículos/Peças", "Unidade"),
                new Category("Papelaria", "Materiais Escolar", "Pacote"),
                new Category("Utilidades Domésticas", "Casa", "Unidade"),
                new Category("Móveis", "Casa/Escritório", "Unidade"),
                new Category("Eletrodomésticos", "Casa", "Caixa"),
                new Category("Segurança", "Equipamentos", "Unidade"),
                new Category("Fitness", "Esportes", "Unidade"),
                new Category("Artigos Esportivos", "Esportes", "Unidade"),
                new Category("Jardinagem", "Casa/Lazer", "Unidade"),
                new Category("Instrumentos Musicais", "Música", "Unidade"),
                new Category("Bijuterias", "Acessórios", "Unidade"),
                new Category("Roupas", "Vestimenta", "Unidade"),
                new Category("Calçados", "Vestimenta", "Par"),
                new Category("Acessórios Veiculares", "Automotivo", "Unidade")
        );

        Map<String, Category> existingCategories = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));

        List<Category> categoriesToSave = new ArrayList<>();

        for (Category cat : categoriesData) {
            if (!existingCategories.containsKey(cat.getName())) {
                categoriesToSave.add(cat);
            }
        }

        if (!categoriesToSave.isEmpty()) {
            List<Category> saved = categoryRepository.saveAll(categoriesToSave);
            saved.forEach(c -> existingCategories.put(c.getName(), c));
            logger.info("{} novas categorias salvas.", saved.size());
        }

        return existingCategories;
    }

    @Transactional
    public List<Product> seedProducts(ProductRepository productRepository, Map<String, Category> categories) {
        List<ProductDefinition> defs = getProductDefinitions();

        Set<String> existingProductNames = productRepository.findAll().stream()
                .map(Product::getName)
                .collect(Collectors.toSet());

        List<Product> productsToSave = new ArrayList<>();

        for (ProductDefinition def : defs) {
            if (!existingProductNames.contains(def.name)) {
                Category cat = categories.get(def.categoryName);
                if (cat != null) {
                    productsToSave.add(new Product(
                            null,
                            def.name,
                            def.price,
                            def.unit,
                            def.stock,
                            def.minStock,
                            def.maxStock,
                            cat
                    ));
                } else {
                    logger.warn("Categoria não encontrada para o produto: {}", def.name);
                }
            }
        }

        if (!productsToSave.isEmpty()) {
            List<Product> savedProducts = productRepository.saveAll(productsToSave);
            logger.info("{} novos produtos salvos.", savedProducts.size());
            return savedProducts;
        }

        return Collections.emptyList();
    }

    @Transactional
    public void seedMovements(MovementRepository movementRepository, List<Product> products) {
        List<Movement> movements = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Product p : products) {
            int initialEntryQty = p.getStockAvailable() + 5;

            Movement entry = new Movement(
                    null,
                    p,
                    today.minusDays(10),
                    initialEntryQty,
                    MovementType.ENTRY,
                    "Normal"
            );
            if (initialEntryQty > p.getMaxStockQuantity()) {
                entry.setStatus("Acima do limite permitido!");
            }
            movements.add(entry);

            Movement exit = new Movement(
                    null,
                    p,
                    today.minusDays(2),
                    5,
                    MovementType.EXIT,
                    "Normal"
            );
            movements.add(exit);
        }


        findProductByName(products, "Teclado Mecânico HyperX").ifPresent(p -> {
            movements.add(new Movement(null, p, today.minusDays(1), 1, MovementType.EXIT, "Abaixo do limite permitido!"));
        });

        findProductByName(products, "Cabo HDMI 2.0 2m").ifPresent(p -> {
            movements.add(new Movement(null, p, today.minusDays(3), 50, MovementType.ENTRY, "Acima do limite permitido!"));
        });

        findProductByName(products, "Picanha Bovina").ifPresent(p -> {
            movements.add(new Movement(null, p, today.minusDays(5), 10, MovementType.ENTRY, "Normal"));
        });

        findProductByName(products, "Caneta Bic Azul").ifPresent(p -> {
            movements.add(new Movement(null, p, today.minusDays(1), 300, MovementType.ENTRY, "Normal"));
        });

        movementRepository.saveAll(movements);
        logger.info("{} movimentações registradas.", movements.size());
    }

    private Optional<Product> findProductByName(List<Product> products, String name) {
        return products.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    private record ProductDefinition(
            String name, BigDecimal price, String unit,
            int stock, int minStock, int maxStock, String categoryName) {}

    private List<ProductDefinition> getProductDefinitions() {
        return List.of(
                new ProductDefinition("Notebook Dell Inspiron 15", new BigDecimal("3899.90"), "UN", 12, 5, 50, "Informática"),
                new ProductDefinition("Mouse Sem Fio Logitech", new BigDecimal("89.90"), "UN", 45, 10, 100, "Informática"),
                new ProductDefinition("Monitor LG Ultrawide 29\"", new BigDecimal("1250.00"), "UN", 8, 2, 20, "Informática"),
                new ProductDefinition("Teclado Mecânico HyperX", new BigDecimal("499.00"), "UN", 3, 5, 30, "Eletrônicos"),
                new ProductDefinition("iPhone 15 128GB", new BigDecimal("5499.00"), "UN", 15, 5, 30, "Smartphones"),
                new ProductDefinition("Samsung Galaxy S23", new BigDecimal("3999.00"), "UN", 20, 5, 40, "Smartphones"),
                new ProductDefinition("Cabo HDMI 2.0 2m", new BigDecimal("25.00"), "UN", 320, 20, 300, "Informática"),
                new ProductDefinition("Coca-Cola 2L", new BigDecimal("9.99"), "UN", 150, 50, 500, "Bebidas"),
                new ProductDefinition("Cerveja Heineken Long Neck", new BigDecimal("6.49"), "UN", 240, 60, 1000, "Bebidas"),
                new ProductDefinition("Suco de Laranja Prats 900ml", new BigDecimal("14.50"), "UN", 40, 10, 100, "Bebidas"),
                new ProductDefinition("Água Mineral Sem Gás 500ml", new BigDecimal("2.50"), "UN", 300, 50, 1000, "Bebidas"),
                new ProductDefinition("Pão Francês", new BigDecimal("14.90"), "KG", 15, 5, 50, "Padaria"),
                new ProductDefinition("Queijo Mussarela Fatiado", new BigDecimal("49.90"), "KG", 10, 2, 30, "Padaria"),
                new ProductDefinition("Bolo de Chocolate", new BigDecimal("22.00"), "UN", 8, 2, 15, "Padaria"),
                new ProductDefinition("Leite Integral Tirol 1L", new BigDecimal("4.89"), "UN", 120, 24, 300, "Padaria"),
                new ProductDefinition("Sabão Líquido Omo 3L", new BigDecimal("45.90"), "UN", 35, 10, 100, "Limpeza"),
                new ProductDefinition("Detergente Ypê 500ml", new BigDecimal("2.99"), "UN", 200, 50, 500, "Limpeza"),
                new ProductDefinition("Arroz Branco Tio João 5kg", new BigDecimal("28.50"), "UN", 60, 20, 200, "Mercearia"),
                new ProductDefinition("Feijão Preto Camil 1kg", new BigDecimal("8.90"), "UN", 80, 20, 250, "Mercearia"),
                new ProductDefinition("Macarrão Espaguete 500g", new BigDecimal("4.50"), "UN", 90, 30, 300, "Mercearia"),
                new ProductDefinition("Banana Prata", new BigDecimal("6.99"), "KG", 40, 10, 100, "Hortifruti"),
                new ProductDefinition("Tomate Longa Vida", new BigDecimal("8.50"), "KG", 25, 5, 80, "Hortifruti"),
                new ProductDefinition("Picanha Bovina", new BigDecimal("89.90"), "KG", 2, 5, 40, "Carnes"),
                new ProductDefinition("Carne Moída 1ª", new BigDecimal("39.90"), "KG", 12, 5, 50, "Carnes"),
                new ProductDefinition("Folha Sulfite A4 - 500 folhas", new BigDecimal("36.90"), "UN", 100, 20, 300, "Escritório"),
                new ProductDefinition("Caneta Bic Azul", new BigDecimal("1.50"), "UN", 1, 50, 1000, "Escritório"),
                new ProductDefinition("Cimento Votoran 50kg", new BigDecimal("32.90"), "UN", 50, 10, 100, "Construção"),
                new ProductDefinition("Tinta Suvinil Branco Neve 18L", new BigDecimal("349.90"), "UN", 15, 5, 40, "Construção"),
                new ProductDefinition("Furadeira Impacto Bosch", new BigDecimal("289.00"), "UN", 8, 3, 20, "Ferramentas"),
                new ProductDefinition("Jogo de Chaves de Fenda Tramontina", new BigDecimal("59.90"), "UN", 20, 5, 50, "Ferramentas"),
                new ProductDefinition("Ração Pedigree Adulto 10kg", new BigDecimal("119.90"), "UN", 25, 5, 60, "Pet Shop"),
                new ProductDefinition("Shampoo Pet 500ml", new BigDecimal("18.50"), "UN", 40, 10, 80, "Pet Shop"),
                new ProductDefinition("Perfume Malbec O Boticário", new BigDecimal("189.90"), "UN", 12, 4, 30, "Perfumaria"),
                new ProductDefinition("Desodorante Rexona Aerosol", new BigDecimal("14.90"), "UN", 100, 20, 200, "Perfumaria"),
                new ProductDefinition("Kit Maquiagem Básico", new BigDecimal("89.90"), "UN", 15, 5, 40, "Cosméticos"),
                new ProductDefinition("Creme Hidratante Nivea", new BigDecimal("19.90"), "UN", 60, 10, 120, "Cosméticos"),
                new ProductDefinition("Boneca Barbie", new BigDecimal("79.90"), "UN", 30, 5, 60, "Brinquedos"),
                new ProductDefinition("Carrinho Hot Wheels", new BigDecimal("12.90"), "UN", 200, 20, 500, "Brinquedos"),
                new ProductDefinition("Óleo Motor 5W30 Sintético", new BigDecimal("45.00"), "UN", 40, 10, 80, "Automotivo"),
                new ProductDefinition("Pneu Aro 14 Michelin", new BigDecimal("459.90"), "UN", 12, 4, 24, "Automotivo"),
                new ProductDefinition("Caderno Espiral 10 Matérias", new BigDecimal("24.90"), "UN", 80, 20, 200, "Papelaria"),
                new ProductDefinition("Mochila Escolar Preta", new BigDecimal("129.90"), "UN", 15, 5, 40, "Papelaria"),
                new ProductDefinition("Vassoura Piaçava", new BigDecimal("18.90"), "UN", 35, 10, 60, "Utilidades Domésticas"),
                new ProductDefinition("Jogo de Panelas Antiaderente", new BigDecimal("249.90"), "UN", 10, 3, 25, "Utilidades Domésticas"),
                new ProductDefinition("Cadeira Gamer XPTO", new BigDecimal("899.00"), "UN", 5, 2, 15, "Móveis"),
                new ProductDefinition("Mesa de Escritório em L", new BigDecimal("450.00"), "UN", 4, 2, 10, "Móveis"),
                new ProductDefinition("Liquidificador Mondial 1200W", new BigDecimal("129.90"), "UN", 20, 5, 50, "Eletrodomésticos"),
                new ProductDefinition("Air Fryer Philips Walita", new BigDecimal("599.00"), "UN", 15, 5, 30, "Eletrodomésticos"),
                new ProductDefinition("Câmera Segurança Wi-Fi", new BigDecimal("189.00"), "UN", 25, 5, 50, "Segurança"),
                new ProductDefinition("Kit Alarme Residencial", new BigDecimal("350.00"), "UN", 8, 2, 20, "Segurança"),
                new ProductDefinition("Halteres 5kg (Par)", new BigDecimal("99.90"), "UN", 15, 5, 30, "Fitness"),
                new ProductDefinition("Colchonete Yoga", new BigDecimal("59.90"), "UN", 20, 5, 50, "Fitness"),
                new ProductDefinition("Bola de Futebol Nike", new BigDecimal("149.90"), "UN", 30, 10, 60, "Artigos Esportivos"),
                new ProductDefinition("Raquete de Tênis Wilson", new BigDecimal("399.00"), "UN", 8, 3, 15, "Artigos Esportivos"),
                new ProductDefinition("Mangueira Jardim 20m", new BigDecimal("69.90"), "UN", 25, 5, 50, "Jardinagem"),
                new ProductDefinition("Cortador de Grama Elétrico", new BigDecimal("450.00"), "UN", 5, 2, 10, "Jardinagem"),
                new ProductDefinition("Violão Acústico Yamaha", new BigDecimal("899.00"), "UN", 6, 2, 12, "Instrumentos Musicais"),
                new ProductDefinition("Teclado Musical Casio", new BigDecimal("750.00"), "UN", 4, 2, 10, "Instrumentos Musicais"),
                new ProductDefinition("Brinco Argola Prata", new BigDecimal("45.00"), "UN", 50, 10, 100, "Bijuterias"),
                new ProductDefinition("Colar Gargantilha Dourada", new BigDecimal("35.00"), "UN", 40, 10, 80, "Bijuterias"),
                new ProductDefinition("Camiseta Básica Algodão", new BigDecimal("39.90"), "UN", 150, 30, 300, "Roupas"),
                new ProductDefinition("Calça Jeans Masculina", new BigDecimal("119.90"), "UN", 60, 15, 120, "Roupas"),
                new ProductDefinition("Tênis Esportivo Running", new BigDecimal("249.90"), "PR", 40, 10, 80, "Calçados"),
                new ProductDefinition("Chinelo Tipo Havaianas", new BigDecimal("29.90"), "PR", 100, 20, 200, "Calçados"),
                new ProductDefinition("Aromatizante Automotivo", new BigDecimal("12.50"), "UN", 80, 20, 150, "Acessórios Veiculares"),
                new ProductDefinition("Capa para Volante Universal", new BigDecimal("49.90"), "UN", 20, 5, 40, "Acessórios Veiculares")
        );
    }
}