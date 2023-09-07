package dev.ronin_engineer.software_development.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RoleFilter {
    private Integer roleId;
    private String roleName;
    private String companyCode;
    private String status;
    private Date createdTimeForm;
    private Date createdTimeTo;
    private Date updateTimeFrom;
    private Date updateTimeTo;
}
