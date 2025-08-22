package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.*;
import com.asb.user.model.entity.*;
import com.asb.user.repository.*;
import com.asb.user.service.IAreaService;
import com.asb.user.service.IUserService;
import com.asb.user.util.Constants;
import com.asb.user.util.ModelMapperLocal;
import com.asb.user.util.ObjectMapperUtils;
import com.asb.user.util.Utils;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author manuelm
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements IUserService {

    private final IUserRepository iRepository;
    private final IRolRepository iRolRepository;
    private final IAreaRepository iAreaRepository;
    private final IPositionRepository iPositionRepository;
    private final ICompanyRepository iCompanyRepository;
    private final ModelMapperLocal modelMapperLocal;
    private final Utils utils;

    @Override
    public UserDto findByEmail(String email) {
        Optional<EntityUser> objectOptional = iRepository.findByEmail(email);
        try {
            if (objectOptional.isPresent()) {
                return mapUserDto(objectOptional.get());
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido");
        }
    }

    @Override
    @Transactional
    public UserDto save(GgpUserSaveAndUpdateDto userDto) {

        if (userDto.getId() == null) {
            Optional<EntityUser> objectUserOptional = iRepository.findByEmailOrLogin(userDto.getEmail(), userDto.getLogin());
            if (objectUserOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Correo o Username ya existe");
            }
        } else {
            Boolean objectExists = iRepository.existsById(userDto.getId());
            if (objectExists) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El usuario con este ID ya existe");
            }
        }


        EntityRol ggpRolRepo = iRolRepository.findById(userDto.getRol())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Rol no encontrado"));

        EntityCompany ggpCompanyRepo = iCompanyRepository.findById(userDto.getCompany())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Company no encontrado"));

        EntityPosition ggpPositionRepo = iPositionRepository.findById(userDto.getPosition())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Position no encontrado"));

        EntityArea ggpAreaRepo = iAreaRepository.findById(userDto.getArea())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Area no encontrada"));


        EntityUser entityUserRepo = new EntityUser();
        entityUserRepo.setName(userDto.getName());
        entityUserRepo.setEmail(userDto.getEmail());
        entityUserRepo.setLogin(userDto.getLogin());
        entityUserRepo.setStatus(Constants.ACTIVE_STATUS);
        entityUserRepo.setRol(ggpRolRepo);
        entityUserRepo.setPosition(ggpPositionRepo);
        entityUserRepo.setCompany(ggpCompanyRepo);
        entityUserRepo.setArea(ggpAreaRepo);

        if (userDto.getPassword() != null) {
            String encodedPassword = utils.bcryptEncryptor(userDto.getPassword());
            entityUserRepo.setPassword(encodedPassword);
        } else {
            entityUserRepo.setPassword(utils.bcryptEncryptor(Constants.DEFAULT_PASSWORD));
        }

        EntityUser newEntityUserRepo = iRepository.save(entityUserRepo);
        return mapUserDto(newEntityUserRepo);
    }



    @Override
    @Transactional
    public UserDto update(long userId, GgpUserSaveAndUpdateDto userDto) {
        Boolean objectExists = iRepository.existsById(userId);
        ArrayList<Long> userDtoList = new ArrayList<>();
        userDtoList.add(userId);

        Pageable pagingSort = PageRequest.of(1, 10);
        Page<EntityUser> objectUserOptional = iRepository.findByEmailOrLoginAndIdNotIn(
                userDto.getEmail(), userDto.getLogin(), userDtoList, pagingSort);

        if (objectExists && objectUserOptional.getTotalElements() == 0) {


            EntityRol ggpRolRepo = iRolRepository.findById(userDto.getRol())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Rol no encontrado"));

            EntityCompany ggpCompanyRepo = iCompanyRepository.findById(userDto.getCompany())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Company no encontrado"));

            EntityPosition ggpPositionRepo = iPositionRepository.findById(userDto.getPosition())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Position no encontrado"));

            EntityArea ggpAreaRepo = iAreaRepository.findById(userDto.getArea())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Area no encontrada"));


            EntityUser entityUserRepo = iRepository.findById(userId).orElse(new EntityUser());
            entityUserRepo.setName(userDto.getName());
            entityUserRepo.setEmail(userDto.getEmail());
            entityUserRepo.setLogin(userDto.getLogin());
            entityUserRepo.setStatus(userDto.getStatus());
            entityUserRepo.setRol(ggpRolRepo);
            entityUserRepo.setPosition(ggpPositionRepo);
            entityUserRepo.setCompany(ggpCompanyRepo);
            entityUserRepo.setArea(ggpAreaRepo);

            if (userDto.getPassword() != null) {
                String encodedPassword = utils.bcryptEncryptor(userDto.getPassword());
                entityUserRepo.setPassword(encodedPassword);
            }

            EntityUser updatedEntityUserRepo = iRepository.save(entityUserRepo);
            return mapUserDto(updatedEntityUserRepo);

        } else {
            if (objectUserOptional.getTotalElements() > 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Correo o Username ya existe");
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
            }
        }
    }


    @Override
    @Transactional
    public boolean delete(long id) {
        try {
            Optional<EntityUser> objectOptional = iRepository.findById(id);
            UserDto objectDtoVo = null;
            if (objectOptional.isPresent()) {
                objectDtoVo = new UserDto();
                BeanUtils.copyProperties(objectOptional.get(), objectDtoVo);
                EntityUser objectTmp = objectOptional.get();
                objectTmp.setStatus(Constants.INACTIVE_STATUS);
                iRepository.save(objectTmp);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public UserDto get(long id) {
        Optional<EntityUser> objectOptional = iRepository.findById(id);
        if (objectOptional.isPresent()) {
            return mapUserDto(objectOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto No existe");
        }
    }

    @Override
    public Page<GgpUserGetAllDto> getAll(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        return mapPageUserDto(iRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<GgpUserGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        return iRepository.getStatus(pagingSort);
    }


    @Override
    public List<GgpUserGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return iRepository.findByStatus(status).stream()
                .map(objects -> GgpUserGetAllDto.builder()
                        .id(objects.getId())
                        .name(objects.getName())
                        .login(objects.getLogin())
                        .password(objects.getPassword())
                        .email(objects.getEmail())
                        .rol(RolDto.builder()
                                .id(objects.getRol().getId())
                                .name(objects.getRol().getName())
                                .build())
                        .rolId(objects.getRol().getId())
                        .position(objects.getPosition())
                        .company(objects.getCompany())
                        .status(objects.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public Page<GgpUserGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        Long id = null;
        String name = null;
        String email = null;
        String login = null;
        String companyName = null;
        String positionDescription = null;
        String areaDescription = null;
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("id")) {
            //name = "%" + customQuery.get("name") + "%";
            id = Long.valueOf(customQuery.get("id"));
        }
        if (customQuery.containsKey("name")) {
            //name = "%" + customQuery.get("name") + "%";
            name = customQuery.get("name");
        }

        if (customQuery.containsKey("email")) {
            //email = "%" + customQuery.get("email") + "%";
            email = customQuery.get("email");
        }

        if (customQuery.containsKey("login")) {
            //login = "%" + customQuery.get("login") + "%";
            login = customQuery.get("login");
        }

        if (customQuery.containsKey("companyName")) {
            //login = "%" + customQuery.get("companyName") + "%";
            companyName = customQuery.get("companyName");
        }

        if (customQuery.containsKey("description")) {
            //login = "%" + customQuery.get("positionDescription") + "%";
            positionDescription = customQuery.get("description");
        }

        if (customQuery.containsKey("areaDescription")) {
            //login = "%" + customQuery.get("positionDescription") + "%";
            areaDescription = customQuery.get("areaDescription");
        }



        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        return mapPageUserDto(iRepository
                .findByIdOrNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrLoginContainingIgnoreCaseOrCompanyId_CompanyNameContainingIgnoreCaseOrPositionId_DescriptionContainingIgnoreCaseOrAreaId_DescriptionContainingIgnoreCaseOrAndStatus(
                       id, name, email, login,companyName,positionDescription,areaDescription, status,pagingSort), pagingSort
                );
    }

    @Override
    public ForgotPasswordUserDto forgotPassword(GgpForgotPasswordDto ggpForgotPasswordDto) {
        Optional<EntityUser> objectOptional = iRepository.findByEmail(ggpForgotPasswordDto.getEmail())
                .stream()
                .findFirst();

        if (!objectOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
        }

        Random random = new Random();
        EntityUser entityUser = objectOptional.get();
        String password = Constants.PASSWORD_DEFUULT_PREFIX + "+" + random.nextInt(10);
        String encodedPassword = utils.bcryptEncryptor(password);
        entityUser.setPassword(encodedPassword);

        iRepository.save(entityUser);
/*
        try {
            //iMailService.forgotPassword(ggpForgotPasswordDto.getEmail(), password);

            return ForgotPasswordUserDto.builder()
                    .email(ggpForgotPasswordDto.getEmail())
                    .build();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }*/
        return ForgotPasswordUserDto.builder()
                .email(ggpForgotPasswordDto.getEmail())
                .build();
    }

    private UserDto mapUserDto(EntityUser objectUser) {
        UserDto objectDtoVo = new UserDto();
        BeanUtils.copyProperties(objectUser, objectDtoVo);

        objectDtoVo.setRolId(objectUser.getRol().getId());
        objectDtoVo.setCompanyId(objectUser.getCompany().getId());
        objectDtoVo.setPositionId(objectUser.getPosition().getId());
        objectDtoVo.setAreaId(objectUser.getArea().getId());

        return objectDtoVo;
    }


    private Page<GgpUserGetAllDto> mapPageUserDto(Page<EntityUser> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                ObjectMapperUtils.mapAll(entityPage.getContent(),
                        GgpUserGetAllDto.class),
                pagingSort, totalElements).map(ggpUserGetAllDto -> {
            ggpUserGetAllDto.setRolId(ggpUserGetAllDto.getRol().getId());
            return ggpUserGetAllDto;
        });
    }
}
