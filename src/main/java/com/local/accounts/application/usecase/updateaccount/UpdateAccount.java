package com.local.accounts.application.usecase.updateaccount;

import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.router.dto.AccountDTO;

import java.util.function.BiFunction;

public interface UpdateAccount extends BiFunction<String, AccountDTO, AccountAggregate> {


//    ClienteModel obtenerClientePorId(Long id);
//
//    void borrarCliente(Long id);
//
//    Metrica calcularMetricas();
//
//    List<ClientePlusDto> obtenerClientesConMetrica();
}
