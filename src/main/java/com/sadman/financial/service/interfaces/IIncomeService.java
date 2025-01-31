package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.IncomeRequest;
import com.sadman.financial.entity.Income;
import com.sadman.financial.responses.IncomeResponse;

import java.util.Map;

public interface IIncomeService {
    Income logIncome(IncomeRequest incomeRequest);

    IncomeResponse getIncomeById(Long incomeId);

    Map<String, Object> search(Integer page, Integer size, String sortBy, String search);

    IncomeResponse updateIncome(Long incomeId, IncomeRequest incomeRequest);
}
