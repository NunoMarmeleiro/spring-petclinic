package org.springframework.samples.petclinic.visit.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.visit.VisitManagement;
import org.springframework.samples.petclinic.visit.infrastructure.VisitRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitManagementImpl implements VisitManagement {

	private final VisitRepository visits;

	@Override
	public void deleteVisits(int petId) {
		this.visits.deleteVisitByPetId(petId);
	}

}
