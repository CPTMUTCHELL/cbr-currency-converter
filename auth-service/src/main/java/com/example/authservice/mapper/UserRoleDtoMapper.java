package com.example.authservice.mapper;

import com.example.authservice.dto.UserRoleDto;
import com.example.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserRoleDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoles(UserRoleDto dto, @MappingTarget User userEntity);
}
