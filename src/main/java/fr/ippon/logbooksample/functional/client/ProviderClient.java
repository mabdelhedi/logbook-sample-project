package fr.ippon.logbooksample.functional.client;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import fr.ippon.logbooksample.functional.model.Company;
import fr.ippon.logbooksample.functional.model.Criteria;

import java.util.List;

@Headers({"Content-Type: application/json"})
public interface ProviderClient {

    @RequestLine("GET /provider/companies")
    List<Company> searchCompanies(@QueryMap Criteria criteria);

}
