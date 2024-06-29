package sn.ept.git.seminaire.poc.demo;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IArchTest {

    public static final String BASE = "sn.ept.git.seminaire.cicd";
    public static final String SERVICES = BASE.concat(".services..");
    public static final String SERVICES_IMPL = BASE.concat(".services.impl..");
    public static final String REPOSITORY = BASE.concat(".repositories..");
    public static final String MODELS = BASE.concat(".models..");
    public static final String COMPONENTS = BASE.concat(".component..");
    public static final String RESOURCE = BASE.concat(".resources..");
    static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .importPackages(BASE);
    }


    @Test
    void servicesComponentsAndRepositoriesShouldNotDependOnWebLayer() {
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAnyPackage(SERVICES, SERVICES_IMPL)
                .or()
                .resideInAnyPackage(REPOSITORY)
                .or()
                .resideInAnyPackage(COMPONENTS)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(RESOURCE)
                .because("Services, components and repositories should not depend on web layer")
                .check(importedClasses);
    }

    @Test
    void resourcesShouldNotDependOnRepositoryLayer() {
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAnyPackage(RESOURCE)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(REPOSITORY)
                .because("Resources should not depend on Repository layer")
                .check(importedClasses);
    }

    @Test
    void resourcesShouldNotUseConcreteClassesOnServiceLayer() {
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(RESOURCE)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(SERVICES_IMPL)
                .because("Resources should use Interface in  Service layer")
                .check(importedClasses);
    }


    @Test
    void servicesShouldNotDependOnServiceLayer() {
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAnyPackage(SERVICES_IMPL)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(SERVICES_IMPL)
                .because("Services should not depend on Service layer")
                .check(importedClasses);
    }


    @Test
    void servicesInterfacesShouldNotBeInServiceImplPackage() {
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAnyPackage(SERVICES_IMPL)
                .should()
                .beInterfaces()
                .because("Service Interfaces should not be inside service.impl package")
                .check(importedClasses);
    }




    @Test
    void servicesClassesShouldNotBeDirectlyInServicePackage() {
        ArchRuleDefinition.classes()
                .that()
                .resideInAnyPackage(SERVICES)
                .and()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage(SERVICES_IMPL)
                .because("Service classes should not be directly inside service package")
                .check(importedClasses);
    }

    @Test
    void servicesClassesNamesShouldEndWithIImpl() {
        ArchRuleDefinition.classes()
                .that()
                .resideInAnyPackage(SERVICES_IMPL)
                .should()
                .haveSimpleNameEndingWith("Impl")
                .because("Service classes' name should end with Impl")
                .check(importedClasses);
    }

    @Test
    void resourcesNamesShouldEndWithResource() {
        ArchRuleDefinition.classes()
                .that()
                .resideInAPackage(RESOURCE)
                .should()
                .haveSimpleNameEndingWith("Resource")
                .because("Resource name should end with Resource")
                .check(importedClasses);
    }

    @Test
    void onlyRepositoriesShouldBeAnnotatedWithRepository() {
        ArchRuleDefinition.classes()
                .that()
                .areAnnotatedWith(org.springframework.stereotype.Repository.class)
                .should()
                .resideInAnyPackage(REPOSITORY)
                .because("Only repositories should be annotated with @Repository")
                .check(importedClasses);
    }


    @Test
    void onlyServicesShouldBeAnnotatedWithService() {
        ArchRuleDefinition.classes()
                .that()
                .areAnnotatedWith(org.springframework.stereotype.Service.class)
                .should()
                .resideInAnyPackage(SERVICES_IMPL)
                .because("Only services should be annotated with @Service")
                .check(importedClasses);
    }

    @Test
    void onlyResourcesShouldBeAnnotatedWithRestController() {
        ArchRuleDefinition.classes()
                .that()
                .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                .should()
                .resideInAnyPackage(RESOURCE)
                .because("Only resources should be annotated with @RestController")
                .check(importedClasses);
    }

    @Test
    void onlyDomaineClassesShouldBeAnnotatedWithEntity() {
        ArchRuleDefinition.classes()
                .that()
                .areAnnotatedWith(jakarta.persistence.Entity.class)
                .should()
                .resideInAnyPackage(MODELS)
                .because("Only entities should be annotated with @Entity")
                .check(importedClasses);
    }

    @Test
    void onlyDomaineClassesShouldBeAnnotatedWithTable() {
        ArchRuleDefinition.classes()
                .that()
                .areAnnotatedWith(jakarta.persistence.Table.class)
                .should()
                .resideInAnyPackage(MODELS)
                .because("Only entities should be annotated with @Table")
                .check(importedClasses);
    }





}
