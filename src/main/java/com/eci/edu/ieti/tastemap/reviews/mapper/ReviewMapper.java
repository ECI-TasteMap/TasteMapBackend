package com.eci.edu.ieti.tastemap.reviews.mapper;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.dto.ReviewResponseDto;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Review, ReviewRequestDto, and ReviewResponseDto.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    Review toReview(ReviewRequestDto reviewRequestDto);

    ReviewResponseDto toReviewResponseDto(Review review);
}

