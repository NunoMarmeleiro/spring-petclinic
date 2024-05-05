package org.springframework.samples.petclinic.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

	private final VisitRepository visits;

	@Override
	public void deleteVisits(int petId) {
		this.visits.deleteVisitByPetId(petId);
	}
}
