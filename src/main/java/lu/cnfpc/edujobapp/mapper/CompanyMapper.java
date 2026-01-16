package lu.cnfpc.edujobapp.mapper;

import lu.cnfpc.edujobapp.dto.response.CompanyResponse;
import lu.cnfpc.edujobapp.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyResponse toCompanyResponse(Company company) {
        if (company == null) {
            return null;
        }
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setType(company.getType().name());
        response.setCountry(company.getCountry());
        response.setAddress(company.getAddress());
        response.setWebsite(company.getWebsite());
        response.setPhone(company.getPhone());
        response.setEmail(company.getEmail());
        if (company.getUser() != null) {
            response.setUserId(company.getUser().getId());
        }
        return response;
    }
}
