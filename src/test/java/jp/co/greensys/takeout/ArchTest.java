package jp.co.greensys.takeout;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("jp.co.greensys.takeout");

        noClasses()
            .that()
            .resideInAnyPackage("jp.co.greensys.takeout.service..")
            .or()
            .resideInAnyPackage("jp.co.greensys.takeout.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..jp.co.greensys.takeout.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
