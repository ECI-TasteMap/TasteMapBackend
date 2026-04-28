package com.eci.edu.ieti.tastemap.restaurant.service.serviceImpl;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RestaurantService interface.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public Restaurant create(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantMapper.toRestaurant(restaurantRequestDto);
        return restaurantRepository.save(restaurant);
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
    public void deleteById(String id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException("Restaurant with id " + id + " not found");
        }
        restaurantRepository.deleteById(id);
    }

    @Override
    public Restaurant update(String id, RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));
        restaurant.setOwnerId(restaurantRequestDto.getOwnerId());
        restaurant.setName(restaurantRequestDto.getName());
        restaurant.setDescription(restaurantRequestDto.getDescription());
        restaurant.setLogo(restaurantRequestDto.getLogo());
        restaurant.setMenu(restaurantRequestDto.getMenu());
        restaurant.setTheme(restaurantRequestDto.getTheme());
        restaurant.setIdComment(restaurantRequestDto.getIdComment());
        restaurant.setHour(restaurantRequestDto.getHour());
        return restaurantRepository.save(restaurant);
    }
}

