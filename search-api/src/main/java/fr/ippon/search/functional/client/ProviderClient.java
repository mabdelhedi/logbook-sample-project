package fr.ippon.search.functional.client;

import feign.Headers;
import feign.RequestLine;
import fr.ippon.search.functional.model.Company;
import fr.ippon.search.functional.model.Criteria;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Headers({"Content-Type: application/json", "Accept: application/json"})
public interface ProviderClient {

    @RequestLine("POST /provider/company/search")
    List<Company> searchCompanies(@RequestBody Criteria criteria);

}
