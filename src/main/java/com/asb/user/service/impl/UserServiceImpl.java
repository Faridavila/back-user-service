package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.*;
import com.asb.user.model.entity.*;
import com.asb.user.repository.*;
import com.asb.user.service.IMailService;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements IUserService {

    private final IUserRepository iRepository;
    private final IRolRepository iRolRepository;
    private final IAreaRepository iAreaRepository;
    private final IMailService iMailService;
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
            Optional<EntityUser> existing = iRepository.findByEmailOrLogin(
                    userDto.getEmail(), userDto.getLogin());
            if (existing.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                        "Correo o Username ya existe");
            }
        }

        EntityUser entityUser = new EntityUser();

        entityUser.setId(userDto.getId() != null ? userDto.getId() : null);
        entityUser.setName(userDto.getName());
        entityUser.setEmail(userDto.getEmail());
        entityUser.setLogin(userDto.getLogin());
        entityUser.setPhone(userDto.getPhone());
        entityUser.setStatus(Constants.ACTIVE_STATUS);

        entityUser.setRolId(userDto.getRolId());
        entityUser.setPositionId(userDto.getPositionId());
        entityUser.setCompanyId(userDto.getCompanyId());
        entityUser.setAreaId(userDto.getAreaId());

        // Password
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            entityUser.setPassword(utils.bcryptEncryptor(userDto.getPassword()));
        } else {
            entityUser.setPassword(utils.bcryptEncryptor(Constants.DEFAULT_PASSWORD));
        }

        EntityUser saved = iRepository.save(entityUser);
        return mapUserDto(saved);
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


            EntityRol ggpRolRepo = iRolRepository.findById(userDto.getRolId())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Rol no encontrado"));

            EntityCompany ggpCompanyRepo = iCompanyRepository.findById(userDto.getCompanyId())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Company no encontrado"));

            EntityPosition ggpPositionRepo = iPositionRepository.findById(userDto.getPositionId())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Position no encontrado"));

            EntityArea ggpAreaRepo = iAreaRepository.findById(userDto.getAreaId())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Area no encontrada"));


            EntityUser entityUserRepo = iRepository.findById(userId).orElse(new EntityUser());
            entityUserRepo.setName(userDto.getName());
            entityUserRepo.setEmail(userDto.getEmail());
            entityUserRepo.setLogin(userDto.getLogin());
            entityUserRepo.setStatus(("ACTIVE"));
            entityUserRepo.setPhone(userDto.getPhone());
            entityUserRepo.setRolId(userDto.getRolId());
            entityUserRepo.setPositionId(userDto.getPositionId());
            entityUserRepo.setCompanyId(userDto.getCompanyId());
            entityUserRepo.setAreaId(userDto.getAreaId());

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
    public List<GgpUserGetAllDto> getAllTerminalByUser(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;
        Long terminalId = null;

        if (customQuery.containsKey("status") && !customQuery.get("status").trim().isEmpty()) {
            status = customQuery.get("status").trim();
        }

        if (customQuery.containsKey("terminalId") && !customQuery.get("terminalId").trim().isEmpty()) {
            terminalId = Long.valueOf(customQuery.get("terminalId").trim());
        }

        return iRepository.findUsersWithoutTerminal(status, terminalId);
    }

    @Override
    @Transactional
    public UserDto status(long userId, StatusUserDto statusUser) {
        EntityUser entityUser = iRepository.findById(userId)
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        if (!Constants.ACTIVE_STATUS.equals(statusUser.getStatus()) &&
                !Constants.INACTIVE_STATUS.equals(statusUser.getStatus())) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Estado inválido. Use 'ACTIVE' o 'INACTIVE'");
        }

        entityUser.setStatus(statusUser.getStatus());
        EntityUser updated = iRepository.save(entityUser);

        return mapUserDto(updated);
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
            Long rolId = Constants.ROLD_ID_CONDUCTOR;

            if (customQuery.containsKey("status") && !customQuery.get("status").trim().isEmpty()) {
                status = customQuery.get("status").trim();
            }
            if (customQuery.containsKey("rolId") && !customQuery.get("rolId").trim().isEmpty()) {
                rolId = Long.valueOf(customQuery.get("rolId").trim());
            }
            return iRepository.findByStatusAndRolId(status, rolId).stream()
                    .map(user -> GgpUserGetAllDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .login(user.getLogin())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .rolId(user.getRolId())
                            .rolName("Conductor")
                            .positionId(user.getPositionId())
                            .positionName(user.getName())
                            .companyId(user.getCompanyId())
                            .companyName(user.getName())
                            .areaId(user.getAreaId())
                            .areaName(user.getName())
                            .status(user.getStatus())
                            .build())
                    .collect(Collectors.toList());
        }


    @Override
    public List<GgpUserGetAllDto> getAllRolByUser(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;
        Long rol = Constants.ROLD_ID_CONDUCTOR;
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("rol")) {
            rol = Long.valueOf(customQuery.get("rol"));
        }

        return iRepository.findByStatus(status).stream()
                .map(objects -> GgpUserGetAllDto.builder()
                        .id(objects.getId())
                        .name(objects.getName())
                        .login(objects.getLogin())
                        .password(objects.getPassword())
                        .email(objects.getEmail())
                        .rolId(objects.getRolId())
                        .rolName(objects.getName())
                        .positionId(objects.getPositionId())
                        .positionName(objects.getName())
                        .companyId(objects.getCompanyId())
                        .companyName(objects.getName())
                        .areaId(objects.getAreaId())
                        .areaName(objects.getName())
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

        String id = null;
        String userName = null;
        String email = null;
        String login = null;
        String phone = null;
        String companyName = null;
        String positionDescription = null;
        String areaDescription = null;
        String rolName = null;

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

        if (customQuery.containsKey("id") && !customQuery.get("id").trim().isEmpty()) {
            id = "%" + customQuery.get("id").trim() + "%";
        }

        if (customQuery.containsKey("name") && !customQuery.get("name").trim().isEmpty()) {
            userName = "%" + customQuery.get("name").trim() + "%";
        }

        if (customQuery.containsKey("email") && !customQuery.get("email").trim().isEmpty()) {
            email = "%" + customQuery.get("email").trim() + "%";
        }

        if (customQuery.containsKey("login") && !customQuery.get("login").trim().isEmpty()) {
            login = "%" + customQuery.get("login").trim() + "%";
        }

        if (customQuery.containsKey("phone") && !customQuery.get("phone").trim().isEmpty()) {
            phone = "%" + customQuery.get("phone").trim() + "%";
        }

        if (customQuery.containsKey("companyName") && !customQuery.get("companyName").trim().isEmpty()) {
            companyName = "%" + customQuery.get("companyName").trim() + "%";
        }

        if (customQuery.containsKey("positionName") && !customQuery.get("positionName").trim().isEmpty()) {
            positionDescription = "%" + customQuery.get("positionName").trim() + "%";
        }

        if (customQuery.containsKey("areaName") && !customQuery.get("areaName").trim().isEmpty()) {
            areaDescription = "%" + customQuery.get("areaName").trim() + "%";
        }

        if (customQuery.containsKey("rolName") && !customQuery.get("rolName").trim().isEmpty()) {
            rolName = "%" + customQuery.get("rolName").trim() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<UserResponsiveDto> responsivePage = iRepository.searchFiltered(
                id,
                userName,
                email,
                login,
                phone,
                companyName,
                positionDescription,
                areaDescription,
                rolName,
                status,
                pagingSort
        );

        return responsivePage.map(this::mapToGgpUserGetAllDto);
    }
    private GgpUserGetAllDto mapToGgpUserGetAllDto(UserResponsiveDto dto) {
        if (dto == null) {
            return null;
        }
        RolDto rolDto = RolDto.builder()
                .name(dto.getRolName())
                .build();

        return GgpUserGetAllDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .login(dto.getLogin())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .rolId(dto.getId())
                .rolName(dto.getRolName())
                .companyId(dto.getId())
                .companyName(dto.getCompanyName())
                .areaId(dto.getId())
                .areaName(dto.getAreaDescription())
                .positionId(dto.getId())
                .positionName(dto.getPositionDescription())
                .status(dto.getStatus())
                .build();
    }

    @Transactional
    @Override
    public ForgotPasswordUserDto forgotPassword(GgpForgotPasswordDto ggpForgotPasswordDto) {
        Optional<EntityUser> objectOptional = iRepository.findByEmail(ggpForgotPasswordDto.getEmail())
                .stream()
                .findFirst();

        if (!objectOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Objecto NO existe");
        }

        Random random = new Random();
        EntityUser ggpUser = objectOptional.get();
        String password = Constants.RECOVER_PASSWORD + "+" + random.nextInt(10);
        String encodedPassword = utils.bcryptEncryptor(password);
        ggpUser.setPassword(encodedPassword);

        iRepository.save(ggpUser);

        try {
            iMailService.forgotPassword(ggpForgotPasswordDto.getEmail(), password);

            return ForgotPasswordUserDto.builder()
                    .email(ggpForgotPasswordDto.getEmail())
                    .build();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }


    private UserDto mapUserDto(EntityUser objectUser) {
        UserDto objectDtoVo = new UserDto();
        BeanUtils.copyProperties(objectUser, objectDtoVo);

        objectDtoVo.setRolId(objectUser.getRolId());
        objectDtoVo.setCompanyId(objectUser.getCompanyId());
        objectDtoVo.setPositionId(objectUser.getPositionId());
        objectDtoVo.setAreaId(objectUser.getAreaId());

        return objectDtoVo;
    }


    private Page<GgpUserGetAllDto> mapPageUserDto(Page<EntityUser> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                ObjectMapperUtils.mapAll(entityPage.getContent(),
                        GgpUserGetAllDto.class),
                pagingSort, totalElements).map(ggpUserGetAllDto -> {
            ggpUserGetAllDto.setRolId(ggpUserGetAllDto.getRolId());
            return ggpUserGetAllDto;
        });
    }
}
