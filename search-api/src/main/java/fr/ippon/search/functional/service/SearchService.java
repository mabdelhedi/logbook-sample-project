package fr.ippon.search.functional.service;

import fr.ippon.search.functional.client.ProviderClient;
import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SearchService {

    private final ProviderClient providerClient;

    public List<Company> searchCompanies(Criteria criteria) {
        // On appelle ici notre ProviderApi
        try {
            return providerClient.searchCompanies(criteria);
        } catch (Exception exception) {
            // On catch ici une Exception dans le cas où l’appel n’a pas renvoyé un status code 200 voire 404
            log.warn("Call to provider api went wrong", exception);
            return new ArrayList<>();
        }
    }

}
