package fr.ippon.search.functional.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private String streetName;
    private String country;
    private String status;
}
