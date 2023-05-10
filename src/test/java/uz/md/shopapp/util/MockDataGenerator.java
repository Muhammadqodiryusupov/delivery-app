package uz.md.shopapp.util;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.repository.InstitutionTypeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MockDataGenerator {

    private final Faker FAKER = new Faker();
    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;

    public InstitutionType getInstitutionTypeSaved() {
        return institutionTypeRepository.save(getInstitutionType());
    }

    public InstitutionType getInstitutionType() {
        long l = RandomUtils.nextLong(1, 100);
        return getInstitutionType(l);
    }

    public InstitutionType getInstitutionType(long index) {
        return new InstitutionType(
                "Restaurant " + index,
                "Restaurant " + index,
                " All restaurants " + index,
                " All restaurants " + index);
    }

    public List<InstitutionType> getInstitutionTypes(int count) {
        return IntStream.range(0, count)
                .mapToObj(operand -> getInstitutionType())
                .collect(Collectors.toList());
    }

    public List<InstitutionType> getInstitutionTypesSaved(int count) {
        return institutionTypeRepository.saveAll(IntStream.range(0, count)
                .mapToObj(this::getInstitutionType)
                .collect(Collectors.toList()));
    }


    public User getUser(Role role) {
        return new User(
                "Ali",
                "Yusupov",
                "+998902002020",
                role);
    }

    public Address getAddress(User manager) {
        return new Address(
                manager,
                15,
                4,
                2
        );
    }

    public Institution getInstitution(Location location, InstitutionType institutionType, User manager) {
        return new Institution(
                "Max Way",
                "Max Way",
                " Cafe ",
                " Cafe ",
                null,
                location,
                institutionType,
                null,
                manager);
    }

    public Category getCategory(Institution institution) {
        Category category = new Category();
        category.setNameUz("Uzbek meals");
        category.setNameRu("Uzbek meals");
        category.setDescriptionUz("uzbek national meals");
        category.setDescriptionRu("uzbek national meals");
        category.setDeleted(false);
        category.setActive(true);
        category.setInstitution(institution);
        return category;
    }

    public Location getLocation() {
        long la = RandomUtils.nextLong(15, 100);
        long lo = RandomUtils.nextLong(15, 100);
        return new Location((double) la, (double) lo);
    }

    public Product getProduct(Category category) {
        Random random = new Random();
        long price = random.nextLong() * 300 + 200;
        int v = random.nextInt(5) + 1;
        Product product = new Product();
        product.setNameUz("Plov " + v);
        product.setNameRu("Plov " + v);
        product.setDescriptionUz(" Andijan Plov ");
        product.setDescriptionRu("Andijan Plov");
        product.setPrice(price);
        product.setDeleted(false);
        product.setActive(true);
        product.setCategory(category);
        return product;
    }

    private Role getAdminRole() {
        return Role.builder()
                .name("ADMIN")
                .description("admin description")
                .permissions(Set.of(PermissionEnum.values()))
                .build();
    }

    public User getMockEmployee() {
        Role adminRole = getAdminRole();
        User user = getUser();
        user.setRole(adminRole);
        return user;
    }

    private User getUser() {
        Name name = FAKER.name();
        String phoneNumber = "+99893" + RandomStringUtils.random(7, false, true);
        String password = RandomStringUtils.random(5, false, true);
        return User.builder()
                .firstName(name.firstName())
                .lastName(name.lastName())
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    public User getMockClient() {
        Role clientRole = getClientRole();
        User user = getUser();
        user.setRole(clientRole);
        user.setCodeValidTill(LocalDateTime.now().plusDays(1));
        return user;
    }

    private Role getClientRole() {
        return Role.builder()
                .name("CLIENT")
                .description("client description")
                .permissions(Set.of(PermissionEnum.values()))
                .build();
    }

    public Institution getInstitution() {
        String random = RandomStringUtils.random(5, true, false);
        Location location = getLocation();
        InstitutionType institutionType = getInstitutionType();
        return Institution.builder()
                .nameUz("Cafe " + random)
                .nameRu("Cafe ru " + random)
                .descriptionUz("Cafe description " + random)
                .descriptionUz("Cafe description ru" + random)
                .location(location)
                .type(institutionType)
                .build();
    }

}
