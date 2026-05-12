package com.eci.edu.ieti.tastemap.restaurant.service.serviceImpl;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import com.eci.edu.ieti.tastemap.restaurant.service.AzureStorageService;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the RestaurantService interface.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AzureStorageService azureStorageService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper, AzureStorageService azureStorageService) {
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
    public Restaurant create(String ownerId,
                             String name,
                             String phone,
                             String description,
                             String theme,
                             List<String> locations,
                             List<String> tags,
                             Integer priceMin,
                             Integer priceMax,
                             String hour,
                             MultipartFile logoFile,
                             MultipartFile menuFile) {
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setOwnerId(ownerId);
        restaurantRequestDto.setName(name);
        restaurantRequestDto.setPhone(phone);
        restaurantRequestDto.setDescription(description);
        restaurantRequestDto.setTheme(theme);
        restaurantRequestDto.setLocations(locations == null ? null : new LinkedHashSet<>(locations));
        restaurantRequestDto.setTags(tags == null ? null : new LinkedHashSet<>(tags));
        restaurantRequestDto.setPriceMin(priceMin);
        restaurantRequestDto.setPriceMax(priceMax);
        restaurantRequestDto.setHour(hour);

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
    public String getOpenStatus(String hourRange) {
        return isRestaurantOpen(hourRange) ? "ABIERTO" : "CERRADO";
    }

    @Override
    public RestaurantOpenStatusResponseDto getOpenStatusByRestaurantId(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));
        return new RestaurantOpenStatusResponseDto(restaurant.getId(), getOpenStatus(restaurant.getHour()));
    }

    @Override
    public void deleteById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        deleteIfPresent(restaurant.getLogo());
        deleteIfPresent(restaurant.getMenu());
        restaurantRepository.deleteById(id);
    }

    private Restaurant updateFromDto(String id, RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        cleanupIfRemovedOrChanged(restaurant.getLogo(), restaurantRequestDto.getLogo());
        cleanupIfRemovedOrChanged(restaurant.getMenu(), restaurantRequestDto.getMenu());

        restaurant.setOwnerId(restaurantRequestDto.getOwnerId());
        restaurant.setName(restaurantRequestDto.getName());
        restaurant.setPhone(restaurantRequestDto.getPhone());
        restaurant.setDescription(restaurantRequestDto.getDescription());
        restaurant.setLogo(restaurantRequestDto.getLogo());
        restaurant.setMenu(restaurantRequestDto.getMenu());
        restaurant.setTheme(restaurantRequestDto.getTheme());
        restaurant.setLocations(normalizeLocations(restaurantRequestDto.getLocations()));
        restaurant.setTags(restaurantRequestDto.getTags());
        restaurant.setPriceMin(restaurantRequestDto.getPriceMin());
        restaurant.setPriceMax(restaurantRequestDto.getPriceMax());
        restaurant.setHour(restaurantRequestDto.getHour());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant update(String id,
                             String ownerId,
                             String name,
                             String phone,
                             String description,
                             String theme,
                             List<String> locations,
                             List<String> tags,
                             Integer priceMin,
                             Integer priceMax,
                             String hour,
                             MultipartFile logoFile,
                             MultipartFile menuFile) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));

        RestaurantRequestDto request = new RestaurantRequestDto();
        request.setOwnerId(ownerId != null ? ownerId : existing.getOwnerId());
        request.setName(name != null ? name : existing.getName());
        request.setPhone(phone != null ? phone : existing.getPhone());
        request.setDescription(description != null ? description : existing.getDescription());
        request.setTheme(theme != null ? theme : existing.getTheme());
        request.setLocations(locations != null ? new LinkedHashSet<>(locations) : existing.getLocations());
        request.setTags(tags != null ? new LinkedHashSet<>(tags) : existing.getTags());
        request.setPriceMin(priceMin != null ? priceMin : existing.getPriceMin());
        request.setPriceMax(priceMax != null ? priceMax : existing.getPriceMax());
        request.setHour(hour != null ? hour : existing.getHour());
        request.setLogo(existing.getLogo());
        request.setMenu(existing.getMenu());

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

    private Set<String> normalizeLocations(Set<String> locations) {
        if (locations == null) {
            return null;
        }

        return locations.stream()
                .filter(location -> location != null && !location.trim().isEmpty())
                .map(String::trim)
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

    private boolean isRestaurantOpen(String hourRange) {
        if (hourRange == null || hourRange.isBlank() || !hourRange.contains("-")) {
            return false;
        }

        String[] parts = hourRange.split("-");
        if (parts.length != 2) {
            return false;
        }

        LocalTime start = parseHour(parts[0].trim());
        LocalTime end = parseHour(parts[1].trim());
        if (start == null || end == null) {
            return false;
        }

        LocalTime now = LocalTime.now(ZoneId.of("America/Bogota"));
        if (end.isAfter(start)) {
            return !now.isBefore(start) && now.isBefore(end);
        }

        return !now.isBefore(start) || now.isBefore(end);
    }

    private LocalTime parseHour(String value) {
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
}

