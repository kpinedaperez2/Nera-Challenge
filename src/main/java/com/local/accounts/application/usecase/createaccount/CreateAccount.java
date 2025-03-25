package com.local.accounts.application.usecase.createaccount;

import com.local.accounts.router.dto.AccountDTO;

import java.util.function.Function;

public interface CreateAccount extends Function<AccountDTO, String> {


//    ClienteModel obtenerClientePorId(Long id);
//
//    void borrarCliente(Long id);
//
//    Metrica calcularMetricas();
//
//    List<ClientePlusDto> obtenerClientesConMetrica();
}
