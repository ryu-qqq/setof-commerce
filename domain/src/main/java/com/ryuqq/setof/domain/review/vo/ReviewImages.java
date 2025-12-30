package com.ryuqq.setof.domain.review.vo;

import com.ryuqq.setof.domain.review.exception.ReviewImageLimitExceededException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ReviewImages {

    private static final int MAX_IMAGE_COUNT = 3;

    private final List<ReviewImage> images;

    private ReviewImages(List<ReviewImage> images) {
        validate(images);
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
    }

    public static ReviewImages of(List<ReviewImage> images) {
        return new ReviewImages(images);
    }

    public static ReviewImages empty() {
        return new ReviewImages(Collections.emptyList());
    }

    private void validate(List<ReviewImage> images) {
        if (images != null && images.size() > MAX_IMAGE_COUNT) {
            throw new ReviewImageLimitExceededException(images.size());
        }
    }

    public List<ReviewImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public int size() {
        return images.size();
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }

    public boolean hasImages() {
        return !images.isEmpty();
    }

    public boolean canAddMore() {
        return images.size() < MAX_IMAGE_COUNT;
    }

    public int remainingCapacity() {
        return MAX_IMAGE_COUNT - images.size();
    }

    public ReviewImages withAdded(ReviewImage image) {
        if (!canAddMore()) {
            throw new ReviewImageLimitExceededException(images.size() + 1);
        }
        List<ReviewImage> newImages = new ArrayList<>(images);
        newImages.add(image);
        return new ReviewImages(newImages);
    }

    public ReviewImages replace(List<ReviewImage> newImages) {
        return new ReviewImages(newImages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewImages that = (ReviewImages) o;
        return Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(images);
    }

    @Override
    public String toString() {
        return "ReviewImages{images=" + images + "}";
    }
}
