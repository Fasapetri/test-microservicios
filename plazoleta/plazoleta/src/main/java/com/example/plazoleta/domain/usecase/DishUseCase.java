package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.DishExceptionType;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.spi.IDishPersistencePort;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.ISecurityContextPort;
import com.example.plazoleta.domain.validations.DishUseCaseValidation;

import java.util.List;
import java.util.Optional;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ISecurityContextPort securityContextPort;
    private final DishUseCaseValidation dishUseCaseValidation;

    public DishUseCase(IDishPersistencePort iDishPersistencePort, IRestaurantPersistencePort iRestaurantPersistencePort, ISecurityContextPort iSecurityContextPort, DishUseCaseValidation dishUseCaseValidation) {
        this.dishPersistencePort = iDishPersistencePort;
        this.restaurantPersistencePort = iRestaurantPersistencePort;
        this.securityContextPort = iSecurityContextPort;
        this.dishUseCaseValidation = dishUseCaseValidation;
    }

    @Override
    public Dish saveDish(Dish dishToCreate) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Restaurant foundRestaurant = restaurantPersistencePort.findById(dishToCreate.getRestaurant().getId());

        dishUseCaseValidation.valitationCreatedDish(userAuthenticatedId, foundRestaurant);

        return dishPersistencePort.saveDish(dishToCreate.toBuilder().active(true).build());
    }

    @Override
    public Dish updateDish(Long findDishId, Dish dishToUpdate) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Dish oldDish = dishPersistencePort.findById(findDishId);

        dishUseCaseValidation.validationUpdateDish(userAuthenticatedId, oldDish);

        return dishPersistencePort.updateDish(dishToUpdate.toBuilder()
                .id(oldDish.getId())
                .restaurant(oldDish.getRestaurant())
                .active(oldDish.getActive())
                .build());
    }

    @Override
    public Dish updateDishStatus(Long findDishId) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Dish dishToUpdate = dishPersistencePort.findById(findDishId);

        dishUseCaseValidation.validationUpdateStatusDish(userAuthenticatedId, dishToUpdate);

        return dishPersistencePort.updateDish(dishToUpdate.toBuilder().active(!dishToUpdate.getActive()).build());
    }

    @Override
    public List<Dish> getAllDish() {
        return Optional.ofNullable(dishPersistencePort.getAllDish())
                .orElseThrow(() -> new DishException(DishExceptionType.DISH_NOT_DATA));
    }

    @Override
    public List<Dish> getDishRestaurantCategory(Long findRestaurantId, String dishCategory) {
        Restaurant foundRestaurant = restaurantPersistencePort.findById(findRestaurantId);

        dishUseCaseValidation.validationFindDishRestaurant(foundRestaurant);
        return dishPersistencePort.getDishRestaurantCategory(findRestaurantId, dishCategory);
    }

    @Override
    public Dish findById(Long findDishId) {
        return Optional.ofNullable(dishPersistencePort.findById(findDishId))
                .orElseThrow(() -> new DishException(DishExceptionType.NOT_EXISTS_DISH));
    }
}
