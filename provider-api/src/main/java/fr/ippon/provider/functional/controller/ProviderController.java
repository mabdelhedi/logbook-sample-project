package fr.ippon.provider.functional.controller;

import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProviderController {

    @PostMapping("/provider/company/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestBody Criteria criteria) {
        List<Company> companies = Arrays.asList(
                Company.builder()
                        .name("Ippon Technologie")
                        .status("Active")
                        .streetName("16 boulevard des tentacules")
                        .country("FR")
                        .build(),

                Company.builder()
                        .name(criteria.getName())
                        .status("Closed")
                        .streetName("15 impasse du chat qui dort")
                        .country("BE")
                        .build()
        );

        return ResponseEntity.ok(companies);
    }

}
