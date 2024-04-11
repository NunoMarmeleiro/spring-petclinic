package org.springframework.samples.petclinic;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import java.util.ArrayList;
import java.util.Collection;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@AnalyzeClasses(packages = {"org.springframework.samples.petclinic.pet", "org.springframework.samples.petclinic.owner", "org.springframework.samples.petclinic.visit", "org.springframework.samples.petclinic.vet"})
public class ArchitectureTests {

	@ArchTest
	public static final ArchRule petRule = classes().that().resideInAPackage("..pet..")
		.should().onlyHaveDependentClassesThat().resideInAnyPackage( "..pet..", "..owner..", "..visit..");

	@ArchTest
	public static final ArchRule ownerRule = classes().that().resideInAPackage("..owner..")
		.should().onlyHaveDependentClassesThat().resideInAnyPackage( "..owner..", "..visit..");

	@ArchTest
	public static final ArchRule visitRule = classes().that().resideInAPackage("..visit..")
		.should().onlyHaveDependentClassesThat().resideInAnyPackage("..owner..", "..pet..", "..visit..");

	@ArchTest
	public static final ArchRule vetRule = noClasses().that().resideInAPackage("..vet..")
		.should().dependOnClassesThat().resideInAnyPackage("..owner..", "..pet..", "..visit..");

	Collection<String> packages = new ArrayList<>();
	@BeforeEach
	void setup() {
		this.packages.add("org.springframework.samples.petclinic.pet");
		this.packages.add("org.springframework.samples.petclinic.owner");
		this.packages.add("org.springframework.samples.petclinic.visit");
		this.packages.add("org.springframework.samples.petclinic.vet");
	}

	@Test
	void petDependencyChecks() {
		JavaClasses importedClasses = new ClassFileImporter().importPackages(this.packages);
		petRule.check(importedClasses);
	}

	@Test
	void ownerDependencyChecks() {
		JavaClasses importedClasses = new ClassFileImporter().importPackages(this.packages);
		ownerRule.check(importedClasses);
	}

	@Test
	void visitDependencyChecks() {
		JavaClasses importedClasses = new ClassFileImporter().importPackages(this.packages);
		visitRule.check(importedClasses);
	}

	@Test
	void vetDependencyChecks() {
		JavaClasses importedClasses = new ClassFileImporter().importPackages(this.packages);
		vetRule.check(importedClasses);
	}
}
