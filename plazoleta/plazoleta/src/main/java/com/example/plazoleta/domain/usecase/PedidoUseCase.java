package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.api.IPedidoServicePort;
import com.example.plazoleta.domain.constants.PedidoUseCaseConstants;
import com.example.plazoleta.domain.exception.PedidoException;
import com.example.plazoleta.domain.exception.PedidoExceptionType;
import com.example.plazoleta.domain.model.*;
import com.example.plazoleta.domain.spi.*;
import com.example.plazoleta.domain.validations.PedidoUseCaseValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ISecurityContextPort securityContextPort;
    private final PedidoUseCaseValidation pedidoUseCaseValidation;
    private final IItemPedidoPersistencePort itemPedidoPersistencePort;
    private final ITrazabilidadPersistencePort trazabilidadPersistencePort;
    private final IUserClientPort userClientPort;
    private final IMensajeriaSmsPersistencePort smsPersistencePort;
    private static final Random RANDOM = new Random();


    public PedidoUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                         IPedidoPersistencePort pedidoPersistencePort,
                         ISecurityContextPort securityContextPort,
                         IDishPersistencePort dishPersistencePort,
                         PedidoUseCaseValidation pedidoUseCaseValidation,
                         IItemPedidoPersistencePort itemPedidoPersistencePort,
                         ITrazabilidadPersistencePort trazabilidadPersistencePort,
                         IUserClientPort userClientPort,
                         IMensajeriaSmsPersistencePort smsPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.securityContextPort = securityContextPort;
        this.dishPersistencePort = dishPersistencePort;
        this.pedidoUseCaseValidation = pedidoUseCaseValidation;
        this.itemPedidoPersistencePort = itemPedidoPersistencePort;
        this.trazabilidadPersistencePort = trazabilidadPersistencePort;
        this.userClientPort = userClientPort;
        this.smsPersistencePort = smsPersistencePort;
    }

    @Override
    public Pedido savePedido(Pedido pedidoToCreate) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();
        pedidoToCreate.setClienteId(userAuthenticatedId);

         if(!restaurantPersistencePort.existsRestaurant(pedidoToCreate.getRestaurant().getId())){
             throw new PedidoException(PedidoExceptionType.RESTAURANT_NOT_EXISTS);
         }
        Restaurant foundRestaurant = restaurantPersistencePort.findById(pedidoToCreate.getRestaurant().getId());
         pedidoToCreate.setRestaurant(foundRestaurant);

        if(Optional.ofNullable(pedidoPersistencePort.findByClienteIdAndEstadoIn(
                userAuthenticatedId,
                new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO}
        )).isPresent()){
            throw new PedidoException(PedidoExceptionType.CLIENT_STATUS_PEDIDO_IN);

        }

        pedidoToCreate.setEstado(EstadoPedido.PENDIENTE);
        pedidoToCreate.setFechaCreacion(LocalDateTime.now());

        Pedido savedPedido =  pedidoPersistencePort.savePedido(pedidoToCreate);

        List<Dish> listDishRestaurant = dishPersistencePort.getDishRestaurant(pedidoToCreate.getRestaurant().getId());
        pedidoUseCaseValidation.validateListDishResturant(listDishRestaurant);

        pedidoUseCaseValidation.validarItemsPedido(pedidoToCreate.getItems());

        List<ItemPedido> dishAvailableToPedido = new ArrayList<>();
        for (ItemPedido dishToPedido : pedidoToCreate.getItems()) {
            if (Optional.ofNullable(pedidoUseCaseValidation.validationItemDishToPedido(listDishRestaurant, dishToPedido)).isPresent()) {
                dishToPedido.setPedido(savedPedido);
                dishAvailableToPedido.add(dishToPedido);
            }
        }
        pedidoUseCaseValidation.validateListDishAvailableIsEmpty(dishAvailableToPedido);

        savedPedido.setItems(dishAvailableToPedido);
        itemPedidoPersistencePort.saveAll(dishAvailableToPedido);
        return savedPedido;
    }

    @Override
    public Page<Pedido> findByStatusPedido(EstadoPedido findEstadoPedido, Long findRestaurantId, int pagina, int cantidadPorPagina) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();
        pedidoUseCaseValidation.validationUserAuthenticatedBelongsRestaurant(userAuthenticatedId, findRestaurantId);

        Pageable paginacion = PageRequest.of(pagina - 1, cantidadPorPagina);
        Page<Pedido> paginacionPedidos = pedidoPersistencePort.findByEstadoAndRestaurantId(findEstadoPedido, findRestaurantId, paginacion);

        if(paginacionPedidos.isEmpty()){
            throw new PedidoException(PedidoExceptionType.NOT_EXISTS_PEDIDOS_STATUS_IN_RESTAURANT);
        }

        paginacionPedidos.forEach(pedido -> {
            List<ItemPedido> itemsPedido = itemPedidoPersistencePort.findByPedidoId(pedido.getId());
            pedido.setItems(itemsPedido);
        });

        return paginacionPedidos;
    }

    @Override
    public String updateStatusPedidoEnPreparacion(Long findPedidoId) {

        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Pedido pedidoUpdateToStatus = pedidoUseCaseValidation.validarYObtenerPedido(findPedidoId);

        pedidoUseCaseValidation.validationStatusPedidoActualPendiente(
                pedidoUpdateToStatus.getEstado());

        return updateStatusPedido(pedidoUpdateToStatus, EstadoPedido.EN_PREPARACION,
                userAuthenticatedId, false, null);
    }

    @Override
    public String updateStatusPedidoListo(Long findPedidoId) {

        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Pedido pedidoUpdateToStatus = pedidoUseCaseValidation.validarYObtenerPedido(findPedidoId);

        pedidoUseCaseValidation.validationStatusPedidoActualEnPreparacion(
                pedidoUpdateToStatus.getEstado());

        User clientToPedido = userClientPort.getUserById(pedidoUpdateToStatus.getClienteId());
        String pinSeguridad = this.generarPin();
        String mensajeNotification = String.format(
                PedidoUseCaseConstants.ORDER_READY_WITH_PIN,
                pinSeguridad
        );
        smsPersistencePort.sendSms(new SmsMessage(clientToPedido.getPhone(), mensajeNotification));

        return updateStatusPedido(pedidoUpdateToStatus, EstadoPedido.LISTO, userAuthenticatedId,
                false, pinSeguridad);
    }

    @Override
    public String updateStatusPedidoEntregado(Long findPedidoId, String pinSeguridad) {

        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Pedido pedidoUpdateToStatus = pedidoUseCaseValidation.validarYObtenerPedido(findPedidoId);

        pedidoUseCaseValidation.validationStatusPedidoActualEnListo(pedidoUpdateToStatus.getEstado());
        pedidoUseCaseValidation.validationPinSecurityPedido(pedidoUpdateToStatus, pinSeguridad);

        return updateStatusPedido(pedidoUpdateToStatus, EstadoPedido.ENTREGADO, userAuthenticatedId,
                true, null);
    }

    @Override
    public String canceledPedido(Long findPedidoId) {

        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Pedido pedidoUpdateToStatus = Optional.ofNullable(pedidoPersistencePort.findByIdPedido(findPedidoId))
                .orElseThrow(() -> new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO));

        pedidoUseCaseValidation.validationUserAuthenticatedBelongsClientePedido(
                userAuthenticatedId, pedidoUpdateToStatus);
        pedidoUseCaseValidation.validationStatusPedidoActualPendienteToCanceled(
                pedidoUpdateToStatus.getEstado());

        return updateStatusPedido(pedidoUpdateToStatus, EstadoPedido.CANCELADO, userAuthenticatedId,
                false, null);
    }

    @Override
    public List<String> obtainPedidoEfficiency() {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        List<Long> restaurantsPropietario = restaurantPersistencePort.findRestaurantByPropietarioId(
                userAuthenticatedId).stream().map(Restaurant::getId).toList();

        List<Pedido> pedidos = pedidoPersistencePort.findAllPedidos();
        List<String> eficienciaPedidos = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getFechaEntrega() != null && pedido.getEstado() == EstadoPedido.ENTREGADO
            && restaurantsPropietario.contains(pedido.getRestaurant().getId())) {
                Duration duracion = Duration.between(pedido.getFechaCreacion(), pedido.getFechaEntrega());
                eficienciaPedidos.add(String.format(PedidoUseCaseConstants.ORDER_TOTAL_TIME, pedido.getId(), duracion.toMinutes()));
            }
        }

        return eficienciaPedidos;
    }

    @Override
    public List<String> employeeEfficiencyRanking() {

        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        List<Long> restaurantsPropietario = restaurantPersistencePort.findRestaurantByPropietarioId(
                userAuthenticatedId).stream().map(Restaurant::getId).toList();

        List<Pedido> pedidos = pedidoPersistencePort.findAllPedidos();
        Map<Long, List<Pedido>> pedidosPorEmpleado = new HashMap<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getFechaEntrega() != null && pedido.getEstado() == EstadoPedido.ENTREGADO
            && restaurantsPropietario.contains(pedido.getRestaurant().getId())) {
                pedidosPorEmpleado.computeIfAbsent(pedido.getEmpleadoId(), k -> new ArrayList<>()).add(pedido);
            }
        }

        List<String> rankingEficiencia = new ArrayList<>();

        for (Map.Entry<Long, List<Pedido>> entry : pedidosPorEmpleado.entrySet()) {
            Long empleadoId = entry.getKey();
            List<Pedido> pedidosEmpleado = entry.getValue();

            long totalTiempo = 0;
            for (Pedido pedido : pedidosEmpleado) {
                totalTiempo += Duration.between(pedido.getFechaCreacion(), pedido.getFechaEntrega()).toMinutes();
            }

            long promedioTiempo = totalTiempo / pedidosEmpleado.size();
            rankingEficiencia.add(String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME, empleadoId, promedioTiempo));
        }

        rankingEficiencia.sort(Comparator.comparingInt(s -> {
            String[] partes = s.split(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME_SPLIT);
            return Integer.parseInt(partes[1].replace(PedidoUseCaseConstants.MINUTES_CONSTANTS, "").trim());
        }));

        return rankingEficiencia;
    }

    @Override
    public List<TrazabilidadPedido> obtenerTrazabilidadPedido(Long findPedidoId) {
        return trazabilidadPersistencePort.obtenerTrazabilidadPedido(findPedidoId);
    }


    public String generarPin(){
        return String.format(PedidoUseCaseConstants.PIN_FORMAT, RANDOM.nextInt(PedidoUseCaseConstants.PIN_MAX_VALUE));
    }

    public String updateStatusPedido(Pedido updatePedido, EstadoPedido newEstatus,
                                     Long userAuthenticatedId, boolean actualizarFechaEntrega,
                                     String pinSeguridad){
        EstadoPedido estadoAnterior = updatePedido.getEstado();
        updatePedido.setEstado(newEstatus);

        if(updatePedido.getEmpleadoId() == null) {
            updatePedido.setEmpleadoId(userAuthenticatedId);
        }

        if (pinSeguridad != null) {
            updatePedido.setPinSeguridad(pinSeguridad);
        }

        if (actualizarFechaEntrega) {
            updatePedido.setFechaEntrega(LocalDateTime.now());
        }

        Pedido updatedStatusPedido = pedidoPersistencePort.updateStatusPedido(updatePedido);
        return saveTrazabilidadPedido(updatedStatusPedido, userAuthenticatedId, estadoAnterior);
    }

    public String saveTrazabilidadPedido(Pedido updatedStatusPedido, Long userAuthenticatedId,
                                          EstadoPedido estadoAnterior){
        TrazabilidadPedido trazabilidad = pedidoUseCaseValidation
                .createTrazabilidadToUpdateStatusPedido(updatedStatusPedido, userAuthenticatedId,
                        estadoAnterior);

        try {
            trazabilidadPersistencePort.savedTrazabilidad(trazabilidad);
            return PedidoUseCaseConstants.ORDER_STATUS_UPDATED_SUCCESS + estadoAnterior +
                    " a " + updatedStatusPedido.getEstado();
        } catch (Exception e) {
            throw new PedidoException(PedidoExceptionType
                    .ERROR_UPDATE_STATUS_PEDIDO, PedidoUseCaseConstants.ERROR_PREFIX +
                    e.getMessage());
        }
    }
}
