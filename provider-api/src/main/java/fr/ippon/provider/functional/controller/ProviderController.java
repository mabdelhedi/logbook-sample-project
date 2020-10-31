package fr.ippon.provider.functional.controller;

import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import fr.ippon.search.functional.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.unmodifiableList;

@RestController
public class ProviderController {

    @GetMapping("/provider/companies")
    public ResponseEntity<List<Company>> searchCompanies(Criteria criteria) {
        List<Company> companies = unmodifiableList(List.of(
                Company.builder()
                        .name("Ippon Technologie")
                        .status(Status.A)
                        .streetName("16 boulevard des tentacules")
                        .country("FR")
                        .build(),

                Company.builder()
                        .name(criteria.getName())
                        .status(Status.C)
                        .streetName("15 impasse du chat qui dort")
                        .country("BE")
                        .build()
        ));

        return ResponseEntity.ok(companies);
    }

}
