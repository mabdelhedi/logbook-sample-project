package fr.ippon.search.functional.controller;

import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import fr.ippon.search.functional.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> searchCompanies(Criteria criteria) {
        return ResponseEntity.ok(searchService.searchCompanies(criteria));
    }
}
