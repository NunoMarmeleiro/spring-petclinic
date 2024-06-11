package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ApplicationTests {

	@Test
	void verifyModuleStructureAndGenerateDocumentation() {
		var modules = ApplicationModules.of(PetClinicApplication.class).verify();
		new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
	}

}
