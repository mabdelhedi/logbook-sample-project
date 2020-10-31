package fr.ippon.search.functional.service;

import fr.ippon.search.functional.client.ProviderClient;
import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SearchService {

    private final ProviderClient providerClient;

    public List<Company> searchCompanies(Criteria criteria) {
        try {
            return providerClient.searchCompanies(criteria);
        } catch (Exception exception) {
            log.warn("Call to provider api went wrong", exception);
            return List.of();
        }
    }

}
