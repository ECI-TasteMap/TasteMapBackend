package com.eci.edu.ieti.tastemap.restaurant.service.serviceImpl;

import com.eci.edu.ieti.tastemap.restaurant.dto.LocationOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import com.eci.edu.ieti.tastemap.restaurant.service.AzureStorageService;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the RestaurantService interface.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AzureStorageService azureStorageService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 RestaurantMapper restaurantMapper,
                                 AzureStorageService azureStorageService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.azureStorageService = azureStorageService;
    }

    @Override
    public Restaurant create(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantMapper.toRestaurant(restaurantRequestDto);
        restaurant.setLocations(normalizeLocations(restaurantRequestDto.getLocations()));
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant create(RestaurantRequestDto restaurantRequestDto,
                             MultipartFile logoFile,
                             MultipartFile menuFile) {
        if (restaurantRequestDto == null) {
            restaurantRequestDto = new RestaurantRequestDto();
        }

        if (logoFile != null && !logoFile.isEmpty()) {
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed for logo");
            }
            restaurantRequestDto.setLogo(azureStorageService.uploadImage(logoFile));
        }

        if (menuFile != null && !menuFile.isEmpty()) {
            String contentType = menuFile.getContentType();
            if (contentType == null || !(contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu file must be an image or PDF");
            }
            restaurantRequestDto.setMenu(azureStorageService.uploadMenu(menuFile));
        }

        return create(restaurantRequestDto);
    }

    @Override
    public Optional<Restaurant> findById(String id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public List<Restaurant> all() {
        return restaurantRepository.findAll();
    }

    @Override
    public String getOpenStatus(Location location) {
        return isLocationOpen(location) ? "ABIERTO" : "CERRADO";
    }

    @Override
    public RestaurantOpenStatusResponseDto getOpenStatusByRestaurantId(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        List<LocationOpenStatusResponseDto> locationStatuses = restaurant.getLocations() == null
                ? List.of()
                : restaurant.getLocations().stream()
                .map(location -> new LocationOpenStatusResponseDto(location, getOpenStatus(location)))
                .collect(Collectors.toList());

        return new RestaurantOpenStatusResponseDto(restaurant.getId(), locationStatuses);
    }

    @Override
    public void deleteById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        deleteIfPresent(restaurant.getLogo());
        deleteIfPresent(restaurant.getMenu());
        restaurantRepository.deleteById(id);
    }

    @Override
    public Restaurant update(String id,
                             RestaurantRequestDto restaurantRequestDto,
                             MultipartFile logoFile,
                             MultipartFile menuFile) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        RestaurantRequestDto request = new RestaurantRequestDto();
        if (restaurantRequestDto != null) {
            request.setOwnerId(restaurantRequestDto.getOwnerId() != null ? restaurantRequestDto.getOwnerId() : existing.getOwnerId());
            request.setName(restaurantRequestDto.getName() != null ? restaurantRequestDto.getName() : existing.getName());
            request.setDescription(restaurantRequestDto.getDescription() != null ? restaurantRequestDto.getDescription() : existing.getDescription());
            request.setTheme(restaurantRequestDto.getTheme() != null ? restaurantRequestDto.getTheme() : existing.getTheme());
            request.setLocations(restaurantRequestDto.getLocations() != null ? restaurantRequestDto.getLocations() : existing.getLocations());
            request.setTags(restaurantRequestDto.getTags() != null ? restaurantRequestDto.getTags() : existing.getTags());
            request.setPriceMin(restaurantRequestDto.getPriceMin() != null ? restaurantRequestDto.getPriceMin() : existing.getPriceMin());
            request.setPriceMax(restaurantRequestDto.getPriceMax() != null ? restaurantRequestDto.getPriceMax() : existing.getPriceMax());
        } else {
            request.setOwnerId(existing.getOwnerId());
            request.setName(existing.getName());
            request.setDescription(existing.getDescription());
            request.setTheme(existing.getTheme());
            request.setLocations(existing.getLocations());
            request.setTags(existing.getTags());
            request.setPriceMin(existing.getPriceMin());
            request.setPriceMax(existing.getPriceMax());
        }

        request.setLogo(existing.getLogo());
        request.setMenu(existing.getMenu());

        cleanupIfRemovedOrChanged(existing.getLogo(), request.getLogo());
        cleanupIfRemovedOrChanged(existing.getMenu(), request.getMenu());

        if (logoFile != null && !logoFile.isEmpty()) {
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed for logo");
            }
            request.setLogo(azureStorageService.uploadImage(logoFile));
        }

        if (menuFile != null && !menuFile.isEmpty()) {
            String contentType = menuFile.getContentType();
            if (contentType == null || !(contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu file must be an image or PDF");
            }
            request.setMenu(azureStorageService.uploadMenu(menuFile));
        }

        return updateFromDto(id, request);
    }

    private Restaurant updateFromDto(String id, RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        cleanupIfRemovedOrChanged(restaurant.getLogo(), restaurantRequestDto.getLogo());
        cleanupIfRemovedOrChanged(restaurant.getMenu(), restaurantRequestDto.getMenu());

        restaurant.setOwnerId(restaurantRequestDto.getOwnerId());
        restaurant.setName(restaurantRequestDto.getName());
        restaurant.setDescription(restaurantRequestDto.getDescription());
        restaurant.setLogo(restaurantRequestDto.getLogo());
        restaurant.setMenu(restaurantRequestDto.getMenu());
        restaurant.setTheme(restaurantRequestDto.getTheme());
        restaurant.setLocations(normalizeLocations(restaurantRequestDto.getLocations()));
        restaurant.setTags(restaurantRequestDto.getTags());
        restaurant.setPriceMin(restaurantRequestDto.getPriceMin());
        restaurant.setPriceMax(restaurantRequestDto.getPriceMax());
        return restaurantRepository.save(restaurant);
    }

    private List<Location> normalizeLocations(List<Location> locations) {
        if (locations == null) {
            return null;
        }

        return locations.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeLocation)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Location normalizeLocation(Location location) {
        if (!StringUtils.hasText(location.getAddress()) && !StringUtils.hasText(location.getPhone()) && location.getSchedules() == null) {
            return null;
        }

        if (!StringUtils.hasText(location.getId())) {
            location.setId(UUID.randomUUID().toString());
        }

        location.setAddress(trimToNull(location.getAddress()));
        location.setPhone(trimToNull(location.getPhone()));
        location.setSchedules(normalizeSchedules(location.getSchedules()));
        return location;
    }

    private List<Schedule> normalizeSchedules(List<Schedule> schedules) {
        if (schedules == null) {
            return null;
        }

        return schedules.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeSchedule)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Schedule normalizeSchedule(Schedule schedule) {
        if (schedule.getDays() != null) {
            schedule.setDays(schedule.getDays().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }

        schedule.setOpenTime(trimToNull(schedule.getOpenTime()));
        schedule.setCloseTime(trimToNull(schedule.getCloseTime()));
        return schedule;
    }

    private void cleanupIfRemovedOrChanged(String existingUrl, String newUrl) {
        if (!StringUtils.hasText(existingUrl)) {
            return;
        }
        if (!StringUtils.hasText(newUrl) || !existingUrl.equals(newUrl)) {
            azureStorageService.deleteFileByUrl(existingUrl);
        }
    }

    private void deleteIfPresent(String fileUrl) {
        if (StringUtils.hasText(fileUrl)) {
            azureStorageService.deleteFileByUrl(fileUrl);
        }
    }

    private boolean isLocationOpen(Location location) {
        if (location == null || location.getSchedules() == null || location.getSchedules().isEmpty()) {
            return false;
        }

        ZoneId zoneId = ZoneId.of("America/Bogota");
        DayOfWeek currentDay = LocalDate.now(zoneId).getDayOfWeek();
        LocalTime now = LocalTime.now(zoneId);

        return location.getSchedules().stream().anyMatch(schedule -> isScheduleOpen(schedule, currentDay, now));
    }

    private boolean isScheduleOpen(Schedule schedule, DayOfWeek currentDay, LocalTime now) {
        if (schedule == null || schedule.isClosed() || schedule.getDays() == null || !schedule.getDays().contains(currentDay)) {
            return false;
        }

        LocalTime openTime = parseHour(schedule.getOpenTime());
        LocalTime closeTime = parseHour(schedule.getCloseTime());
        if (openTime == null || closeTime == null) {
            return false;
        }

        if (closeTime.isAfter(openTime)) {
            return !now.isBefore(openTime) && now.isBefore(closeTime);
        }

        return !now.isBefore(openTime) || now.isBefore(closeTime);
    }

    private LocalTime parseHour(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("H:mm"),
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("H"),
                DateTimeFormatter.ofPattern("HH")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }
        return null;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }
}