package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.IncomeRequest;
import com.sadman.financial.entity.Income;

public interface IIncomeService {
    Income logIncome(IncomeRequest incomeRequest);
}
