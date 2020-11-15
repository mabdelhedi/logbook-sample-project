package fr.ippon.logbooksample.functional.service;

import fr.ippon.logbooksample.functional.client.ProviderClient;
import fr.ippon.logbooksample.functional.model.Company;
import fr.ippon.logbooksample.functional.model.Criteria;
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
