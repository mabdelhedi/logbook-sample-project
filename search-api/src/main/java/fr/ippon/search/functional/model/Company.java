package fr.ippon.search.functional.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String name;
    private String streetName;
    private String country;
    private Status status;
}
