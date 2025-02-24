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
        String userAuthenticatedRol = securityContextPort.getUserAuthenticateRol();
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Restaurant foundRestaurant = restaurantPersistencePort.findById(dishToCreate.getRestaurant().getId());

        dishUseCaseValidation.valitationCreatedDish(userAuthenticatedRol, userAuthenticatedId, foundRestaurant);

        dishToCreate.setActive(true);
        return dishPersistencePort.saveDish(dishToCreate);
    }

    @Override
    public Dish updateDish(Long findDishId, Dish dishToUpdate) {
        String userAuthenticatedRol = securityContextPort.getUserAuthenticateRol();
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Dish oldDish = dishPersistencePort.findById(findDishId);

        dishUseCaseValidation.validationUpdateDish(userAuthenticatedRol, userAuthenticatedId, oldDish);

        dishToUpdate.setId(oldDish.getId());
        dishToUpdate.setRestaurant(oldDish.getRestaurant());
        dishToUpdate.setActive(oldDish.getActive());
        return dishPersistencePort.updateDish(dishToUpdate);
    }

    @Override
    public Dish updateDishStatus(Long findDishId) {
        String userAuthenticatedRol = securityContextPort.getUserAuthenticateRol();
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Dish dishToUpdate = dishPersistencePort.findById(findDishId);

        dishUseCaseValidation.validationUpdateStatusDish(userAuthenticatedRol, userAuthenticatedId, dishToUpdate);

        dishToUpdate.setActive(!dishToUpdate.getActive());
        return dishPersistencePort.updateDish(dishToUpdate);
    }

    @Override
    public List<Dish> getAllDish() {
        List<Dish> listAllDish= dishPersistencePort.getAllDish();
        if(listAllDish.isEmpty()){
            throw new DishException(DishExceptionType.DISH_NOT_DATA);
        }
        return listAllDish;
    }

    @Override
    public List<Dish> getDishRestaurant(Long findRestaurantId) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();
        Restaurant foundRestaurant = restaurantPersistencePort.findById(findRestaurantId);

        dishUseCaseValidation.validationFindDishRestaurant(foundRestaurant, userAuthenticatedId);
        return dishPersistencePort.getDishRestaurant(findRestaurantId);
    }

    @Override
    public Dish findById(Long findDishId) {
        Dish foundDish = dishPersistencePort.findById(findDishId);
        if(foundDish == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }
        return foundDish;
    }
}
