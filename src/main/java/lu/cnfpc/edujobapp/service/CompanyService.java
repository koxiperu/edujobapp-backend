package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.request.CompanyRequest;
import lu.cnfpc.edujobapp.dto.response.CompanyResponse;
import lu.cnfpc.edujobapp.entity.Company;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.entity.enums.ECompanyType;
import lu.cnfpc.edujobapp.mapper.CompanyMapper;
import lu.cnfpc.edujobapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserService userService;

    public List<CompanyResponse> getMyCompanies() {
        User user = userService.getCurrentUser();
        return companyRepository.findByUserId(user.getId()).stream()
                .map(companyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    public CompanyResponse createCompany(CompanyRequest request) {
        User user = userService.getCurrentUser();
        
        Company company = new Company();
        company.setName(request.getName());
        try {
            company.setType(ECompanyType.valueOf(request.getType()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid company type: " + request.getType());
        }
        company.setCountry(request.getCountry());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());
        company.setPhone(request.getPhone());
        company.setEmail(request.getEmail());
        company.setUser(user);
        
        return companyMapper.toCompanyResponse(companyRepository.save(company));
    }

    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        
        // Ensure user owns the company
        User currentUser = userService.getCurrentUser();
        // Allow if user is owner or admin
        if (!company.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to access this company.");
        }
        
        return companyMapper.toCompanyResponse(company);
    }

    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!company.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to update this company.");
        }

        company.setName(request.getName());
        try {
            company.setType(ECompanyType.valueOf(request.getType()));
        } catch (IllegalArgumentException e) {
             throw new RuntimeException("Invalid company type: " + request.getType());
        }
        company.setCountry(request.getCountry());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());
        company.setPhone(request.getPhone());
        company.setEmail(request.getEmail());

        return companyMapper.toCompanyResponse(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!company.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to delete this company.");
        }
        
        companyRepository.delete(company);
    }
}
